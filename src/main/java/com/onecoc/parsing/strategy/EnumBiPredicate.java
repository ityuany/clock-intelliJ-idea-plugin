package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class EnumBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {
    @Override
    public boolean test(PsiField field, Map<String, PsiTypeElement> stringPsiTypeElementMap) {
        return Optional.ofNullable(field)
                .map(PsiField::getType)
                .map(super::isEnum)
                .orElse(false);

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
