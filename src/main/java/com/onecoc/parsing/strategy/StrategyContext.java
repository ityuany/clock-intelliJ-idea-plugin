package com.onecoc.parsing.strategy;

import com.google.common.collect.Maps;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.onecoc.model.Structure;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class StrategyContext {

    private static final Map<BiPredicate<PsiField, Map<String, PsiTypeElement>>, BiFunction<PsiField, Map<String, PsiTypeElement>, Structure>> PARSING_MACHINE = Maps.newHashMap();

    static {
        /**
         * 基础数据类型的解析方案
         */
        PARSING_MACHINE.put(new BasicBiPredicate(), new BasicBiFunction());

        /**
         * 枚举类型
         */
        PARSING_MACHINE.put(new EnumBiPredicate(), new EnumBiFunction());
        /**
         * 基础数据类型的List 解析方案
         */
        PARSING_MACHINE.put(new BasicListBiPredicate(), new BasicListBiFunction());
        /**
         * 自定义Object List 解析方案
         */
        PARSING_MACHINE.put(new ObjectListBiPredicate(), new ObjectListBiFunction());

        /**
         * 泛型映射出来的是基础数据类型
         */
        PARSING_MACHINE.put(new GenericsBasicListBiPredicate(), new GenericsBasicListBiFunction());

        /**
         * 泛型映射出来的是非基础数据类型
         */
        PARSING_MACHINE.put(new GenericsListBiPredicate(), new GenericsListBiFunction());

        PARSING_MACHINE.put(new GenericsObjectBiPredicate(), new GenericsObjectBiFunction());
    }

    public Structure execute(PsiField field, Map<String, PsiTypeElement> tagToElement) {
        return PARSING_MACHINE
                .entrySet()
                .stream()
                .filter(n -> n.getKey().test(field, tagToElement))
                .findFirst()
                .map(n -> n.getValue().apply(field, tagToElement))
                .orElse(null);
    }

}
