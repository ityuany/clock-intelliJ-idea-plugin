package com.onecoc.parsing.strategy;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.javadoc.PsiDocTokenImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.onecoc.parsing.JavaTypeParsing;
import com.onecoc.parsing.TypeParsing;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yuany
 */
public abstract class ParsingStrategy {

    protected TypeParsing typeParsing = new JavaTypeParsing();

    public static final List<String> BASIC_SYSTEM_TYPE = Lists.newArrayList(
            "boolean",
            "java.lang.Boolean",
            "long",
            "java.lang.Long",
            "double",
            "java.lang.Double",
            "int",
            "java.lang.Integer",
            "float",
            "java.lang.Float",
            "char",
            "java.lang.Character",
            "java.math.BigDecimal",
            "java.lang.String",
            "java.util.Date"
    );

    private static final List<String> LIST_QUALIFIED_NAME = Lists.newArrayList(
            "java.util.List",
            "java.util.ArrayList"
    );

    private static final List<String> MAP_QUALIFIED_NAME = Lists.newArrayList(
            "java.util.Map"
    );

    private static final List<String> IGNORE_FIELD_NAME = Lists.newArrayList(
            "serialVersionUID"
    );

    private static final List<String> IGNORE_FIELD_QUALIFIED_NAME = Lists.newArrayList(
            "org.bson.types.ObjectId"
    );

    private static final List<String> JSR303_REQUIRED_ANNOTATION = Lists.newArrayList(
            "javax.validation.constraints.NotNull",
            "javax.validation.constraints.NotEmpty"
    );

    /**
     * 获取字段的完整名称
     *
     * @param field 目标节点
     * @return 字段类型的完整名称
     */
    public String getFieldTypeQualifiedName(PsiField field) {
        return Optional.ofNullable(field)
                .map(PsiField::getType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::getQualifiedName)
                .orElse(null);
    }

    /**
     * 获取字段的完整名称
     *
     * @param targetType 目标节点
     * @return 字段类型的完整名称
     */
    public String getFieldTypeQualifiedName(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::getQualifiedName)
                .orElse(null);
    }

    /**
     * 获取字面类型名称
     *
     * @param field 目标节点
     * @return 字面类型名称
     */
    public String getFieldTypeLiteralName(PsiField field) {
        return Optional.ofNullable(field)
                .map(PsiField::getType)
                .map(PsiType::getCanonicalText)
                .orElse(null);
    }

    /**
     * 获取字面类型名称
     *
     * @param targetType 目标节点
     * @return 字面类型名称
     */
    public String getFieldTypeLiteralName(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(PsiType::getCanonicalText)
                .orElse(null);
    }

    /**
     * 获取可用名称
     *
     * @param field 目标节点
     * @return 可用名称
     */
    public String getFieldTypeUsableName(PsiField field) {
        String literalName = Optional
                .ofNullable(field)
                .map(this::getFieldTypeLiteralName)
                .orElse(null);
        return Optional
                .ofNullable(field)
                .map(this::getFieldTypeQualifiedName)
                .orElse(literalName);
    }

    /**
     * 获取可用名称
     *
     * @param targetType 目标节点类型
     * @return 可用名称
     */
    public String getFieldTypeUsableName(PsiType targetType) {
        String literalName = Optional
                .ofNullable(targetType)
                .map(this::getFieldTypeLiteralName)
                .orElse(null);
        return Optional
                .ofNullable(targetType)
                .map(this::getFieldTypeQualifiedName)
                .orElse(literalName);
    }

    /**
     * 是否是集合类型
     *
     * @param field 目标字段
     * @return 是否是集合
     */
    public boolean isList(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::getFieldTypeUsableName)
                .map(LIST_QUALIFIED_NAME::contains)
                .orElse(false);
    }


    /**
     * 是否是枚举
     *
     * @param targetType 目标字段
     * @return 布尔
     */
    public boolean isEnum(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::isEnum)
                .orElse(false);
    }

    /**
     * 是否是枚举
     *
     * @param targetClass 目标字段
     * @return 布尔
     */
    public boolean isEnum(PsiClass targetClass) {
        return Optional.ofNullable(targetClass)
                .map(PsiClass::isEnum)
                .orElse(false);
    }

    /**
     * 是否是集合类型
     *
     * @param targetType 目标字段
     * @return 是否是集合
     */
    public boolean isList(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(this::getFieldTypeUsableName)
                .map(LIST_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    /**
     * 是否是系统的基础数据类型
     *
     * @param field 目标节点
     * @return 是否为基础数据类型
     */
    public boolean isBasic(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::getFieldTypeUsableName)
                .map(BASIC_SYSTEM_TYPE::contains)
                .orElse(false);
    }

    /**
     * 是否是系统的基础数据类型
     *
     * @param targetType 目标节点
     * @return 是否为基础数据类型
     */
    public boolean isBasic(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(this::getFieldTypeUsableName)
                .map(BASIC_SYSTEM_TYPE::contains)
                .orElse(false);
    }

    /**
     * 字段是否必传
     *
     * @param field 目标字段
     * @return 是否必传
     */
    public boolean getRequired(PsiField field) {
        return Optional.of(field)
                .map(PsiModifierListOwner::getAnnotations)
                .map(Lists::newArrayList)
                .orElse(Lists.newArrayList())
                .stream()
                .map(PsiAnnotation::getQualifiedName)
                .anyMatch(JSR303_REQUIRED_ANNOTATION::contains);
    }

    /**
     * 获取doc注释
     *
     * @param javaDocumentedElement doc节点
     * @return 注释信息
     */
    public String getDocDescription(PsiJavaDocumentedElement javaDocumentedElement) {
        return Optional.ofNullable(javaDocumentedElement)
                .map(PsiJavaDocumentedElement::getDocComment)
                .map(PsiDocComment::getDescriptionElements)
                .map(Lists::newArrayList)
                .orElse(Lists.newArrayList())
                .stream()
                .filter(n -> n instanceof PsiDocTokenImpl)
                .map(PsiElement::getText)
                .map(String::trim)
                .collect(Collectors.joining());
    }

    /**
     * 获取语句快的泛型信息
     * @param element 目标节点
     * @return 泛型集合
     */
    public List<PsiTypeElement> getGenericPsiTypeElement(PsiTypeElement element) {
        return Optional.ofNullable(element)
                .map(n -> PsiTreeUtil.findChildrenOfAnyType(element, PsiTypeElement.class))
                .map(Lists::newArrayList)
                .orElse(Lists.newArrayList());
    }

    /**
     * 提取出集合的泛型最终类型，这里主要是为多维集合做工作
     *
     * @param field 目标字段
     * @return 具体的类型
     */
    public PsiType getListGenericsPsiType(PsiField field) {
        return Optional.ofNullable(field)
                .map(PsiVariable::getTypeElement)
                .map(element -> PsiTreeUtil.findChildrenOfAnyType(element, PsiJavaCodeReferenceElement.class))
                .map(Lists::newArrayList)
                .map(Lists::reverse)
                .map(Collection::stream)
                .map(n -> n.skip(1))
                .flatMap(Stream::findFirst)
                .map(PsiJavaCodeReferenceElement::getTypeParameters)
                .map(Lists::newArrayList)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .orElse(null);
    }

    /**
     * 解析出list嵌套的层数
     *
     * @param field 目标字段
     * @return 嵌套层数
     */
    public int getListDeep(PsiField field) {
        return Optional.ofNullable(field)
                .map(PsiVariable::getTypeElement)
                .map(element -> PsiTreeUtil.findChildrenOfAnyType(element, PsiJavaCodeReferenceElement.class))
                .map(Collection::size)
                .map(n -> --n)
                .orElse(1);
    }

    /**
     * 泛型的描述是否为tag
     *
     * @param field        目标字段
     * @param tagToElement 映射表
     * @return 布尔
     */
    public boolean isGenericsTagForList(PsiField field, Map<String, PsiTypeElement> tagToElement) {
        return Optional.ofNullable(this.getListGenericsPsiType(field))
                .map(this::getFieldTypeUsableName)
                .map(tagToElement::containsKey)
                .orElse(false);
    }

    /**
     * 泛型最终的类型是否是基础数据类型
     *
     * @param field        目标字段
     * @param tagToElement 映射表
     * @return 布尔
     */
    public boolean isBasicForGenericsTagForList(PsiField field, Map<String, PsiTypeElement> tagToElement) {
        return this.isBasic(
                Optional.ofNullable(field)
                        .map(this::getListGenericsPsiType)
                        .map(this::getFieldTypeUsableName)
                        .map(tagToElement::get)
                        .map(PsiTypeElement::getType)
                        .orElse(null)
        );
    }

    /**
     * 是否是Map类型
     *
     * @param field 目标字段
     * @return 是否是Map
     */
    public boolean isMap(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::getFieldTypeUsableName)
                .map(MAP_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    /**
     * 是否是Map类型
     *
     * @param type 目标字段类型
     * @return 是否是Map
     */
    public boolean isMap(PsiType type) {
        return Optional.ofNullable(type)
                .map(this::getFieldTypeUsableName)
                .map(MAP_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    public String toClockTypeName(String nativeTypeName) {
        switch (nativeTypeName) {
            case "boolean":
            case "java.lang.Boolean":
                return "boolean";

            case "long":
            case "java.lang.Long":
            case "double":
            case "java.lang.Double":
            case "int":
            case "java.lang.Integer":
            case "float":
            case "java.lang.Float":
            case "java.math.BigDecimal":
                return "number";

            case "char":
            case "java.lang.Character":
            case "java.lang.String":
                return "string";

            case "java.util.List":
                return "array";

            case "java.util.Map":
                return "map";

            case "java.util.Date":
                return "moment";

            default:
                return "object";
        }
    }

}
