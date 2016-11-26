package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OlvidePaso2Activity extends AppCompatActivity implements View.OnClickListener{
    String url ="http://gestao-web.co/olvido_contrasena_nuevo.php";

    EditText contrasena;
    EditText contrasena2;
    Button actualizar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_paso2);

        contrasena = (EditText) findViewById(R.id.etClave);
        contrasena2 = (EditText) findViewById(R.id.etClave2);
        actualizar = (Button) findViewById(R.id.btActualizar);

        String font_path = "fonts/Ubuntu-C.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        contrasena.setTypeface(TF);
        contrasena2.setTypeface(TF);
        actualizar.setTypeface(TF);

        actualizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btActualizar:
                    requestQueue = Volley.newRequestQueue(getApplicationContext());

                    boolean estado = true;

                    if(!contrasena.getText().toString().equals(contrasena2.getText().toString())){
                        contrasena.setError(getResources().getString(R.string.claveNoIgual));
                        contrasena2.setError(getResources().getString(R.string.claveNoIgual));
                        estado = false;
                    }

                    if (contrasena.length() < 8) {
                        contrasena.setError(getResources().getString(R.string.claveNoCaracter));
                        estado = false;
                    }

                    if (estado == true) {

                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(OlvidePaso2Activity.this, response, Toast.LENGTH_LONG).show();
                                    Intent intent;
                                    intent = new Intent(OlvidePaso2Activity.this, LoginActivity.class);
                                    OlvidePaso2Activity.this.startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(OlvidePaso2Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parameters = new HashMap<String, String>();

                                parameters.put("correo", getIntent().getStringExtra("correo"));
                                parameters.put("contrasena", contrasena.getText().toString());


                                return parameters;
                            }
                        };
                        requestQueue.add(request);


                    }


                    break;
            }
    }
}
