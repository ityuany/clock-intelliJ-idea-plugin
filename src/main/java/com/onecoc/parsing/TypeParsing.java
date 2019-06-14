package com.onecoc.parsing;

import com.intellij.psi.*;

import java.util.List;
import java.util.Map;

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
    boolean hasGenericTag(PsiType type);

    /**
     * 是否是泛型
     *
     * @param psiClass psi类
     * @return 布尔
     */
    boolean hasGenericTag(PsiClass psiClass);

    /**
     * 是否是泛型
     *
     * @param psiField psi类
     * @return 布尔
     */
    boolean hasGenericTag(PsiField psiField);

    /**
     * 是否是枚举
     * @param psiField 目标字段
     * @return 布尔
     */
    boolean isEnum(PsiField psiField);

    /**
     * 是否是集合类型
     *
     * @param field 目标字段
     * @return 是否是集合
     */
    boolean isList(PsiField field);

    /**
     * 是否是集合类型
     *
     * @param targetType 目标字段
     * @return 是否是集合
     */
    boolean isList(PsiType targetType);

    /**
     * 是否是Map类型
     *
     * @param field 目标字段
     * @return 是否是Map
     */
    boolean isMap(PsiField field);

    /**
     * 是否是基础数据类型
     *
     * @param field
     * @return
     */
    boolean isNormal(PsiField field);

    /**
     * 是否是基础数据类型
     *
     * @param targetType
     * @return
     */
    boolean isNormal(PsiType targetType);

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
    List<String> extractGenericTagText(PsiClass psiClass);

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
     * @param psiField 语言本地完整的名称
     * @return clock类型系统的名称
     */
    String convertToClockTypeName(PsiField psiField,Map<String, PsiTypeElement> tagToElement);

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
    String extractFieldNativeQualifiedTypeName(PsiField targetField);

    /**
     * 解析出本地完整合法的类型名称
     *
     * @param targetType 类型
     * @return 本地合法的类型名称
     */
    String extractFieldNativeQualifiedTypeName(PsiType targetType);

    /**
     * 解析出本地完整合法的类型名称
     * @param targetType 类型
     * @param tagToElement 泛型集合
     * @return 合法的类型名称
     */
    String extractFieldNativeQualifiedTypeName(PsiType targetType, Map<String, PsiTypeElement> tagToElement);

    /**
     * 解析出本地完整合法的类型名称
     * @param targetField 类型
     * @param tagToElement 泛型集合
     * @return 合法的类型名称
     */
    String extractFieldNativeQualifiedTypeName(PsiField targetField, Map<String, PsiTypeElement> tagToElement);

    /**
     * 解析出集合的泛型类型
     *
     * @param field
     * @return
     */
    PsiType parsingListGenericsPsiType(PsiField field);

    /**
     * 解析出list嵌套的层数
     *
     * @param field 目标字段
     * @return 嵌套层数
     */
    int parsingListDeep(PsiField field);

    List<Structure> parsing(PsiClass psiClass, List<PsiTypeElement> generic);


}

