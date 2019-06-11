package com.onecoc.parsing;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yuany
 */
public class JavaMethodController implements MethodParsing {

    @Override
    public boolean isQualifiedHttpInterface(PsiMethod selectedMethod) {
        return Optional.ofNullable(selectedMethod)
                .map(PsiModifierListOwner::getAnnotations)
                .map(Lists::newArrayList)
                .map(this::hasRouteMappingAnnotations)
                .get();
    }

    @Override
    public boolean hasRouteMappingAnnotations(List<PsiAnnotation> annotations) {
        return annotations
                .stream()
                .map(PsiAnnotation::getQualifiedName)
                .anyMatch(routeMappingAnnotations::contains);
    }

    @Override
    public String getRoutePath(PsiMethod selectedMethod) {

        return routeMappingAnnotations
                .stream()
                .map(
                        route ->
                                Optional
                                        .ofNullable(selectedMethod)
                                        .map(method -> method.getAnnotation(route))
                                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .map(n -> PsiTreeUtil.findChildrenOfAnyType(n.getSourceElement(), PsiLiteralExpression.class))
                .map(Lists::newArrayList)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(PsiElement::getText)
                .map(n -> n.replace("\"", ""))
                .get();

    }

    @Override
    public String getRequestMethod(PsiMethod selectedMethod) {
        return Optional.of(selectedMethod)
                .map(n -> n.getAnnotation(REQUEST_MAPPING))
                .map(n -> n.findAttributeValue("method"))
                .map(n -> PsiTreeUtil.findChildrenOfAnyType(n.getNavigationElement(), PsiIdentifier.class))
                .map(Lists::newArrayList)
                .map(Lists::reverse)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(PsiElement::getText)
                .get();
    }

    @Override
    public String getMethodDescription(PsiMethod selectedMethod) {
        return Optional.of(selectedMethod)
                .map(PsiJavaDocumentedElement::getDocComment)
                .map(PsiDocComment::getDescriptionElements)
                .map(Lists::newArrayList)
                .map(n -> n.stream().map(PsiElement::getText).collect(Collectors.toList()))
                .map(n -> String.join("", n))
                .map(String::trim)
                .orElse(
                        Optional.of(selectedMethod)
                                .map(PsiMethod::getName)
                                .orElse(null)
                );
    }

    @Override
    public List<PsiTypeElement> getMethodReturnGenericStructure(PsiMethod selectedMethod) {
        return Optional.of(selectedMethod)
                .map(n -> PsiTreeUtil.findChildrenOfAnyType(n.getReturnTypeElement(), PsiTypeElement.class))
                .map(Lists::newArrayList)
                .get();
    }

    @Override
    public boolean isGenericForReturnType(PsiMethod selectedMethod) {
        return Optional.of(selectedMethod)
                .map(n -> PsiTypesUtil.getPsiClass(n.getReturnType()))
                .map(PsiTypeParameterListOwner::getTypeParameters)
                .map(Lists::newArrayList)
                .map(ArrayList::size)
                .orElse(0) > 0;
    }
}
