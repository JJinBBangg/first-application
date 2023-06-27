package com.example.first.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * jwt 토큰 암호화, 복호화 key property 파일에 숨기는기능
 */
@Data
@ConfigurationProperties(prefix = "jtw")
public class AppConfig {

    public SecretKey key;

    public void setKey(String key) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
    }

    public SecretKey getKey() {
        return key;
    }
}
