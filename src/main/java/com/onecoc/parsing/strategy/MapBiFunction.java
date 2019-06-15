package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.onecoc.model.Structure;
import com.onecoc.model.TypeStructure;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * @author yuany
 */
public class MapBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {
    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> stringPsiTypeElementMap) {


        //TODO
        String usableName = super.getFieldTypeUsableName(field);

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
                .build();
    }
}
