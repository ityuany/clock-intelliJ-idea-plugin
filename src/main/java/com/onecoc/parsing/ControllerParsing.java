package com.onecoc.parsing;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;

import java.util.List;

/**
 * @author yuany
 */
public interface ControllerParsing {

    List<String> controllerAnnotations = Lists.newArrayList(
            "org.springframework.web.bind.annotation.RestController",
            "org.springframework.stereotype.Controller"
    );

    /**
     * 是否是标准的controller
     *
     * @param selectedClass 需要进行判断的controller 的psiClass
     * @return 布尔类型，表示该controller是否标准
     */
    boolean isQualifiedController(PsiClass selectedClass);

    /**
     * 是否存在controller注解
     *
     * @param annotations 一组注释的数组
     * @return 布尔类型，表示该controller是否标准
     */
    boolean hasControllerAnnotation(List<PsiAnnotation> annotations);

    /**
     * 查询controller的 http 请求路径
     * @param selectedClass 目标class
     * @return 路径
     */
    String getControllerHttpPath(PsiClass selectedClass);

}
