package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class BasicBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {

    @Override
    public boolean test(PsiField element, Map<String, PsiTypeElement> tagToElement) {
        return super.isBasic(element);
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
