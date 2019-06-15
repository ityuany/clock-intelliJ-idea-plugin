package com.onecoc.test;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Children<T> {


    /**
     * 姓名
     */
    private String name;


    /**
     * 描述信息
     */
    private T description;
}
