package com.baidu.code_notes.publish;

import org.springframework.context.ApplicationEvent;

/**
 * @author lxh
 * @date 2022/3/6 20:11
 */
public class SystemLogDOEven  extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public SystemLogDOEven(Object source) {
        super(source);
    }
}
