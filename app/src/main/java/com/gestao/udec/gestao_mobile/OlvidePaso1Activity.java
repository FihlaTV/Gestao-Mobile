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

public class OlvidePaso1Activity extends AppCompatActivity implements View.OnClickListener {
    String url ="http://gestao-web.co/olvido_contrasena_envio.php";
    String url2 ="http://gestao-web.co/olvido_contrasena_restablecimiento.php";
    EditText correo;
    EditText codigo;
    Button enviar;
    Button recuperar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_paso1);

        correo = (EditText) findViewById(R.id.etCorreo);
        enviar = (Button) findViewById(R.id.btGenerarCodigo);

        codigo = (EditText) findViewById(R.id.etCodigo);
        recuperar = (Button) findViewById(R.id.btRecuperar);

        String font_path = "fonts/Ubuntu-C.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        correo.setTypeface(TF);
        enviar.setTypeface(TF);
        codigo.setTypeface(TF);
        recuperar.setTypeface(TF);

        codigo.setEnabled(false);
        recuperar.setClickable(false);

        correo.setText(getIntent().getStringExtra("correo"));

        enviar.setOnClickListener(this);
        recuperar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btGenerarCodigo:
                requestQueue = Volley.newRequestQueue(getApplicationContext());


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(correo.getWindowToken(), 0);


                boolean estado = true;


                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                ;

                Matcher mather = pattern.matcher(correo.getText().toString());

                if (mather.find() != true) {
                    correo.setError(getResources().getString(R.string.correoInvalido));
                    estado = false;
                }



                if (estado == true) {

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(OlvidePaso1Activity.this, response.substring(0,response.length()-1), Toast.LENGTH_LONG).show();
                            if(response.substring(response.length()-1).equals("1")) {
                                enviar.setClickable(false);
                                correo.setEnabled(false);
                                enviar.setVisibility(View.GONE);
                                correo.setVisibility(View.GONE);

                                codigo.setEnabled(true);
                                recuperar.setClickable(true);
                                codigo.setVisibility(View.VISIBLE);
                                recuperar.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(OlvidePaso1Activity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("correo", correo.getText().toString());


                            return parameters;
                        }
                    };
                    requestQueue.add(request);


                }


                break;
            case R.id.btRecuperar:
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(OlvidePaso1Activity.this, response.substring(0,response.length()-1), Toast.LENGTH_LONG).show();
                            if(response.substring(response.length()-1).equals("1")){
                                Intent intent;
                                intent = new Intent(OlvidePaso1Activity.this, OlvidePaso2Activity.class);
                                intent.putExtra("correo",correo.getText().toString());
                                OlvidePaso1Activity.this.startActivity(intent);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(OlvidePaso1Activity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("codigo", codigo.getText().toString());
                            parameters.put("correo", correo.getText().toString());


                            return parameters;
                        }
                    };
                    requestQueue.add(request);

                break;
        }
    }
}
