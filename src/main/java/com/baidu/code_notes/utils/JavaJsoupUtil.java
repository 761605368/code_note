package com.baidu.code_notes.utils;


import lombok.Data;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JavaJsoupUtil
 * @Description TODO
 * @Author Lock-玄清
 * @Date 2022/9/15 10:59
 **/
public class JavaJsoupUtil {

    /**
     * 公共路径url
     */
    private static String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2022/";

    /**
     * 建立连接
     */
    private static Document connect(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("无效的url");
        }
        try {
            return Jsoup.connect(url).timeout(200 * 2000).get();
        } catch (IOException e) {
            System.out.println(url+"地址不存在");
            return null;
        }
    }

    /**
     * 获取所有的省份    Lock-玄清
     * @param
     * @return
     */
    public List<SysCitys> getProvinces() {
        List<SysCitys> sysAreas = new ArrayList<>();
        Document connect = connect(url+"index.html");
        Elements rowProvince = connect.select("tr.provincetr");
        for (Element provinceElement : rowProvince) {
            Elements select = provinceElement.select("a");
            for (Element province : select) {
                String codUrl = province.select("a").attr("href");
                String fatherCode = codUrl.replace(".html", "0000");
                String name = province.text();
                SysCitys sysCitys = returnCitys(fatherCode, name, "0", "1", name, "0");
                sysAreas.add(sysCitys);
                System.err.println("++++++++++++++++++++++++++开始获取" + name + "下属市区行政区划信息++++++++++++++++++++++++");
                String provinceUrl = url + codUrl;
                List<SysCitys> sysAreasList = getCitys(provinceUrl, fatherCode, name, "0" + "," + fatherCode);
                sysAreas.addAll(sysAreasList);

            }
        }
        return sysAreas;
    }


    /**
     * 获取市行政区划信息    Lock-玄清
     * @param provinceUrl 省份对应的地址
     * @param parentCode  需要爬取的省份行政区划（对于市的父级代码即为省行政区划）
     * @return
     */
    public List<SysCitys> getCitys(String provinceUrl,String parentCode, String provinceName, String pids){
        List<SysCitys> sysAreas = new ArrayList<>();
        Document connect = connect(provinceUrl);
        Elements rowCity = connect.select("tr.citytr");
        for (Element cityElement : rowCity) {
            String codUrl = cityElement.select("a").attr("href");
            String name = cityElement.select("td").text();
            String[] split = name.split(" ");
            String addrCode = split[0].substring(0,4);
            SysCitys sysCitys = returnCitys(addrCode+"00",split[1],parentCode,"2", provinceName+split[1],pids);
            sysAreas.add(sysCitys);
            System.err.println("-------------------开始获取"+split[1]+"下属区县行政区划信息-----------------------");
            String cityUrl =  url+codUrl;
            List<SysCitys> downAreaCodeList = getCountys(cityUrl,addrCode+"00", provinceName+split[1], pids + "," + addrCode + "00");
            sysAreas.addAll(downAreaCodeList);

        }
        return sysAreas;
    }

    /**
     * 获取区县行政区划信息    Lock-玄清
     * @param cityUrl 城市对应的地址
     * @param parentCode  需要爬取的市行政区划（对于区县的父级代码即为市行政区划）
     * @return
     */
    public List<SysCitys> getCountys(String cityUrl,String parentCode, String cityFullName, String pids){
        List<SysCitys> sysAreas = new ArrayList<>();
        Document connect = connect(cityUrl);
        Elements rowDown = connect.select("tr.countytr");
        for (Element downElement : rowDown) {
            String codUrl = downElement.select("a").attr("href");
            String name = downElement.select("td").text();
            String[] split = name.split(" ");
            if(!"市辖区".equals(split[1])){
                SysCitys sysCitys = returnCitys(split[0].substring(0,6),split[1],parentCode,"3", cityFullName+split[1], pids);
                sysAreas.add(sysCitys);
            }
        }
        return sysAreas;
    }

    /**
     * 返回城市对象  Lock-玄清
     * @param addrCode
     * @param addrName
     * @param fatherCode
     * @return
     */
    private SysCitys returnCitys(String addrCode,String addrName,String fatherCode,String type, String fullName, String pids){
        SysCitys sysCitys = new SysCitys();
        sysCitys.setId(addrCode);
        sysCitys.setName(addrName);
        sysCitys.setPid(fatherCode);
        sysCitys.setPids(pids);
        sysCitys.setLevel(type);
        sysCitys.setPinyin(JavaJsoupUtil.toPinyin(addrName));
        sysCitys.setFullName(fullName);



        return sysCitys;
    }

    /**
     * 汉字转为拼音
     * @param chinese
     * @return
     */
    public static String toPinyin(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }


    /**
     * @author lxh
     * @date 2023/2/11 20:20
     */
    @Data
    public static class SysCitys {

        private String id;
        private String name;
        private String pid;
        private String pids;
        private String level;
        private String fullName;
        private String pinyin;
    }

}





