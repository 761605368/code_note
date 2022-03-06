package com.baidu.code_notes.aop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author lxh
 * @date 2022/3/6 18:04
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogDO {

    /** 消息 */
    @NotBlank
    private String message;

    /** 操作人姓名 */
    @NotNull
    private String operatorName;

    /** 操作人Id */
    @NotNull
    private Long operatorId;

    /** url */
    @NotNull
    private String url;

    /** 参数 */
    private String parameters;

    /** 结果 */
    private String result;

    /** 开始时间 */
    @NotNull
    private LocalDateTime startTime;

    /** 结束时间 */
    @NotNull
    private LocalDateTime endTime;

    /** 错误消息 */
    private String errorMessage;

}
