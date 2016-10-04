package com.gestao.udec.gestao_mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etemail = (EditText) findViewById(R.id.etCorreo);
        final EditText etPassword = (EditText) findViewById(R.id.etClave);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegistro);
        final Button bLogin = (Button) findViewById(R.id.btnIngresar);
        final ImageView iudec = (ImageView) findViewById(R.id.iudec);


        iudec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = (String)iudec.getTag();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse(url));

                startActivity(intent);

            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String correo = etemail.getText().toString();
                final String password = etPassword.getText().toString();

                boolean estado = true;
                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                String email = etemail.getText().toString();

                Matcher mather = pattern.matcher(email);

                if (mather.find() != true) {
                    etemail.setError(getResources().getString(R.string.correoInvalido));
                }
                if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    etPassword.setError(getResources().getString(R.string.campoNoNulo));
                    estado = false;
                }
                if (etPassword.length() < 7) {
                    etPassword.setError(getResources().getString(R.string.claveNoCaracter));
                }

                if (estado == true) {




                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String name1 = jsonResponse.getString("name1");


                                Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                               intent.putExtra("name1", name1);
                                LoginActivity.this.startActivity(intent);
                            } else {

                                Toast.makeText(LoginActivity.this,getResources().getString(R.string.errorLogin),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(correo, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }}
        });
    }
}