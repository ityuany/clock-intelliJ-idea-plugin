package com.onecoc.parsing;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeElement;

import java.util.List;

/**
 * @author yuany
 */
public interface MethodParsing {


    String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";

    String GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";

    String DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";

    String PATCH_MAPPING = "org.springframework.web.bind.annotation.PatchMapping";

    String POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";

    String PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";

    List<String> routeMappingAnnotations = Lists.newArrayList(
            REQUEST_MAPPING, GET_MAPPING, DELETE_MAPPING,
            PATCH_MAPPING, POST_MAPPING, PUT_MAPPING
    );

    /**
     * 是否是一个标准的http接口
     *
     * @param selectedMethod 选择的函数
     * @return 布尔类型
     */
    boolean isQualifiedHttpInterface(PsiMethod selectedMethod);

    /**
     * 是否存在路径映射的注解
     *
     * @param annotations 一组注释的数组
     * @return 布尔类型
     */
    boolean hasRouteMappingAnnotations(List<PsiAnnotation> annotations);

    /**
     * 查询接口函数的请求路径
     *
     * @param selectedMethod 目标函数
     * @return 函数路径
     */
    String getRoutePath(PsiMethod selectedMethod);

    /**
     * 获取请求方法
     *
     * @param selectedMethod 目标函数
     * @return 请求方法
     */
    String getRequestMethod(PsiMethod selectedMethod);

    /**
     * 获取函数的描述信息 ， 默认规则优先取注释内容，没有注释的情况下取函数的名称
     * @param selectedMethod    目标函数
     * @return  描述信息
     */
    String getMethodDescription(PsiMethod selectedMethod);

    /**
     * 获取函数返回值的结构
     * @param selectedMethod 目标函数
     * @return 结构
     */
    List<PsiTypeElement> getMethodReturnGenericStructure(PsiMethod selectedMethod);

    /**
     * 判断函数的返回值是不是泛型结构
     * @param selectedMethod 目标函数
     * @return 是不是泛型
     */
    boolean isGenericForReturnType(PsiMethod selectedMethod);

}
