package com.lijl.encrypy.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijl.encrypy.annotation.Encrpty;
import com.lijl.encrypy.config.EncryptProperties;
import com.lijl.encrypy.model.RespBean;
import com.lijl.encrypy.utils.AESUtils;
import com.lijl.encrypy.utils.DesEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


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

    private final ObjectMapper om = new ObjectMapper();

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
        String type = encryptProperties.getType();
        try {
            if (respBean.getObj()!=null){
                if ("AES".equals(type)){
                    respBean.setObj(AESUtils.encrypy(om.writeValueAsBytes(respBean.getObj()),keyBytes));
                }else if ("DES".equals(type)){
                    respBean.setObj(DesEncryptUtil.encrypt(om.writeValueAsBytes(respBean.getObj()),encryptProperties.getKey()));
                }
            }
            return respBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        respBean.setStatus(500);
        respBean.setMsg("加密异常");
        respBean.setObj(null);
        return respBean;
    }
}
