package com.onecoc.model;

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

    private String id;

    private String name;

    private String description;

    private boolean required;

    private String nativeType;

    private String clockType;

    private String nativeTypeForList;

    private String clockTypeForList;

    private TypeStructure type;

    private List<Structure> children;

    private List<EnumStructure> optional;
}
