package com.onecoc.parsing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yuany
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Structure {

    private String name;

    private String description;

    private String nativeType;

    private String clockType;

    private boolean required;

    private List<Structure> children;

    private List<EnumStructure> optional;
}
