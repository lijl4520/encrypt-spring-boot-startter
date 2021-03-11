package com.lijl.encrypy.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijl.encrypy.annotation.Encrpty;
import com.lijl.encrypy.config.EncryptProperties;
import com.lijl.encrypy.model.RespBean;
import com.lijl.encrypy.utils.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author Lijl
 * @ClassName EncryptResponse
 * @Description 拦截响应并对数据对数据进行解密处理
 * @Date 2021/3/11 9:59
 * @Version 1.0
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class EncryptResponse implements ResponseBodyAdvice<RespBean> {

    private ObjectMapper om = new ObjectMapper();

    EncryptProperties encryptProperties;

    @Autowired
    public void setEncryptProperties(EncryptProperties encryptProperties){
        this.encryptProperties = encryptProperties;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(Encrpty.class);
    }

    @Override
    public RespBean beforeBodyWrite(RespBean respBean, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        byte[] keyBytes = encryptProperties.getKey().getBytes();
        try {
            if (respBean.getObj()!=null){
                respBean.setObj(AESUtils.encrypy(om.writeValueAsBytes(respBean.getObj()),keyBytes));
            }
            return respBean;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        respBean.setStatus(500);
        respBean.setMsg("加密异常");
        respBean.setObj(null);
        return respBean;
    }
}
