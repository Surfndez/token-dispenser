package com.github.yeriomin.tokendispenser;

import com.github.yeriomin.playstoreapi.GooglePlayAPI;
import com.github.yeriomin.playstoreapi.GooglePlayException;
import com.github.yeriomin.playstoreapi.PropertiesDeviceInfoProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class TokenAc2dmGsfIdResource extends TokenAc2dmResource {

    @Override
    public String handle(Request request, Response response) {
        String email = Server.passwords.getRandomEmail();
        String password = Server.passwords.get(email);
        int code = 500;
        String message;
        try {
            String token = getApi().generateToken(email, password);
            String ac2dmToken = getApi().generateAC2DMToken(email, password);
            String gsfId = getApi().generateGsfId(email, ac2dmToken);
            return token + " " + gsfId;
        } catch (GooglePlayException e) {
            if (e.getCode() >= 400) {
                code = e.getCode();
            }
            message = e.getMessage();
            Server.LOG.warn(e.getClass().getName() + ": " + message);
            halt(code, message);
        } catch (IOException e) {
            message = e.getMessage();
            Server.LOG.error(e.getClass().getName() + ": " + message);
            halt(code, message);
        }
        return "";
    }
}
