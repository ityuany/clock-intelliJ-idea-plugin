package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.util.PsiTypesUtil;
import com.onecoc.model.Structure;
import com.onecoc.model.TypeStructure;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author yuany
 */
public class OtherBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {
    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> tagToElement) {

        String usableName = super.getFieldTypeUsableName(field);

        List<String> fieldTagList = Optional.ofNullable(field)
                .map(PsiVariable::getTypeElement)
                .map(super::getGenericPsiTypeElement)
                .orElse(Lists.newArrayList())
                .stream()
                .map(PsiTypeElement::getType)
                .map(super::getFieldTypeUsableName)
                .collect(Collectors.toList());

        List<PsiTypeElement> elements = tagToElement.entrySet()
                .stream()
                .filter(n -> fieldTagList.contains(n.getKey()))
                .map(Map.Entry::getValue).collect(Collectors.toList());


        List<Structure> children = super.typeParsing.parsing(
                Optional.of(field)
                        .map(PsiField::getType)
                        .map(PsiTypesUtil::getPsiClass)
                        .orElse(null),
                elements
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
