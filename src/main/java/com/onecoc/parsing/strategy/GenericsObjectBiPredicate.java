package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class GenericsObjectBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {


    /**
     * T - Address
     * <p>
     * T content
     */
    @Override
    public boolean test(PsiField field, Map<String, PsiTypeElement> tagToElement) {


        String usableName = super.getFieldTypeUsableName(field);

        boolean isTag = tagToElement.containsKey(usableName);

        boolean isMap = super.isMap(super.getListGenericsPsiType(field));


        return isTag && !isMap;
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
