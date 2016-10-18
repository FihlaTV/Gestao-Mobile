package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "http://192.168.1.3/gestao/mobile/select_perfil.php";
    String url2 = "http://192.168.1.3/gestao/mobile/update_perfil.php";

    EditText codigo;//o
    EditText nombre1;//o
    EditText nombre2;
    EditText apellido1;//o
    EditText apellido2;
    EditText email;//o
    EditText telefono;//o
    EditText facebook;//o
    EditText twitter;
    EditText descripcion;//o
    EditText semestre;
    Button actualizar;

    RequestQueue requestQueue;
    SessionManager sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sesion = new SessionManager(PerfilActivity.this);

        codigo = (EditText) findViewById(R.id.etCodigo);
        nombre1 = (EditText) findViewById(R.id.etNombre1);
        nombre2 = (EditText) findViewById(R.id.etNombre2);
        apellido1 = (EditText) findViewById(R.id.etApellido1);
        apellido2 = (EditText) findViewById(R.id.etApellido2);
        email = (EditText) findViewById(R.id.etEmail);
        telefono = (EditText) findViewById(R.id.etTelefono);
        facebook = (EditText) findViewById(R.id.etFacebook);
        twitter = (EditText) findViewById(R.id.etTwitter);
        descripcion = (EditText) findViewById(R.id.etDescripcion);
        semestre = (EditText) findViewById(R.id.etSemestre);
        actualizar = (Button) findViewById(R.id.btActualizar);

        if (sesion.getUserDetails().get("rol").equals("D")) {
            facebook.setVisibility(View.VISIBLE);
            twitter.setVisibility(View.VISIBLE);
            descripcion.setVisibility(View.VISIBLE);
        } else {
            semestre.setVisibility(View.VISIBLE);
        }

        actualizar.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        obtenerDatosActuales();
    }

    protected void obtenerDatosActuales() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject clases = jArray.getJSONObject(i);
                        codigo.setText(clases.getString("codigo"));
                        nombre1.setText(clases.getString("nombre1"));
                        nombre2.setText(clases.getString("nombre2"));
                        apellido1.setText(clases.getString("apellido1"));
                        apellido2.setText(clases.getString("apellido2"));
                        email.setText(clases.getString("email"));
                        telefono.setText(clases.getString("telefono"));

                        if (sesion.getUserDetails().get("rol").equals("D")) {
                            facebook.setText(clases.getString("facebook"));
                            twitter.setText(clases.getString("twitter"));
                            descripcion.setText(clases.getString("descripcion"));
                        } else {
                            semestre.setText(clases.getString("semestre"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PerfilActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_persona", sesion.getUserDetails().get("id"));
                parameters.put("rol", sesion.getUserDetails().get("rol"));
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btActualizar:

                requestQueue = Volley.newRequestQueue(getApplicationContext());


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(codigo.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(nombre1.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(nombre2.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(apellido1.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(apellido2.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(telefono.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(facebook.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(twitter.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(descripcion.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(semestre.getWindowToken(), 0);


                boolean estado = true;


                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                ;

                Matcher mather = pattern.matcher(email.getText().toString());

                if (mather.find() != true) {
                    email.setError(getResources().getString(R.string.correoInvalido));
                    estado = false;
                }

                pattern = Pattern
                        .compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

                ;
                if (!facebook.getText().toString().equals("")) {
                    mather = pattern.matcher(facebook.getText().toString());

                    if (mather.find() != true) {
                        facebook.setError(getResources().getString(R.string.urlInvalida));
                        estado = false;
                    }
                }

                ;
                if (!twitter.getText().toString().equals("")) {
                    mather = pattern.matcher(twitter.getText().toString());

                    if (mather.find() != true) {
                        twitter.setError(getResources().getString(R.string.urlInvalida));
                        estado = false;
                    }
                }

            /*    if (telefono.getText().toString().equals("")) {
                    estado = false;
                    telefono.setError(getResources().getString(R.string.numeroTelefonicoInvalido));
                } else if (Integer.parseInt(telefono.getText().toString()) < 1000000 || Integer.parseInt(telefono.getText().toString()) >= 9999999) {
                    if ((Long.getLong(telefono.getText().toString()) < 3000000000L) || (Long.getLong(telefono.getText().toString()) >= 3999999999L)) {
                        estado = false;
                        telefono.setError(getResources().getString(R.string.numeroTelefonicoInvalido));
                    }
                }*/
                if (sesion.getUserDetails().get("rol").equals("E")) {
                    if (semestre.getText().toString().equals("")) {
                        estado = false;
                        semestre.setError(getResources().getString(R.string.semestreInvalido));
                    } else if (Integer.parseInt(semestre.getText().toString()) < 1 || Integer.parseInt(semestre.getText().toString()) >= 13) {
                        estado = false;
                        semestre.setError(getResources().getString(R.string.semestreInvalido));
                    }
                }


                if (estado == true) {

                    StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Intent intent;
                            Toast.makeText(PerfilActivity.this, response, Toast.LENGTH_LONG).show();
                            if (sesion.getUserDetails().get("rol").equals("D")) {
                                intent = new Intent(PerfilActivity.this, TeacherAreaActivity.class);
                            } else {
                                intent = new Intent(PerfilActivity.this, UserAreaActivity.class);
                            }
                            PerfilActivity.this.startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PerfilActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("id_persona", sesion.getUserDetails().get("id"));
                            parameters.put("rol", sesion.getUserDetails().get("rol"));
                            parameters.put("codigo", codigo.getText().toString());
                            parameters.put("nombre1", nombre1.getText().toString());
                            parameters.put("nombre2", nombre2.getText().toString());
                            parameters.put("apellido1", apellido1.getText().toString());
                            parameters.put("apellido2", apellido2.getText().toString());
                            parameters.put("email", email.getText().toString());
                            parameters.put("telefono", telefono.getText().toString());
                            parameters.put("codigo", codigo.getText().toString());
                            parameters.put("facebook", facebook.getText().toString());
                            parameters.put("twitter", twitter.getText().toString());
                            parameters.put("descripcion", descripcion.getText().toString());
                            parameters.put("semestre", semestre.getText().toString());

                            return parameters;
                        }
                    };
                    requestQueue.add(request);


                }


                break;

        }
    }
}
