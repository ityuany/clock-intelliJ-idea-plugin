package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.util.PsiTypesUtil;
import com.onecoc.model.Structure;
import com.onecoc.model.TypeStructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * @author yuany
 */
public class ObjectListBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {


    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> tagToElement) {
        String id = UUID.randomUUID().toString();

        String fieldName = field.getName();

        boolean required = super.getRequired(field);

        String description = super.getDocDescription(field);
        //字段类型名称
        String usableName = super.getFieldTypeUsableName(field);

        PsiClass psiClass = Optional.of(field).map(super::getListGenericsPsiType).map(PsiTypesUtil::getPsiClass).orElse(null);

        String nativeTypeNameForGenerics = Optional.of(field).map(super::getListGenericsPsiType).map(super::getFieldTypeUsableName).orElse(null);

        List<Structure> children = super.typeParsing.parsing(psiClass, tagToElement.values());

        return Structure
                .builder()
                .id(id)
                .name(fieldName)
                .required(required)
                .description(description)
                .type(
                        TypeStructure
                                .builder()
                                .nativeName(usableName)
                                .clockName(toClockTypeName(usableName))
                                .extraNativeNameForList(nativeTypeNameForGenerics)
                                .extraClockNameForList(toClockTypeName(nativeTypeNameForGenerics))
                                .deep(super.getListDeep(field))
                                .build()
                )
                .children(children)
                .build();
    }
}
