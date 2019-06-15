package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.onecoc.model.Structure;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author yuany
 */
public class GenericsObjectBiFunction extends ParsingStrategy implements BiFunction<PsiField, Map<String, PsiTypeElement>, Structure> {
    @Override
    public Structure apply(PsiField field, Map<String, PsiTypeElement> stringPsiTypeElementMap) {
        return null;
    }
}
