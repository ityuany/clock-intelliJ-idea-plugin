package com.onecoc.parsing;

import com.intellij.psi.*;

import java.util.List;

/**
 * @author yuany
 */
public interface TypeParsing {

    /**
     * 是否是泛型
     *
     * @param type psi的类型
     * @return 布尔
     */
    boolean isGeneric(PsiType type);

    /**
     * 是否是泛型
     *
     * @param psiClass psi类
     * @return 布尔
     */
    boolean isGeneric(PsiClass psiClass);

    /**
     * 提取泛型的类型
     *
     * @param element 目标类型
     * @return 类型元素
     */
    List<PsiTypeElement> extractGenericPsiTypeElement(PsiTypeElement element);


    /**
     * 提取泛型的描述占位符
     *
     * @param psiClass psi类信息
     * @return 描述信息
     */
    List<String> extractGenericTextDescription(PsiClass psiClass);

    /**
     * 是否属于系统类型
     *
     * @param psiType psi类型
     * @return 是否属于系统类型
     */
    boolean belongBasicSystemType(PsiType psiType);

    /**
     * 解析枚举类型的结构
     *
     * @param psiClass 枚举类
     * @return 枚举的结构
     */
    List<EnumStructure> parsingEnumStructure(PsiClass psiClass);

    /**
     * 转换类型的名称
     *
     * @param qualifiedNativeName 语言本地完整的名称
     * @return clock类型系统的名称
     */
    String convertQualifiedNativeTypeNameToClockTypeName(String qualifiedNativeName);

    /**
     * 解析注释信息的具体描述
     *
     * @param javaDocumentedElement 文档节点
     * @return 注释信息的描述
     */
    String parsingDocDescription(PsiJavaDocumentedElement javaDocumentedElement);

    /**
     * 解析出本地完整合法的类型名称
     *
     * @param targetField 类型
     * @return 本地合法的类型名称
     */
    String parsingPsiFieldQualifiedNativeTypeName(PsiField targetField);

    List<Structure> parsing(PsiClass psiClass, List<PsiTypeElement> generic);


}

