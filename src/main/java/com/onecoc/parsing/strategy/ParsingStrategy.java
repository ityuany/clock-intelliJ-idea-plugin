package com.onecoc.parsing.strategy;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.onecoc.parsing.Structure;

import java.util.List;

/**
 * @author yuany
 */
public abstract class ParsingStrategy {

    /**
     * 解析策略
     *
     * @param target  需要解析的节点
     * @param generic 泛型的真实节点
     * @return 数据结构
     */
    abstract Structure parsing(PsiField target, List<PsiTypeElement> generic);

}
