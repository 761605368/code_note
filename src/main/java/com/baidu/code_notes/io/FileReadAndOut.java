package com.baidu.code_notes.io;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import lombok.SneakyThrows;

import java.io.*;

/**
 * @author lxh
 * @date 2022/2/17 15:52
 * @desc 文件的读写
 */
public class FileReadAndOut {

    /**
     * text文本读写
     */
    @SneakyThrows
    public void textReadAndOut() {

        //创建文本bufferedReader对象，以便逐行读取
        File file = new File("D:\\temp\\历史强采数据.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        //使用builder拼接字符串
        StringBuilder str = new StringBuilder("");
        String temp = "";

        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);

        //创建printWriter，以便逐行写入
        FileWriter fileWriter = new FileWriter("D:\\temp\\历史强采数据new.txt", true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        while ((temp = bufferedReader.readLine()) != null ) {
            str.append(temp);
            str.append("\r\n");//Windows换行符
        }

        DateTime startDate = DateUtil.parse("2021-07-01", "yyyy-MM-dd");
        DateTime endDate = DateUtil.parse("2022-02-18", "yyyy-MM-dd");
        //计算两个日期的天的差数
        long betweenDay = DateUtil.betweenDay(startDate, endDate, true);
        for (long i = 0; i <= betweenDay; i++) {
            DateTime offsetDay = DateUtil.offsetDay(startDate, (int) i);
            String offsetDayStr = DateUtil.format(offsetDay, "yyyy-MM-dd");
            String result = str.toString().replace("?", offsetDayStr);
            printWriter.println(result);
        }

        IoUtil.flush(printWriter);
        IoUtil.flush(fileWriter);
        IoUtil.close(printWriter);
        IoUtil.close(fileWriter);
    }
}
