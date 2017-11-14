package com.hushao.schoolblog.util;
import com.hushao.schoolblog.vo.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hushao
 * ExceptionHandlerAdvice统一异常处理器
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {

        return  new ErrorResponse(false,e.getMessage());
    }
}
