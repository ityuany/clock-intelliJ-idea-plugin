package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class GenericsBasicListBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {


    /**
     *
     * T is basic type
     *
     * List<T>
     */
    @Override
    public boolean test(PsiField field, Map<String, PsiTypeElement> tagToElement) {


        boolean isList = super.isList(field);

        boolean isGenericsTag = super.isGenericsTagForList(field,tagToElement);

        boolean isBasic = super.isBasicForGenericsTagForList(field,tagToElement);

        return isList && isGenericsTag & isBasic;
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
