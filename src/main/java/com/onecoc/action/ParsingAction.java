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
import com.onecoc.model.Structure;
import com.onecoc.parsing.*;
import com.onecoc.test.Interfaces;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

            selectedMethod.getParameterList().getParameters()[0].getTypeElement();

//            selectedMethod.getReturnTypeElement()

            String routePath = methodParsing.getRoutePath(selectedMethod);

            String requestMethod = methodParsing.getRequestMethod(selectedMethod);

            String methodDescription = methodParsing.getMethodDescription(selectedMethod);

            List<PsiTypeElement> methodReturnGenericStructure = typeParsing.extractGenericPsiTypeElement(selectedMethod.getReturnTypeElement());


            boolean genericForReturnType = typeParsing.hasGenericTag(selectedMethod.getReturnType());


            String requestBodyAnnotation = "org.springframework.web.bind.annotation.RequestBody";

//            List<List<Structure>> payloadParameter = Lists
//                    .newArrayList(selectedMethod.getParameterList().getParameters())
//                    .stream()
//                    .filter(k -> k.hasAnnotation(requestBodyAnnotation))
//                    .map(k -> typeParsing.parsing(PsiTypesUtil.getPsiClass(k.getType()), methodReturnGenericStructure))
//                    .collect(Collectors.toList());

            List<Structure> returnValue = typeParsing.parsing(PsiTypesUtil.getPsiClass(selectedMethod.getReturnType()), methodReturnGenericStructure);

            System.out.println(String.format("接口id：%s%s%s", selectedClass.getQualifiedName(), controllerHttpPath, routePath));
            System.out.println(String.format("是否合法：%s", isInterface && isController));
            System.out.println(String.format("接口名称：%s", methodDescription));
            System.out.println(String.format("请求方法：%s", requestMethod));
            System.out.println(String.format("返回值是否是泛型：%s", genericForReturnType));
            System.out.println(String.format("返回值的泛型结构：%s", methodReturnGenericStructure));
            System.out.println(String.format("请求地址：%s%s", controllerHttpPath, routePath));

//            System.out.println(String.format("请求参数：%s", JSONObject.toJSONString(payloadParameter)));
            System.out.println(String.format("接口的返回值：%s", JSONObject.toJSONString(returnValue)));


            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");


            Interfaces interfaces = Interfaces
                    .builder()
                    .id(String.format("%s%s%s", selectedClass.getQualifiedName(), controllerHttpPath, routePath))
                    .name(methodDescription)
                    .method(requestMethod)
                    .requestPath(String.format("%s%s", controllerHttpPath, routePath))
//                    .jsonPayload(payloadParameter.get(0))
                    .returnJson(returnValue)
                    .build();


            String requestBody = JSONObject.toJSONString(interfaces);


            Request request = new Request.Builder()
                    .url("http://127.0.0.1:9090/test/world")
                    .post(RequestBody.create(mediaType, requestBody))
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
//                    Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
//                    Headers headers = response.headers();
//                    for (int i = 0; i < headers.size(); i++) {
//                        Log.d(TAG, headers.name(i) + ":" + headers.value(i));
//                    }
//                    Log.d(TAG, "onResponse: " + response.body().string());
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
