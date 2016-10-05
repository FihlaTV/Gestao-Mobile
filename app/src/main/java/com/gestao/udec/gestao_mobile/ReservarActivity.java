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
import android.widget.Spinner;
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

public class ReservarActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner clase;
    Spinner diaSemana;
    EditText horaInicial;
    EditText horaFinal;
    SessionManager sesion;
    Button reservar;

    HashMap<String,String> arregloClases;

    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.4/gestao/mobile/select_clases.php";
    String insertUrl2 = "http://192.168.1.4/gestao/mobile/insert_clase_aula_horario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        sesion = new SessionManager(ReservarActivity.this);
        sesion.checkLogin();

        clase = (Spinner) findViewById(R.id.spClase);
        diaSemana = (Spinner) findViewById(R.id.spDiaSemana);
        horaInicial = (EditText) findViewById(R.id.etHoraInicial);
        horaFinal = (EditText) findViewById(R.id.etHoraFinal);
        reservar = (Button) findViewById(R.id.btReservar);

        reservar.setOnClickListener(this);

        String[] diasSemana = {getResources().getString(R.string.lunes), getResources().getString(R.string.martes), getResources().getString(R.string.miercoles), getResources().getString(R.string.jueves), getResources().getString(R.string.viernes), getResources().getString(R.string.sabado)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, diasSemana);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaSemana.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        obtenerClases();
    }

    protected void obtenerClases() {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    String[] clasesNombres  = new String[jArray.length()];
                    arregloClases= new HashMap<String, String>();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject e = jArray.getJSONObject(i);
                        clasesNombres[i] = e.getString("nombre");
                        arregloClases.put(e.getString("nombre"),e.getString("id_clase"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReservarActivity.this, android.R.layout.simple_spinner_dropdown_item, clasesNombres);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clase.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReservarActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_persona", sesion.getUserDetails().get("id"));

                return parameters;
            }
        };
        requestQueue.add(request);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btReservar:

                requestQueue = Volley.newRequestQueue(getApplicationContext());



                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(horaInicial.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(horaFinal.getWindowToken(), 0);

                boolean estado = true;


                if (Integer.parseInt(horaInicial.getText().toString())<7 || Integer.parseInt(horaInicial.getText().toString())>=22) {
                    estado=false;
                    horaInicial.setError(getResources().getString(R.string.horaInicialIntervalo));
                }
                if (Integer.parseInt(horaFinal.getText().toString())<=7 || Integer.parseInt(horaFinal.getText().toString())>22) {
                    estado=false;
                    horaFinal.setError(getResources().getString(R.string.horaFinalIntervalo));
                }


                if (estado == true) {

                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl2, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Intent intent;
                            Toast.makeText(ReservarActivity.this,response , Toast.LENGTH_LONG).show();
                            intent = new Intent(ReservarActivity.this, TeacherAreaActivity.class);
                            ReservarActivity.this.startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ReservarActivity.this,getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("id_clase", arregloClases.get(clase.getSelectedItem()));
                            parameters.put("id_persona", sesion.getUserDetails().get("id"));
                            parameters.put("dia_semana", String.valueOf(diaSemana.getSelectedItemPosition()));
                            parameters.put("hora_inicio", horaInicial.getText().toString());
                            parameters.put("hora_final", horaFinal.getText().toString());

                            return parameters;
                        }
                    };
                    requestQueue.add(request);


                }




                break;

        }
    }
}
