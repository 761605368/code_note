package com.baidu.code_notes.publish.service.impl;

import com.alibaba.fastjson.JSON;
import com.baidu.code_notes.aop.entity.SystemLogDO;
import com.baidu.code_notes.publish.SystemLogDOEven;
import com.baidu.code_notes.publish.service.SystemLogService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 事件监听者
 * @author lxh
 * @date 2022/3/6 20:17
 */
@Service
public class SystemLogServiceImpl implements SystemLogService, ApplicationListener<SystemLogDOEven> {
    @Override
    public void onApplicationEvent(SystemLogDOEven event) {
        System.out.println("开始处理，，，");
        System.out.println(JSON.toJSONString(event.getSource()));
        System.out.println("处理完成");
    }
}
