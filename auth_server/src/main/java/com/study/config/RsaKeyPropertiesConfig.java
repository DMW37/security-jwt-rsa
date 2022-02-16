package com.study.config;

import com.study.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author 35612
 */
// @Configuration //申明为配置类，同时该类不会往IOC容器中存储
@ConfigurationProperties("rsa.key")
public class RsaKeyPropertiesConfig {
    private String publicFilePath;
    private String privateFilePath;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 在类的构造后再通过 publicFilePath 获取 publicKey
     *
     * @return
     */
    @PostConstruct
    private void loadKey() throws Exception {
        publicKey = RsaUtils.getPublicKey(this.getPublicFilePath());
        privateKey = RsaUtils.getPrivateKey(this.getPrivateFilePath());
    }

    public String getPublicFilePath() {
        return publicFilePath;
    }

    public void setPublicFilePath(String publicFilePath) {
        this.publicFilePath = publicFilePath;
    }

    public String getPrivateFilePath() {
        return privateFilePath;
    }

    public void setPrivateFilePath(String privateFilePath) {
        this.privateFilePath = privateFilePath;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
