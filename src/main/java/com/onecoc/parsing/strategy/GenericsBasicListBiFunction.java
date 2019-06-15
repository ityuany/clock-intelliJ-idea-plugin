package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.onecoc.model.Structure;
import com.onecoc.model.TypeStructure;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * @author yuany
 */
public class GenericsBasicListBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {
    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> tagToElement) {


        PsiType tagForGenerics = super.getListGenericsPsiType(field);

        String usableName = super.getFieldTypeUsableName(field);

        PsiTypeElement element = tagToElement.get(
                super.getFieldTypeUsableName(tagForGenerics)
        );

        String typeNameForGenerics = this.getFieldTypeUsableName(element.getType());

        return Structure
                .builder()
                .id(UUID.randomUUID().toString())
                .name(field.getName())
                .required(super.getRequired(field))
                .description(super.getDocDescription(field))
                .type(
                        TypeStructure
                                .builder()
                                .nativeName(usableName)
                                .clockName(super.toClockTypeName(usableName))
                                .extraNativeNameForList(typeNameForGenerics)
                                .extraClockNameForList(super.toClockTypeName(typeNameForGenerics))
                                .deep(super.getListDeep(field))
                                .build()
                )
                .build();
    }
}
