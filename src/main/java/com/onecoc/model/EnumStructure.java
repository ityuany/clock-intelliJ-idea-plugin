package com.onecoc.model;

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
public class EnumStructure {

    private String value;

    private String description;
}
