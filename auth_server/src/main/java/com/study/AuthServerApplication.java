package com.study;

import com.study.config.RsaKeyPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 35612
 */
@SpringBootApplication
/**加载自定义的配置文件*/
@EnableConfigurationProperties(RsaKeyPropertiesConfig.class)
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
