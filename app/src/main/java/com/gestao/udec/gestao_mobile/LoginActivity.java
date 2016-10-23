package com.gestao.udec.gestao_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    String url = "http://192.168.1.66/gestao/mobile/verificacion_datos_basicos.php";
    SessionManager sesion;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sesion = new SessionManager(LoginActivity.this);
        if (sesion.isLoggedIn()) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());

            verificacionPerfil(sesion.getUserDetails().get("rol"));
        }


        final EditText etemail = (EditText) findViewById(R.id.etCorreo);
        final EditText etPassword = (EditText) findViewById(R.id.etClave);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegistro);
        final TextView tvOlvideLink = (TextView) findViewById(R.id.tvOlvido);;
        final Button bLogin = (Button) findViewById(R.id.btnIngresar);
        final ImageView iudec = (ImageView) findViewById(R.id.iudec);

        String font_path = "fonts/Ubuntu-C.ttf";
        final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);

        etemail.setTypeface(TF);
        etPassword.setTypeface(TF);
        tvRegisterLink.setTypeface(TF);
        tvOlvideLink.setTypeface(TF);
        bLogin.setTypeface(TF);

        iudec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = (String) iudec.getTag();

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

        tvOlvideLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent olvideIntent = new Intent(LoginActivity.this, OlvidePaso1Activity.class);
                olvideIntent.putExtra("correo",etemail.getText().toString());
                LoginActivity.this.startActivity(olvideIntent);
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
                if (etPassword.length() < 8) {
                    etPassword.setError(getResources().getString(R.string.claveNoCaracter));
                    estado = false;
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
                                    String id = jsonResponse.getString("id");
                                    String email = jsonResponse.getString("email");
                                    String rol = jsonResponse.getString("rol");

                                    if(jsonResponse.getString("estado").equals("N")){
                                        new AlertDialog.Builder(LoginActivity.this)
                                                .setTitle(getResources().getString(R.string.vinculacionClase))
                                                .setMessage(getResources().getString(R.string.docenteNoConfirmado))
                                                .setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }else {
                                        SessionManager sesion = new SessionManager(LoginActivity.this);
                                        sesion.createLoginSession(name1, email, id, rol);
                                        requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        verificacionPerfil(rol);
                                    }
                                } else {

                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(correo, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            }
        });
    }

    private void verificacionPerfil(final String rol) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject clases = jArray.getJSONObject(i);
                        if (clases.getString("permiso").equals("0")) {
                            Intent intent;
                            intent = new Intent(LoginActivity.this, PerfilActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            LoginActivity.this.startActivity(intent);

                        } else if (rol.equals("D")) {
                            Intent intent;
                            intent = new Intent(LoginActivity.this, TeacherAreaActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            LoginActivity.this.startActivity(intent);
                        } else if (rol.equals("E")) {
                            Intent intent;
                            intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            LoginActivity.this.startActivity(intent);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_persona", sesion.getUserDetails().get("id"));
                parameters.put("rol", rol);
                return parameters;
            }
        };
        requestQueue.add(request);

    }

}