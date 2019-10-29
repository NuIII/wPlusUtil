package com.weixin.common.util.exception;

import com.womdata.common.core.exception.WomException;
import com.womdata.common.core.model.BaseResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


/**
 * @Auther :   Dane.shang
 * @Date :   16:46 2019-07-24
 * @Description :
 * @Version :
 **/
@ControllerAdvice
@ResponseBody
public class GroupExceptionHandler {

    @ExceptionHandler(value = {WomException.class})
    public BaseResponse womException(WomException ex, HttpServletResponse response) {
        if (null != ex.getHttpCode()) {
            response.setStatus(ex.getHttpCode());
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(Integer.valueOf(ex.getCode()));
        baseResponse.setMsg(ex.getMessage());
        return baseResponse;
    }


}
