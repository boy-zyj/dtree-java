package com.clubfactory.dtree;

/**
 * 从INPUT中获得OUTPUT类型的值
 *
 * @author yao
 * @date 2019/03/19
 */
@FunctionalInterface
public interface Getter<INPUT, OUTPUT> {

    /**
     * 从INPUT中获得OUTPUT类型的值
     *
     * @param input INPUT类型
     * @return OUTPUT
     */
    OUTPUT get(INPUT input);

}
