package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diego on 24/10/2016.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    RequestQueue requestQueue;
    SessionManager sesion;
    @Override
    public void onTokenRefresh() {
        SessionManager sesion = new SessionManager(getApplicationContext());
        if (sesion.isLoggedIn()) {
            String token = FirebaseInstanceId.getInstance().getToken();
            registerToken(token,sesion.getUserDetails().get("id"));
        }else{

        }
    }

    private void registerToken(final String token, final String persona) {
        sesion = new SessionManager(FirebaseInstanceIDService.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlp = "http://gestao-web.co/token_insert.php";
        StringRequest request = new StringRequest(Request.Method.POST, urlp, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("from_token",token);
                SessionManager sesion = new SessionManager(getApplicationContext());
                parameters.put("persona",sesion.getUserDetails().get("id"));
                return parameters;
            }
        };
        requestQueue.add(request);


    }

    }
