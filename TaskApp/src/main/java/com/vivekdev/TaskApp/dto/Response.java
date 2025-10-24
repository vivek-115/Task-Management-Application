package com.vivekdev.TaskApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    public int statusCode;
    public String message;
    public T data;  // actual data: Payload to return
}
