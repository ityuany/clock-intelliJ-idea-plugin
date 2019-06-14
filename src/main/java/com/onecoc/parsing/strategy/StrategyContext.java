package com.onecoc.parsing.strategy;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiTypeElement;
import com.onecoc.parsing.Structure;

import java.util.List;

/**
 * @author yuany
 */
public class StrategyContext {

    private ParsingStrategy parsingStrategy;

    private static final List<String> BASIC_SYSTEM_TYPE = Lists.newArrayList(
            "boolean",
            "java.lang.Boolean",
            "long",
            "java.lang.Long",
            "double",
            "java.lang.Double",
            "int",
            "java.lang.Integer",
            "float",
            "java.lang.Float",
            "char",
            "java.lang.Character",
            "java.math.BigDecimal",
            "java.lang.String",
            "java.util.Date"
    );

    private static final List<String> LIST_QUALIFIED_NAME = Lists.newArrayList(
            "java.util.List",
            "java.util.ArrayList"
    );

    private static final List<String> MAP_QUALIFIED_NAME = Lists.newArrayList(
            "java.util.Map"
    );

    private static final List<String> IGNORE_FIELD_NAME = Lists.newArrayList(
            "serialVersionUID"
    );

    private static final List<String> IGNORE_FIELD_QUALIFIED_NAME = Lists.newArrayList(
            "org.bson.types.ObjectId"
    );

    private static final List<String> JSR303_REQUIRED_ANNOTATION = Lists.newArrayList(
            "javax.validation.constraints.NotNull",
            "javax.validation.constraints.NotEmpty"
    );

    public StrategyContext(ParsingStrategy parsingStrategy) {
        this.parsingStrategy = parsingStrategy;
    }

    public Structure apply(PsiField target, List<PsiTypeElement> generic) {
        return parsingStrategy.parsing(target, generic);
    }
}
