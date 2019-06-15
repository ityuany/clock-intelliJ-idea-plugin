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
public class BasicBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {

    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> tagToElement) {

        String id = UUID.randomUUID().toString();

        //字段名称
        String fieldName = field.getName();

        //字段是否必要
        boolean required = super.getRequired(field);

        //字段描述
        String description = super.getDocDescription(field);

        //字段类型名称
        String usableName = super.getFieldTypeUsableName(field);

        //时钟类型
        String clockTypeName = super.toClockTypeName(usableName);

        return Structure.builder()
                .id(id)
                .name(fieldName)
                .required(required)
                .description(description)
                .type(
                        TypeStructure
                                .builder()
                                .nativeName(usableName)
                                .clockName(clockTypeName)
                                .build()
                )
                .build();
    }
}
