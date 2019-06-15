package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class GenericsListBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {


    @Override
    public boolean test(PsiField field, Map<String, PsiTypeElement> tagToElement) {


        boolean isList = super.isList(field);


        boolean isGenericsTag = super.isGenericsTagForList(field, tagToElement);

        boolean isBasic = super.isBasicForGenericsTagForList(field, tagToElement);

        boolean isMap = super.isMap(super.getListGenericsPsiType(field));

        return isList && isGenericsTag && !isBasic && !isMap;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
