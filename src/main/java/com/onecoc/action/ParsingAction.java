package com.onecoc.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.onecoc.model.Structure;
import com.onecoc.parsing.*;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

            PsiElement data = e.getData(CommonDataKeys.PSI_ELEMENT);

            if (Objects.isNull(editor) || Objects.isNull(file)) {
                return;
            }

            List<Structure> structures = Optional
                    .ofNullable(data)
                    .map(PsiElement::getParent)
                    .map(n -> PsiTreeUtil.findChildOfType(n, PsiClass.class))
                    .map(n -> typeParsing.parsing(n, Lists.newArrayList()))
                    .orElse(null);

            System.out.println(JSONObject.toJSONString(structures));

            CopyPasteManager.getInstance().setContents(
                    new StringSelection(JSONObject.toJSONString(structures))
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
