package com.weixin.common.util.exception;

import com.weixin.common.util.constants.BusinessConstant;
import com.womdata.common.core.model.BaseResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 分组校验异常拦截器
 *
 * @author huangweiwei
 * @version 1.0
 * @create 2019-09-09 15:52
 **/

@RestControllerAdvice
public class ValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        BaseResponse baseResponse = new BaseResponse();
        if (result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();
            StringBuilder stringBuilder = new StringBuilder();
            errors.forEach(p -> {

                FieldError fieldError = (FieldError) p;
                stringBuilder.append(fieldError.getField());
                stringBuilder.append(":");
                stringBuilder.append(fieldError.getDefaultMessage());
                stringBuilder.append(";");
            });
            baseResponse.setCode(BusinessConstant.ONE);
            baseResponse.setMsg(String.valueOf(stringBuilder));
        }
        return baseResponse;
    }
}
