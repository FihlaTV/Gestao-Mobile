package com.gestao.udec.gestao_mobile;

/**
 * Created by diego on 29/09/2016.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://192.168.1.2/gestao/mobile/Login.php";
    private Map<String, String> params;

    public LoginRequest(String correo, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("correo", correo);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}