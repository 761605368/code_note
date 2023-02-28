package com.baidu.code_notes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.code_notes.utils.JavaJsoupUtil;
import com.baidu.code_notes.utils.MapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
