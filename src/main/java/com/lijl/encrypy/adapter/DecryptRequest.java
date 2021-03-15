package com.lijl.encrypy.adapter;

import com.lijl.encrypy.annotation.Decrpty;
import com.lijl.encrypy.config.EncryptProperties;
import com.lijl.encrypy.utils.AESUtils;
import com.lijl.encrypy.utils.DesEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @Author Lijl
 * @ClassName DecryptRequest
 * @Description 拦截控制器方法，对参数加密参数进行解密
 * @Date 2021/3/11 10:09
 * @Version 1.0
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class DecryptRequest extends RequestBodyAdviceAdapter {

    EncryptProperties encryptProperties;

    @Autowired
    public void setEncryptProperties(EncryptProperties encryptProperties){
        this.encryptProperties = encryptProperties;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(Decrpty.class) || methodParameter.hasParameterAnnotation(Decrpty.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(final HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String type = encryptProperties.getType();
        byte[] body = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(body);

        try {
            byte[] decrypt = new byte[0];
            if ("AES".equals(type)){
                decrypt = AESUtils.decrypt(body,encryptProperties.getKey().getBytes());
            }else if ("DES".equals(type)){
                String bodyStr = new String(body);
                decrypt = DesEncryptUtil.decrypt(encryptProperties.getKey(),bodyStr);
            }
            if (decrypt!=null && decrypt.length>0){
                final ByteArrayInputStream bais = new ByteArrayInputStream(decrypt);
                return new HttpInputMessage() {
                    @Override
                    public InputStream getBody() {
                        return bais;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return inputMessage.getHeaders();
                    }
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
