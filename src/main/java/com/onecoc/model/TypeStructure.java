package com.onecoc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yuany
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeStructure {

    private String clockName;

    private String nativeName;

    private String extraClockNameForList;

    private String extraNativeNameForList;

    private int deep;

    private List<EnumStructure> enumStructures;

}
