package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class SelfBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {


    @Override
    public boolean test(PsiField field, Map<String, PsiTypeElement> tagToElement) {


        return false;
    }


}
