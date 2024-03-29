package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
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
public class GenericsObjectBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {
    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> tagToElement) {


        PsiType type = Optional.ofNullable(field)
                .map(super::getFieldTypeUsableName)
                .map(tagToElement::get)
                .map(PsiTypeElement::getType)
                .orElse(null);

        PsiClass psiClass = Optional.ofNullable(type)
                .map(PsiTypesUtil::getPsiClass)
                .orElse(null);


        String usableName = Optional.ofNullable(type)
                .map(super::getFieldTypeUsableName)
                .orElse(null);

        List<Structure> children = super.typeParsing.parsing(
                psiClass,
                tagToElement.values()
        );


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
                                .clockName(toClockTypeName(usableName))
                                .build()
                )
                .children(children)
                .build();
    }
}
