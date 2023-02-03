package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.lang.String;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;

import java.security.InvalidKeyException;
import java.security.Key;
import android.util.Base64;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static String foo(String p0,String p1,String p2,String p3,String p4,String p5,String p6) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException{
        Mac instance = Mac.getInstance("HmacSHA1");
        instance.init(new SecretKeySpec(p1.getBytes(), "HmacSHA1"));        // PEDOMETER ID
        instance.update(p3.getBytes());                                             // TIME
        instance.update(p4.getBytes());                                             // NONCE
        if (p2 != null && p2.length() > 0) {                                        // ACCESS_TOKEN
            instance.update(p2.getBytes());
        }
        if (p5 != null && p5.length() > 0) {                                        // PATH PARAMETERS
            instance.update(p5.getBytes());
        }
        if (p6 != null && p6.length() > 0) {                                        // MD5 BODY (POST, PUT, PATCH)
            instance.update(p6.getBytes());
        }
        if (p0 != null && p0.length() > 0) {                                        // PATH
            instance.update(p0.getBytes());
        }
        return (URLEncoder.encode(Base64.encodeToString(instance.doFinal(), 0), "ASCII")).replace("%0A", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String p0, p1, p2, p3, p4, p5, p6;
        p0 = "/pacer/android/api/v18/accounts/334794565";   // p0.i.F.getPath(); OK
        p1 = "B7A4DB15-D69A-4C8A-BA68-39E0AA208DB8";   //Pedometer.o(ApiConstant.GROUP_API_TOKEN);ù
        p2 = "pt_11b9df93-167d-4d31-b579-6a0cb2197f10";     //"X-Pacer-Access-Token";   OK
        p3 = "1668596341";  //"X-Pacer-Time";   OK
        p4 = "138529347";   //"X-Pacer-Nonce"   OK
        p5 = ""; // Path parameters;
        p6 = ""; //  md5 body

        String ret = null;
        try {
            ret = foo(p0, p1, p2, p3, p4, p5, p6);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(ret);

    }
}