package com.baidu.code_notes.controller;

import com.baidu.code_notes.aop.IgnoreSystemLog;
import com.baidu.code_notes.aop.SystemLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lxh
 * @date 2022/3/6 19:45
 */
@RestController
@RequestMapping("/userController")
public class UserController {

    @SystemLog("测试")
    @RequestMapping("/getUserById")
    public String getUserById(Long id) {
        return "li";
    }
}
