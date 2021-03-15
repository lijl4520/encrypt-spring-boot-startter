package com.lijl.encrypy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Lijl
 * @ClassName EncryptProperties
 * @Description 参数配置类
 * @Date 2021/3/10 9:42
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "spring.encrypt")
public class EncryptProperties {

    private final static String DEFAULT_KEY = "www.lijiaxxx.com";
    private final static String DEFAULT_TYPE = "AES";
    private String key = DEFAULT_KEY;
    private String type = DEFAULT_TYPE;

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
