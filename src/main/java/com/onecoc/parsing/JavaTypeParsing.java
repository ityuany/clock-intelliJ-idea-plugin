package com.onecoc.parsing;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.javadoc.PsiDocTokenImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yuany
 */
public class JavaTypeParsing implements TypeParsing {

    private static final List<String> BASIC_SYSTEM_TYPE = Lists.newArrayList(
            "boolean", "java.lang.Boolean",
            "long", "java.lang.Long",
            "double", "java.lang.Double",
            "int", "java.lang.Integer",
            "float", "java.lang.Float",
            "char", "java.lang.Character",
            "java.math.BigDecimal", "java.lang.String",
            "java.util.Date"
    );

    private static final List<String> LIST_QUALIFIED_NAME = Lists.newArrayList(
            "java.util.List", "java.util.ArrayList"
    );

    private static final List<String> MAP_QUALIFIED_NAME = Lists.newArrayList(
            "java.util.Map"
    );

    private static final List<String> IGNORE_FIELD_NAME = Lists.newArrayList("serialVersionUID");

    private static final List<String> IGNORE_FIELD_QUALIFIED_NAME = Lists.newArrayList("org.bson.types.ObjectId");

    private static final List<String> JSR303_REQUIRED_ANNOTATION = Lists.newArrayList(
            "javax.validation.constraints.NotNull", "javax.validation.constraints.NotEmpty"
    );

    @Override
    public String convertToClockTypeName(PsiField psiField) {
        boolean isEnum = Optional.ofNullable(psiField)
                .map(PsiField::getType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::isEnum)
                .orElse(false);


        boolean isList = Optional.ofNullable(psiField)
                .map(this::isList)
                .orElse(false);

        if (isEnum) {
            return "enum";
        } else if (isList) {
            PsiType genericsType = this.parsingListGenericsPsiType(psiField);
            String genericsNativeQualifiedTypeName = this.extractFieldNativeQualifiedTypeName(genericsType);
            //维度
            int deepIndex = this.parsingListDeep(psiField);
            return String.format("array<%s>[%s]", isNormal(genericsType) ? this.convert(genericsNativeQualifiedTypeName) : "object", --deepIndex);
        } else {
            String nativeName = this.extractFieldNativeQualifiedTypeName(psiField);
            return this.convert(nativeName);
        }
    }


    private String convert(String nativeName) {

        switch (nativeName) {
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

    @Override
    public String parsingDocDescription(PsiJavaDocumentedElement javaDocumentedElement) {
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

    @Override
    public String extractFieldNativeQualifiedTypeName(PsiField targetField) {
        return Optional.ofNullable(targetField)
                .map(PsiField::getType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::getQualifiedName)
                .orElse(
                        Optional.ofNullable(targetField)
                                .map(PsiField::getType)
                                .map(PsiType::getCanonicalText)
                                .orElse(null)
                );
    }

    @Override
    public String extractFieldNativeQualifiedTypeName(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiClass::getQualifiedName)
                .orElse(
                        Optional.ofNullable(targetType)
                                .map(PsiType::getCanonicalText)
                                .orElse(null)
                );
    }

    @Override
    public PsiType parsingListGenericsPsiType(PsiField field) {
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

    @Override
    public int parsingListDeep(PsiField field) {
        return Optional.ofNullable(field)
                .map(PsiVariable::getTypeElement)
                .map(element -> PsiTreeUtil.findChildrenOfAnyType(element, PsiJavaCodeReferenceElement.class))
                .map(Collection::size)
                .orElse(1);
    }

    @Override
    public boolean isGeneric(PsiType type) {
        return Optional.of(type)
                .map(PsiTypesUtil::getPsiClass)
                .map(this::isGeneric)
                .orElse(false);
    }

    @Override
    public boolean isGeneric(PsiClass psiClass) {
        return Optional.ofNullable(psiClass)
                .map(PsiTypeParameterListOwner::getTypeParameters)
                .map(Lists::newArrayList)
                .map(ArrayList::size)
                .orElse(0) > 0;
    }

    @Override
    public boolean isGeneric(PsiField psiField) {
        return Optional.ofNullable(psiField)
                .map(PsiField::getType)
                .map(this::isGeneric)
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
    public boolean isList(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::extractFieldNativeQualifiedTypeName)
                .map(LIST_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    @Override
    public boolean isList(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(this::extractFieldNativeQualifiedTypeName)
                .map(LIST_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    @Override
    public boolean isMap(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::extractFieldNativeQualifiedTypeName)
                .map(MAP_QUALIFIED_NAME::contains)
                .orElse(false);
    }

    @Override
    public boolean isNormal(PsiField field) {
        return Optional.ofNullable(field)
                .map(this::extractFieldNativeQualifiedTypeName)
                .map(BASIC_SYSTEM_TYPE::contains)
                .orElse(false);
    }

    @Override
    public boolean isNormal(PsiType targetType) {
        return Optional.ofNullable(targetType)
                .map(this::extractFieldNativeQualifiedTypeName)
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
    public boolean belongBasicSystemType(PsiType psiType) {
        return Optional.ofNullable(psiType)
                .map(PsiType::getCanonicalText)
                .map(BASIC_SYSTEM_TYPE::contains)
                .orElse(false);
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
                                        .description(this.parsingDocDescription(enumConstant))
                                        .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<Structure> parsing(PsiClass psiClass, List<PsiTypeElement> generic) {

        Map<String, PsiTypeElement> tagToElement = Streams.zip(
                this.extractGenericTagText(psiClass).stream(),
                generic.stream(),
                (tag, element) -> GenericMapStructure.builder().key(tag).value(element).build()
        ).collect(Collectors.toMap(GenericMapStructure::getKey, GenericMapStructure::getValue));

        return Optional.of(psiClass)
                .map(PsiClass::getFields)
                .map(Lists::newArrayList)
                .orElse(Lists.newArrayList())
                .stream()
                .filter(field -> !IGNORE_FIELD_NAME.contains(field.getName()))
                .filter(field -> !IGNORE_FIELD_QUALIFIED_NAME.contains(field.getType().getCanonicalText()))
                .map(field -> {

                    //拿到字段的类信息，可能是null， 基础数组类型的话  就是null
                    PsiClass fieldPsiClass = PsiTypesUtil.getPsiClass(field.getType());

                    // 拿到字段的名称
                    String fieldName = field.getName();

                    // 拿到字段的所有注释信息
                    List<PsiAnnotation> annotations = Optional.of(field)
                            .map(PsiModifierListOwner::getAnnotations)
                            .map(Lists::newArrayList)
                            .orElse(Lists.newArrayList());

                    // 获取字段类型的本地规范类型名称
                    String qualifiedNativeTypeName = this.extractFieldNativeQualifiedTypeName(field);

                    // 获取转换后的时钟类型名称
                    String clockTypeName = this.convertToClockTypeName(field);

                    //判断jsr303
                    boolean required = annotations.stream().map(PsiAnnotation::getQualifiedName).anyMatch(JSR303_REQUIRED_ANNOTATION::contains);

                    // 获取描述
                    String description = this.parsingDocDescription(field);

                    //字段是不是泛型
                    boolean isGeneric = this.isGeneric(field);

                    boolean isList = this.isList(field);

                    boolean isMap = this.isMap(field);

                    boolean isEnum = this.isEnum(field);

                    boolean isNormal = this.isNormal(field);

                    Structure structure = Structure
                            .builder()
                            .name(fieldName)
                            .required(required)
                            .description(description)
                            .nativeType(
                                    Optional.ofNullable(tagToElement.get(qualifiedNativeTypeName))
                                            .map(PsiTypeElement::getType)
                                            .map(this::extractFieldNativeQualifiedTypeName)
                                            .orElse(qualifiedNativeTypeName)
                            )
                            .clockType(clockTypeName)
                            .build();


                    if (isList) {


                        //集合
                        PsiType genericsType = this.parsingListGenericsPsiType(field);


                        if (!this.isNormal(genericsType)) {

                            if (tagToElement.keySet().contains(genericsType.getCanonicalText())) {
                                PsiTypeElement element = tagToElement.get(genericsType.getCanonicalText());

                                if (!this.isNormal(element.getType())) {

                                    List<Structure> children = this.parsing(
                                            PsiTypesUtil.getPsiClass(element.getType()),
                                            generic
                                    );
                                    structure.setChildren(children);
                                }


                            } else {
                                structure.setChildren(
                                        this.parsing(
                                                PsiTypesUtil.getPsiClass(genericsType),
                                                Lists.newArrayList()
                                        )
                                );
                            }

                        }


                    } else if (isMap) {
                        //Map
                    } else if (tagToElement.keySet().contains(qualifiedNativeTypeName)) {
                        //泛型
                        PsiTypeElement element = generic.remove(0);
                        structure
                                .setClockType("object")
                                .setChildren(this.parsing(
                                        PsiTypesUtil.getPsiClass(element.getType()),
                                        generic
                                ));
                    } else if (isEnum) {
                        //枚举类型
                        structure.setOptional(this.parsingEnumStructure(fieldPsiClass));
                    } else if (BASIC_SYSTEM_TYPE.contains(qualifiedNativeTypeName)) {
                        //基础数据类型
                    } else {
                        //自定义对象


                        if (tagToElement.keySet().contains(qualifiedNativeTypeName)) {
                            //如果自身 就是泛型的tag指向
                            structure.setChildren(this.parsing(fieldPsiClass, Lists.newArrayList(
                                    tagToElement.get(qualifiedNativeTypeName)
                            )));
                        } else {

//                            List<PsiTypeElement> psiTypeElements = this.extractGenericPsiTypeElement(field.getTypeElement());
//
//                            List<String> collect = psiTypeElements.stream().map(PsiTypeElement::getType).map(this::extractFieldNativeQualifiedTypeName).collect(Collectors.toList());
//
//
//                            List<PsiTypeElement> collect1 = tagToElement.entrySet().stream().filter(n -> collect.contains(n.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());

                            structure.setChildren(this.parsing(fieldPsiClass, generic));
                        }
                    }


                    return structure;
                })
                .collect(Collectors.toList());


    }

}
