package com.mscompra.compra.controller.exceptionhandler;

import com.fasterxml.classmate.AnnotationOverrides;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {

    private Integer status;
    private OffsetDateTime timestamp;
    private String title;
    private String type;
    private String detail;
    private String userMessage;
    private List<Object> objects;

    @Builder
    public static class Object {
        private String name;
        private String userMessage;
    }
}
