package com.baidu.code_notes.aop;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author lxh
 * @date 2022/3/6 19:44
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @Before
    public void before(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    public void getUserByIdTest() {
        String content = mockMvc.perform(MockMvcRequestBuilders.get("/userController/getUserById")
                                                                .param("id", "1")
                                                                .header("version", "1.0.1"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(content);
    }
}
