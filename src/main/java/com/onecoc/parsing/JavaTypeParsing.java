package com.onecoc.parsing;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.javadoc.PsiDocTokenImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.onecoc.model.EnumStructure;
import com.onecoc.model.Structure;
import com.onecoc.parsing.strategy.StrategyContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuany
 */
public class JavaTypeParsing implements TypeParsing {

    private static final List<String> BASIC_SYSTEM_TYPE = Lists.newArrayList(
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

    @Override
    public String convertToClockTypeName(PsiField psiField, Map<String, PsiTypeElement> tagToElement) {
        boolean isEnum = Optional.ofNullable(psiField)
                .map(PsiField::getType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::isEnum)
                .orElse(false);


        boolean isList = Optional.ofNullable(psiField)
                .map(this::isList)
                .orElse(false);

        String qualifiedNativeTypeName = this.extractFieldNativeQualifiedTypeName(psiField, tagToElement);

        if (isEnum) {
            return "enum";
        } else if (isList) {
            PsiType genericsType = this.parsingListGenericsPsiType(psiField);
            String genericsNativeQualifiedTypeName = this.getFieldTypeUsableName(genericsType);
            //维度
            int deepIndex = this.parsingListDeep(psiField);
            return String.format("array<%s>[%s]", isBasic(genericsType) ? this.convert(genericsNativeQualifiedTypeName) : "object", --deepIndex);
        } else {
            return this.convert(qualifiedNativeTypeName);
        }
    }


    private String convert(String nativeName) {

//        switch (nativeName) {
//            case "boolean":
//            case "java.lang.Boolean":
//                return "boolean";
//
//            case "long":
//            case "java.lang.Long":
//            case "double":
//            case "java.lang.Double":
//            case "int":
//            case "java.lang.Integer":
//            case "float":
//            case "java.lang.Float":
//            case "java.math.BigDecimal":
//                return "number";
//
//            case "char":
//            case "java.lang.Character":
//            case "java.lang.String":
//                return "string";
//
//            case "java.util.List":
//                return "array";
//
//            case "java.util.Map":
//                return "map";
//
//            case "java.util.Date":
//                return "moment";
//
//            default:
//                return "object";
//        }
        return null;

    }

    /**
     * @param javaDocumentedElement 文档节点
     * @return
     */
    @Override
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
     * @param targetField 目标字段
     * @return
     */
    @Override
    public String getFieldTypeLiteralName(PsiField targetField) {
        return Optional.ofNullable(targetField)
                .map(PsiField::getType)
                .map(this::getFieldTypeLiteralName)
                .orElse(null);
    }

    @Override
    public String getFieldTypeLiteralName(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(PsiType::getCanonicalText)
                .orElse(null);
    }

    @Override
    public String getFieldTypeQualifiedName(PsiField targetField) {
        return Optional.ofNullable(targetField)
                .map(PsiField::getType)
                .map(this::getFieldTypeQualifiedName)
                .orElse(null);
    }

    @Override
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

    @Override
    public String getFieldTypeUsableName(PsiField targetField) {

        String literalName = Optional
                .ofNullable(targetField)
                .map(this::getFieldTypeLiteralName)
                .orElse(null);

        return Optional
                .ofNullable(targetField)
                .map(this::getFieldTypeQualifiedName)
                .orElse(literalName);
    }

    /**
     * @param targetType 目标字段
     * @return
     */
    @Override
    public String getFieldTypeQualifiedName(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::getQualifiedName)
                .orElse(null);
    }

    @Override
    public String extractFieldNativeQualifiedTypeName(PsiField targetField, Map<String, PsiTypeElement> tagToElement) {
        String literalName = this.getFieldTypeLiteralName(targetField);
        String qualifiedName = this.getFieldTypeQualifiedName(targetField);
        if (Objects.isNull(qualifiedName)) {
            boolean isGenericsTag = Optional.ofNullable(tagToElement)
                    .map(Map::keySet)
                    .map(n -> n.contains(literalName))
                    .orElse(false);
            if (isGenericsTag) {
                PsiTypeElement element = tagToElement.get(literalName);
                PsiType elementType = Optional.ofNullable(element).map(PsiTypeElement::getType).orElse(null);
                return Optional.ofNullable(elementType).map(this::getFieldTypeUsableName).orElse(null);
            } else {
                return literalName;
            }
        }
        return qualifiedName;
    }

    @Override
    public PsiType parsingListGenericsPsiType(PsiField field) {
//        return Optional.ofNullable(field)
//                .map(PsiVariable::getTypeElement)
//                .map(element -> PsiTreeUtil.findChildrenOfAnyType(element, PsiJavaCodeReferenceElement.class))
//                .map(Lists::newArrayList)
//                .map(Lists::reverse)
//                .map(Collection::stream)
//                .map(n -> n.skip(1))
//                .flatMap(Stream::findFirst)
//                .map(PsiJavaCodeReferenceElement::getTypeParameters)
//                .map(Lists::newArrayList)
//                .map(Collection::stream)
//                .flatMap(Stream::findFirst)
//                .orElse(null);
        return null;
    }

    @Override
    public int parsingListDeep(PsiField field) {
        return Optional.ofNullable(field)
                .map(PsiVariable::getTypeElement)
                .map(element -> PsiTreeUtil.findChildrenOfAnyType(element, PsiJavaCodeReferenceElement.class))
                .map(Collection::size)
                .orElse(1);
    }

    @Override
    public boolean hasGenericTag(PsiType type) {
        return Optional.of(type)
                .map(PsiTypesUtil::getPsiClass)
                .map(this::hasGenericTag)
                .orElse(false);
    }

    @Override
    public boolean hasGenericTag(PsiClass psiClass) {
        return Optional.ofNullable(psiClass)
                .map(PsiTypeParameterListOwner::getTypeParameters)
                .map(Lists::newArrayList)
                .map(ArrayList::size)
                .orElse(0) > 0;
    }

    @Override
    public boolean hasGenericTag(PsiField psiField) {
        return Optional.ofNullable(psiField)
                .map(PsiField::getType)
                .map(this::hasGenericTag)
                .orElse(false);
    }

    @Override
    public boolean isEnum(PsiField psiField) {
        return Optional.ofNullable(psiField)
                .map(PsiField::getType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::isEnum)
                .orElse(false);
    }

    @Override
    public boolean isEnum(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::isEnum)
                .orElse(false);
    }

    @Override
    public boolean isEnum(PsiClass targetClass) {
        return Optional.ofNullable(targetClass)
                .map(PsiClass::isEnum)
                .orElse(false);
    }

    @Override
    public boolean isList(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::getFieldTypeUsableName)
                .map(LIST_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    @Override
    public boolean isList(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(this::getFieldTypeUsableName)
                .map(LIST_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    @Override
    public boolean isMap(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::getFieldTypeUsableName)
                .map(MAP_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    @Override
    public boolean isBasic(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::getFieldTypeUsableName)
                .map(BASIC_SYSTEM_TYPE::contains)
                .orElse(false);
    }

    @Override
    public boolean isBasic(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(this::getFieldTypeUsableName)
                .map(BASIC_SYSTEM_TYPE::contains)
                .orElse(false);
    }

    @Override
    public List<PsiTypeElement> extractGenericPsiTypeElement(PsiTypeElement element) {
        return Optional.ofNullable(element)
                .map(n -> PsiTreeUtil.findChildrenOfAnyType(element, PsiTypeElement.class))
                .map(Lists::newArrayList)
                .orElse(Lists.newArrayList());
    }

    @Override
    public List<String> extractGenericTagText(PsiClass psiClass) {
        return Optional.ofNullable(psiClass)
                .map(PsiTypeParameterListOwner::getTypeParameters)
                .map(Lists::newArrayList)
                .map(Collection::stream)
                .map(
                        n -> n.map(PsiElement::getText).collect(Collectors.toList())
                )
                .orElse(Lists.newArrayList());
    }

    @Override
    public List<EnumStructure> parsingEnumStructure(PsiClass psiClass) {
        return Optional.ofNullable(psiClass)
                .map(PsiClass::getFields)
                .map(Lists::newArrayList)
                .orElse(Lists.newArrayList())
                .stream()
                .filter(f -> f instanceof PsiEnumConstant)
                .map(
                        enumConstant ->
                                EnumStructure
                                        .builder()
                                        .value(enumConstant.getName())
                                        .description(this.getDocDescription(enumConstant))
                                        .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<Structure> parsing(PsiClass psiClass, Collection<PsiTypeElement> generic) {

        Map<String, PsiTypeElement> tagToElement = Streams.zip(
                this.extractGenericTagText(psiClass).stream(),
                generic.stream(),
                (tag, element) -> GenericMapStructure.builder().key(tag).value(element).build()
        ).collect(Collectors.toMap(GenericMapStructure::getKey, GenericMapStructure::getValue));

        StrategyContext strategyContext = new StrategyContext();

        return Optional.of(psiClass)
                .map(PsiClass::getFields)
                .map(Lists::newArrayList)
                .orElse(Lists.newArrayList())
                .stream()
                .filter(field -> !IGNORE_FIELD_NAME.contains(field.getName()))
                .filter(field -> !IGNORE_FIELD_QUALIFIED_NAME.contains(field.getType().getCanonicalText()))
                .map(field -> {
                    System.out.println("dd");
                    return strategyContext.execute(field, tagToElement);
                })
                .collect(Collectors.toList());


    }

}
