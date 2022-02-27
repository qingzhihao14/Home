package com.my.friends.controller;

import com.my.friends.utils.pay.QRCodeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * @Atuhor: qin
 * @Create: 2022-02-25-23-32
 * @Time: 23:32
 * @Description:
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Instant minus = Instant.now().minus(Duration.ofMinutes(1));
        Date from = Date.from(minus);
        System.out.println(minus);
        System.out.println(from);
        QRCodeUtil.encode("weixin://wxpay/bizpayurl?pr=WF0xw1Ezz","", "C:/Home"+ File.separator+ UUID.randomUUID().toString()+".jpg");
        System.out.println("C:/Home"+ File.separator+ UUID.randomUUID().toString()+".jpg");
    }
}
