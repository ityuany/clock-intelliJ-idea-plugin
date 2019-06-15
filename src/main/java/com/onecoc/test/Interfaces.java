package com.onecoc.test;

import com.onecoc.model.Structure;
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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Interfaces {


    private String id;

    private String name;

    private String method;

    private String requestPath;

    private List<Structure> jsonPayload;

    private List<Structure> returnJson;

}
