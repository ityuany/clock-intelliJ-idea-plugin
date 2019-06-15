package com.onecoc.parsing.strategy;

import com.google.common.collect.Lists;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.util.PsiTypesUtil;
import com.onecoc.model.EnumStructure;
import com.onecoc.model.Structure;
import com.onecoc.model.TypeStructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author yuany
 */
public class EnumBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {

    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> tagToElement) {


        String usableName = super.getFieldTypeUsableName(field);

        List<EnumStructure> enumStructures = Optional.ofNullable(field)
                .map(PsiField::getType)
                .map(PsiTypesUtil::getPsiClass)
                .map(this::parsingEnumStructure)
                .orElse(null);

        return Structure.builder()
                .id(
                        UUID.randomUUID().toString()
                )
                .name(
                        Optional.ofNullable(field).map(NavigationItem::getName).orElse(null)
                )
                .required(super.getRequired(field))
                .description(super.getDocDescription(field))
                .type(
                        TypeStructure
                                .builder()
                                .nativeName(usableName)
                                .clockName("enum")
                                .enumStructures(enumStructures)
                                .build()
                )
                .build();
    }

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
}
