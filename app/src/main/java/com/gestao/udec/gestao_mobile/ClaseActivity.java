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

public class ClaseActivity extends AppCompatActivity implements View.OnClickListener {


    EditText idClase;
    EditText nombreClase;
    EditText grupo;
    EditText creditos;
    EditText semestre;
    EditText cantEstudiantes;
    EditText requerimientos;

    Spinner diaSemana;
    Spinner ocurrencias;
    EditText horaInicial;
    EditText horaFinal;
    SessionManager sesion;
    Button reservar;

    HashMap<String, String> arregloClases;

    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.66/gestao/mobile/insert_clase.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase);

        sesion = new SessionManager(ClaseActivity.this);
        sesion.checkLogin();


        idClase= (EditText) findViewById(R.id.etCodigoClase);
        nombreClase= (EditText) findViewById(R.id.etNombreClase);
        grupo= (EditText) findViewById(R.id.etGrupo);
        creditos= (EditText) findViewById(R.id.etNumCreditos);
        semestre= (EditText) findViewById(R.id.etSemestre);
        cantEstudiantes= (EditText) findViewById(R.id.etCantEstudiantes);
        requerimientos= (EditText) findViewById(R.id.etRequerimientos);

        diaSemana = (Spinner) findViewById(R.id.spDiaSemana);
        ocurrencias = (Spinner) findViewById(R.id.spOcurrencias);
        horaInicial = (EditText) findViewById(R.id.etHoraInicial);
        horaFinal = (EditText) findViewById(R.id.etHoraFinal);
        reservar = (Button) findViewById(R.id.btCrear);

        reservar.setOnClickListener(this);

        String[] diasSemana = {getResources().getString(R.string.lunes), getResources().getString(R.string.martes), getResources().getString(R.string.miercoles), getResources().getString(R.string.jueves), getResources().getString(R.string.viernes), getResources().getString(R.string.sabado)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, diasSemana);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaSemana.setAdapter(adapter);

        String[] aOcurrencias = {getResources().getString(R.string.unaSolaVez),getResources().getString(R.string.todoElSemestre)};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, aOcurrencias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ocurrencias.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCrear:

                requestQueue = Volley.newRequestQueue(getApplicationContext());


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(horaInicial.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(horaFinal.getWindowToken(), 0);

                boolean estado = true;


                if(idClase.getText().toString().isEmpty()){
                    idClase.setError(getResources().getString(R.string.campoNoNulo));
                    estado = false;
                }
                if(nombreClase.getText().toString().isEmpty()){
                    nombreClase.setError(getResources().getString(R.string.campoNoNulo));
                    estado = false;
                }
                if(grupo.getText().toString().isEmpty()){
                    grupo.setError(getResources().getString(R.string.campoNoNulo));
                    estado = false;
                }
                try {
                    if (Integer.parseInt(cantEstudiantes.getText().toString()) < 1 || Integer.parseInt(cantEstudiantes.getText().toString()) >= 100) {
                        estado = false;
                        cantEstudiantes.setError(getResources().getString(R.string.cantidadNoValida));
                    }
                } catch (NumberFormatException e) {
                    estado = false;
                    cantEstudiantes.setError(getResources().getString(R.string.cantidadNoValida));
                }

                if(!semestre.getText().toString().isEmpty()){
                    try {
                        if (Integer.parseInt(semestre.getText().toString()) < 1 || Integer.parseInt(semestre.getText().toString()) >= 13) {
                            estado = false;
                            semestre.setError(getResources().getString(R.string.cantidadNoValida));
                        }
                    } catch (NumberFormatException e) {
                        estado = false;
                        semestre.setError(getResources().getString(R.string.cantidadNoValida));
                    }
                }
                if(!creditos.getText().toString().isEmpty()){
                    try {
                        if (Integer.parseInt(creditos.getText().toString()) < 1 || Integer.parseInt(creditos.getText().toString()) > 18) {
                            estado = false;
                            creditos.setError(getResources().getString(R.string.cantidadNoValida));
                        }
                    } catch (NumberFormatException e) {
                        estado = false;
                        creditos.setError(getResources().getString(R.string.cantidadNoValida));
                    }
                }

                try {
                    if (Integer.parseInt(horaInicial.getText().toString()) < 7 || Integer.parseInt(horaInicial.getText().toString()) >= 22) {
                        estado = false;
                        horaInicial.setError(getResources().getString(R.string.horaInicialIntervalo));
                    }
                } catch (NumberFormatException e) {
                    estado = false;
                    horaInicial.setError(getResources().getString(R.string.horaInicialIntervalo));
                }
                try {
                    if (Integer.parseInt(horaFinal.getText().toString()) <= 7 || Integer.parseInt(horaFinal.getText().toString()) > 22) {
                        estado = false;
                        horaFinal.setError(getResources().getString(R.string.horaFinalIntervalo));
                    }
                } catch (NumberFormatException e) {
                    estado = false;
                    horaFinal.setError(getResources().getString(R.string.horaInicialIntervalo));
                }


                if (estado == true) {

                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Intent intent;
                            Toast.makeText(ClaseActivity.this, response, Toast.LENGTH_LONG).show();
                            intent = new Intent(ClaseActivity.this, TeacherAreaActivity.class);
                            ClaseActivity.this.startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ClaseActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("id_clase", idClase.getText().toString());
                            parameters.put("nombre_clase", nombreClase.getText().toString());
                            parameters.put("grupo", grupo.getText().toString());
                            parameters.put("creditos", creditos.getText().toString());
                            parameters.put("semestre", semestre.getText().toString());
                            parameters.put("cantidad_estudiantes", cantEstudiantes.getText().toString());
                            parameters.put("requerimientos", requerimientos.getText().toString());
                            parameters.put("id_persona", sesion.getUserDetails().get("id"));
                            parameters.put("dia_semana", String.valueOf(diaSemana.getSelectedItemPosition()));
                            parameters.put("ocurrencias", String.valueOf(ocurrencias.getSelectedItemPosition()));
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
