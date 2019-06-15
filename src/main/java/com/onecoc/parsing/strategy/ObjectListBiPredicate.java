package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * @author yuany
 */
public class ObjectListBiPredicate extends ParsingStrategy implements BiPredicate<PsiField, Map<String, PsiTypeElement>> {


    /**
     * List<Address> 场景
     */
    @Override
    public boolean test(PsiField field, Map<String, PsiTypeElement> tagToElement) {

        PsiType typeForGenerics = super.getListGenericsPsiType(field);

        // 不属于外部泛型
        boolean isGenericsTag = tagToElement.containsKey(
                Optional.ofNullable(typeForGenerics)
                        .map(super::getFieldTypeUsableName)
                        .orElse(null)
        );

        String usableName = super.getFieldTypeUsableName(field);

        //自身先是List
        boolean isList = super.isList(field);

        //泛型不是基础数据类型
        boolean isNotBasicForGenerics = !super.isBasic(typeForGenerics);

        //非泛型传递的引用依赖
        boolean isNotTag = !tagToElement.keySet().contains(usableName);

        boolean isMap = super.isMap(super.getListGenericsPsiType(field));

        return isList && isNotBasicForGenerics && isNotTag && !isGenericsTag && !isMap;
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
