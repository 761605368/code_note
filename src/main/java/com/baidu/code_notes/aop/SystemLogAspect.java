package com.baidu.code_notes.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.code_notes.aop.entity.SystemLogDO;
import com.baidu.code_notes.publish.SystemLogDOEven;
import com.baidu.code_notes.utils.ThreadLocalUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * @author lxh
 * @date 2022/3/6 17:42
 */
@Aspect
@Component
public class SystemLogAspect {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 切入条件：
     * 1.方法上有 @SystemLog 
     * 或
     * 2.类上有 @SystemLog并且方法上没有 @IgnoreSystemLog
     */
    @Pointcut("@annotation(com.baidu.code_notes.aop.SystemLog)" +
            "|| (@within(com.baidu.code_notes.aop.SystemLog) && !@annotation(com.baidu.code_notes.aop.IgnoreSystemLog)) ")
    public void pointcut() {
        
    }
    
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        //获取注解及其消息
        SystemLog annotation = method.getAnnotation(SystemLog.class);
        //尝试从类上获取参数
        if (ObjectUtil.isNull(annotation)) {
            annotation = method.getDeclaringClass().getAnnotation(SystemLog.class);
        }
        String message = annotation.value();

        //获取方法的入参和url
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = URLUtil.getPath(request.getRequestURI());

        //new 一个系统日志实体类
        SystemLogDO systemLogDO = new SystemLogDO();
        systemLogDO.setMessage(message)
                    .setStartTime(LocalDateTime.now())
                    .setOperatorId(1L)
                    .setOperatorName("li")
                    .setParameters(JSON.toJSONString(joinPoint.getArgs()))
                    .setUrl(url);

        //存入ThreadLocal
        ThreadLocalUtil.put("systemLogDO", systemLogDO);
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            systemLogDO.setErrorMessage(throwable.getMessage());
        }
        systemLogDO.setEndTime(LocalDateTime.now());
        //模拟保存日志
        System.out.println(JSON.toJSONString(systemLogDO));
        //发布事件
        applicationContext.publishEvent(new SystemLogDOEven(systemLogDO));
        //清空ThreadLocal
        ThreadLocalUtil.clear();

        return proceed;
    }
}
