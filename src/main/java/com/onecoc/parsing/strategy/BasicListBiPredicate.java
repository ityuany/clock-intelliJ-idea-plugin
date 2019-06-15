package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class BasicListBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {
    @Override
    public boolean test(PsiField psiField, Map<String, PsiTypeElement> stringPsiTypeElementMap) {

        PsiType listGenericsPsiType = super.getListGenericsPsiType(psiField);

        return super.isList(psiField) && super.isBasic(listGenericsPsiType);
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
