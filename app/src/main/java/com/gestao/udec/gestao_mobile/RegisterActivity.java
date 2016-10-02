package com.gestao.udec.gestao_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.5/gestao/mobile/register_person.php";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText etnombre1 = (EditText) findViewById(R.id.etNombre1);
        final EditText etapellido1 = (EditText) findViewById(R.id.etApellido1);
        final EditText etcorreo = (EditText) findViewById(R.id.etCorreo);
        final EditText etclave = (EditText) findViewById(R.id.etClave);
        final RadioButton rbestudiante = (RadioButton) findViewById(R.id.rbEstudiante);
        final RadioButton rbdocente = (RadioButton) findViewById(R.id.rbDocente);
        final Button btnregistro = (Button) findViewById(R.id.btnRegistro);
    requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this,"registro exitoso",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,"ha ocurrido un error",Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String,String> parameters = new HashMap<String, String>();
                        parameters.put("nombre1",etnombre1.getText().toString() );
                        parameters.put("apellido1",etapellido1.getText().toString() );
                        parameters.put("password",etclave.getText().toString() );
                        parameters.put("correo",etcorreo.getText().toString() );
                        if (rbestudiante.isChecked()){
                            parameters.put("rol","E");
                        }else{
                            parameters.put("rol","D");
                        }
                        return parameters;
                    }
                };
                requestQueue.add(request);
            }

        });

    }
}
