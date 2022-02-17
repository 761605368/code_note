package com.baidu.code_notes.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author lxh
 * @date 2022/2/17 16:09
 * @Desc 关于递归的小工具
 * @Desc treeUtil
 */
public class TreeUtil {

    /**
     * 通用递归组装
     * @param list 原数据
     * @param assemblyList 组装后的集合
     * @param parentEntity 父类
     * @param isChildren 判断当前循环的元素 是否是 当前父类的子类
     * @param addChildren 如何加入到当前父类的子类集合中去
     * @param isEnd 判断当前节点是否是 最终的子节点
     * @param <T> 需要处理的元素类型
     * @return
     */
    public <T> List recursiveAssembly(List<T> list, List<T> assemblyList, T parentEntity, BiPredicate<T,T> isChildren, BiConsumer<T,List<T>> addChildren, Predicate<T> isEnd) {
        for (T t : list) {
            //判断是不是当前父类的子类
            if (isChildren.test(parentEntity, t)) {
                //将子类添加到当前级别的集合中
                assemblyList.add(t);
                //是否是最终的子节点
                if (isEnd == null || !isEnd.test(t)) {
                    //创建子节点的集合
                    List<T> childrenList = new ArrayList<>();
                    //将子节点集合添加到当前对象中
                    addChildren.accept(t, childrenList);
                    //递归调用
                    this.recursiveAssembly(list, childrenList, t, isChildren, addChildren, isEnd);
                }
            }
        }
        return assemblyList;
    }

}
