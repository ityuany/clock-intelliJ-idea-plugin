package com.onecoc.parsing;

import com.intellij.psi.PsiTypeElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuany
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericMapStructure{

    private String key;

    private PsiTypeElement value;

}
