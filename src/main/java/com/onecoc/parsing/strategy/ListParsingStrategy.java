package com.onecoc.parsing.strategy;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.onecoc.parsing.Structure;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author yuany
 */
public class ListParsingStrategy extends ParsingStrategy {

    @Override
    public Structure parsing(PsiField field, List<PsiTypeElement> generic) {

        PsiType psiType = this.extractListGenericsNodeType(field);

        return null;
    }

    public PsiType extractListGenericsNodeType(PsiField field) {
        return Optional.ofNullable(field)
                .map(PsiVariable::getTypeElement)
                .map(element -> PsiTreeUtil.findChildrenOfAnyType(element, PsiJavaCodeReferenceElement.class))
                .map(Lists::newArrayList)
                .map(Lists::reverse)
                .map(Collection::stream)
                .map(n -> n.skip(1))
                .flatMap(Stream::findFirst)
                .map(PsiJavaCodeReferenceElement::getTypeParameters)
                .map(Lists::newArrayList)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .orElse(null);
    }

    ;

}
