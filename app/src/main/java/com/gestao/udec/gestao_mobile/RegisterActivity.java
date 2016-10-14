package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class RegisterActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.2/gestao/mobile/register_person.php";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText etnombre1 = (EditText) findViewById(R.id.etNombre1);
        final EditText etapellido1 = (EditText) findViewById(R.id.etApellido1);
        final EditText etcorreo = (EditText) findViewById(R.id.etCorreo);
        final EditText etclave = (EditText) findViewById(R.id.etClave);
        final EditText etclave2 = (EditText) findViewById(R.id.etClave2);
        final RadioButton rbestudiante = (RadioButton) findViewById(R.id.rbEstudiante);
        final RadioButton rbdocente = (RadioButton) findViewById(R.id.rbDocente);
        final Button btnregistro = (Button) findViewById(R.id.btnRegistro);




    requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(etnombre1.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(etapellido1.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(etclave.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(etclave2.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(etcorreo.getWindowToken(), 0);
                boolean estado = true;


                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                String email = etcorreo.getText().toString();

                Matcher mather = pattern.matcher(email);

                if (mather.find() != true) {
                    etcorreo.setError(getResources().getString(R.string.correoInvalido));
                    estado = false;
                }



                if (etnombre1.getText().toString().trim().equalsIgnoreCase("")) {
                    etnombre1.setError(getResources().getString(R.string.campoNoNulo));
                }
                if (etapellido1.getText().toString().trim().equalsIgnoreCase("")) {
                    etapellido1.setError(getResources().getString(R.string.campoNoNulo));
                }
                if (!etclave.getText().toString().equals(etclave2.getText().toString())) {
                    etclave2.setError(getResources().getString(R.string.claveNoIgual));
                    estado = false;
                }

                if (etclave2.length() < 7 || etclave.length() < 7) {
                    etclave2.setError(getResources().getString(R.string.claveNoCaracter));
                }

                if (estado == true) {

                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray jArray = jsonResponse.getJSONArray("response");
                                JSONObject id = jArray.getJSONObject(0);
                                if (id.getString("estado").equals("Registro Exitoso") && rbestudiante.isChecked()){


                                    SessionManager sesion = new SessionManager(RegisterActivity.this);
                                    sesion.createLoginSession(etnombre1.getText().toString(),etcorreo.getText().toString(),id.getString("id"),"E");
                                    Intent intent = new Intent(RegisterActivity.this, UserAreaActivity.class);

                                    RegisterActivity.this.startActivity(intent);
                                    //Toast.makeText(RegisterActivity.this,"aqui me voy al user activity" , Toast.LENGTH_LONG).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this,getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("nombre1", etnombre1.getText().toString());
                            parameters.put("apellido1", etapellido1.getText().toString());
                            parameters.put("password", etclave.getText().toString());
                            parameters.put("correo", etcorreo.getText().toString());
                            if (rbestudiante.isChecked()) {
                                parameters.put("rol", "E");
                            } else {
                                parameters.put("rol", "D");
                            }
                            return parameters;
                        }
                    };
                    requestQueue.add(request);


                }

            }
        }
        );

    }

}

