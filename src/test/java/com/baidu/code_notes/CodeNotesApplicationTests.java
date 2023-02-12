package com.baidu.code_notes;

import com.alibaba.fastjson.JSONObject;
import com.baidu.code_notes.utils.JavaJsoupUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CodeNotesApplicationTests {

    @Test
    void contextLoads() {
        JavaJsoupUtil util = new JavaJsoupUtil();
        List<JavaJsoupUtil.SysCitys> sysAreas = util.getProvinces();
        System.out.println(sysAreas.size());
        System.err.println("爬虫相应数据为："+ JSONObject.toJSONString(sysAreas));
    }

}
