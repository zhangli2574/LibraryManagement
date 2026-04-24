package com.kyk.librarymanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "统一响应格式")
public class ApiResponse<T> {
    
    @Schema(description = "状态码", example = "200")
    private Integer code;
    
    @Schema(description = "消息", example = "success")
    private String message;
    
    @Schema(description = "数据")
    private T data;
    
    public ApiResponse() {
    }
    
    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    // Getters and Setters
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
