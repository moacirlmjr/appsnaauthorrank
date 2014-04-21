package br.com.ufpb.appsnaauthorrank.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuth extends Authenticator {
    private PasswordAuthentication auth;

    public ProxyAuth(String user, String password) {
        auth = new PasswordAuthentication(user, password == null ? new char[]{} : password.toCharArray());
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return auth;
    }
}
