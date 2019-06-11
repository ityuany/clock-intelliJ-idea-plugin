package com.onecoc.parsing;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author yuany
 */
public class JavaControllerParsing implements ControllerParsing {

    @Override
    public boolean isQualifiedController(PsiClass selectedClass) {
        return Optional.ofNullable(selectedClass)
                .map(PsiModifierListOwner::getAnnotations)
                .map(Lists::newArrayList)
                .map(this::hasControllerAnnotation)
                .get();

    }

    @Override
    public boolean hasControllerAnnotation(List<PsiAnnotation> annotations) {
        return annotations
                .stream()
                .map(PsiAnnotation::getQualifiedName)
                .anyMatch(controllerAnnotations::contains);
    }

    @Override
    public String getControllerHttpPath(PsiClass selectedClass) {
        return Optional.ofNullable(selectedClass)
                .map(n -> n.getAnnotation("org.springframework.web.bind.annotation.RequestMapping"))
                .map(n -> PsiTreeUtil.findChildrenOfAnyType(n.getSourceElement(), PsiLiteralExpression.class))
                .map(Lists::newArrayList)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(PsiElement::getText)
                .map(n -> n.replace("\"", ""))
                .get();
    }

}
