package com.study.utils;

import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class RsaUtilsTest {

    private String publicFile = "D:\\programDevelopment\\developmentOfLanguage\\Java\\study\\IdeaDeveloment\\JavaEE\\framework\\springSecurity\\springboot_security_jwt_rsa_parent\\common\\rsa_key.pul";
    private String privateFile = "D:\\programDevelopment\\developmentOfLanguage\\Java\\study\\IdeaDeveloment\\JavaEE\\framework\\springSecurity\\springboot_security_jwt_rsa_parent\\common\\rsa_key";

    @Test
    public void getPublicKey() throws Exception {
        PublicKey publicKey = RsaUtils.getPublicKey(publicFile);
        System.out.println(publicKey);
    }

    @Test
    public void getPrivateKey() throws Exception {
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateFile);
        System.out.println(privateKey);
    }

    @Test
    public void generateKey() throws Exception {
        RsaUtils.generateKey(publicFile,privateFile,"study",2048);
    }
}