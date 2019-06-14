package com.onecoc.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.onecoc.parsing.*;
import com.onecoc.parsing.strategy.ListParsingStrategy;
import com.onecoc.parsing.strategy.ParsingStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @author yuany
 */
public class ParsingAction extends AnAction {

    private ControllerParsing controllerParsing = new JavaControllerParsing();

    private MethodParsing methodParsing = new JavaMethodController();

    private TypeParsing typeParsing = new JavaTypeParsing();


    final List<String> controllerTag = Lists.newArrayList(
            "org.springframework.web.bind.annotation.RestController",
            "org.springframework.stereotype.Controller"
    );

    final List<String> methodTag = Lists.newArrayList(
            "org.springframework.web.bind.annotation.RequestMapping",
            "org.springframework.web.bind.annotation.GetMapping",
            "org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.PatchMapping",
            "org.springframework.web.bind.annotation.DeleteMapping",
            "org.springframework.web.bind.annotation.PutMapping"
    );


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        try {
            // 获取当前的编辑行
            Editor editor = e.getData(PlatformDataKeys.EDITOR);

            //获取文件
            PsiFile file = e.getData(CommonDataKeys.PSI_FILE);

            if (Objects.isNull(editor) || Objects.isNull(file)) {
                return;
            }

            PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());

            PsiClass selectedClass = PsiTreeUtil.getContextOfType(element, PsiClass.class);

            PsiMethod selectedMethod = PsiTreeUtil.getContextOfType(element, PsiMethod.class);

            boolean isController = controllerParsing.isQualifiedController(selectedClass);

            if (!isController) {
                return;
            }

            String controllerHttpPath = controllerParsing.getControllerHttpPath(selectedClass);

            boolean isInterface = methodParsing.isQualifiedHttpInterface(selectedMethod);


            if (!isInterface) {
                return;
            }

            String routePath = methodParsing.getRoutePath(selectedMethod);

            String requestMethod = methodParsing.getRequestMethod(selectedMethod);

            String methodDescription = methodParsing.getMethodDescription(selectedMethod);

            List<PsiTypeElement> methodReturnGenericStructure = typeParsing.extractGenericPsiTypeElement(selectedMethod.getReturnTypeElement());


            boolean genericForReturnType = typeParsing.hasGenericTag(selectedMethod.getReturnType());




//            List<Structure> parameterValue = typeParsing.parsing(
//                    PsiTypesUtil.getPsiClass(Lists.newArrayList(selectedMethod.getParameterList().getParameters()).get(0).getType()),
//                    Lists.newArrayList()
//            );

            List<Structure> returnValue = typeParsing.parsing(PsiTypesUtil.getPsiClass(selectedMethod.getReturnType()), methodReturnGenericStructure);

            System.out.println(String.format("是否合法：%s", isInterface && isController));
            System.out.println(String.format("接口名称：%s", methodDescription));
            System.out.println(String.format("请求方法：%s", requestMethod));
            System.out.println(String.format("返回值是否是泛型：%s", genericForReturnType));
            System.out.println(String.format("返回值的泛型结构：%s", methodReturnGenericStructure));
            System.out.println(String.format("请求地址：%s%s", controllerHttpPath, routePath));

//            System.out.println(String.format("请求参数：%s",JSONObject.toJSONString(parameterValue)));
            System.out.println(String.format("接口的返回值：%s", JSONObject.toJSONString(returnValue)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
