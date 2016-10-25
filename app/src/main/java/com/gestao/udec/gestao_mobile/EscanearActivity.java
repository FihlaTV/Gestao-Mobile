package com.gestao.udec.gestao_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.HashMap;
import java.util.Map;

public class EscanearActivity extends AppCompatActivity {
    TextView aulaNombre;
    String observaciones="";
    RequestQueue requestQueue;
    TextView tvfecha, tvhora, tvaula, tvvacio, tvclase_actual;
    String aula, hora, minuto;
    String scan = "http://192.168.1.66/Gestao/mobile/escanear.php";
    String scan1 = "http://192.168.1.66/Gestao/mobile/escanear_mis_clases.php";
    String scan2 = "http://192.168.1.66/Gestao/mobile/escanear_mi_clase.php";

    TableLayout tlclase;
    ScrollView sv;
    SessionManager sesion;
    TableRow trclase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear);
        Bundle extras = getIntent().getExtras();
        sesion = new SessionManager(EscanearActivity.this);
        sesion.checkLogin();

        aula = extras.getString("aula");
        tvhora = (TextView) findViewById(R.id.tvhora);
        tvfecha = (TextView) findViewById(R.id.tvfecha);
        tvaula = (TextView) findViewById(R.id.tvaula);
        tvvacio= (TextView) findViewById(R.id.tvvacio);
        tvclase_actual= (TextView) findViewById(R.id.tvclase_actual);
        tlclase = (TableLayout) findViewById(R.id.tlclases);

        trclase = (TableRow)  findViewById(R.id.trclase);
        tvclase_actual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EscanearActivity.this);
                builder.setMessage(observaciones)
                        .setTitle("OBSERVACIONES")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        requestQueue = Volley.newRequestQueue(getApplicationContext());
       resumen();
        requestQueue = Volley.newRequestQueue(getApplicationContext());






        clase_en_aula();


    }

    protected void resumen() {

        StringRequest request = new StringRequest(Request.Method.POST, scan, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>0){

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject resumen = jArray.getJSONObject(i);
                            tvaula.setText(tvaula.getText()+": "+aula);
                            tvfecha.setText(tvfecha.getText()+": "+resumen.getString("fecha"));
                            tvhora.setText(tvhora.getText()+": "+resumen.getString("hora"));
                           hora = resumen.getString("hora");

                            minuto = resumen.getString("minuto");
                        }
                    }else{
                        Toast.makeText(EscanearActivity.this, getResources().getString(R.string.errorNoClases), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EscanearActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("estado", "0");

                return parameters;
            }
        };
        requestQueue.add(request);

    }

    protected void traermisiguienteclase(final String id, String hora_inicio, String hora_final, final String nombre){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request1 = new StringRequest(Request.Method.POST, scan2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>=0){
                        String nombre1;
                        String apellido;
                        JSONObject miclase = jArray.getJSONObject(0);
                        if (miclase.getString("nombre1").toString().trim().equalsIgnoreCase("null")){
                            nombre1 = "";
                        }else{
                            nombre1 = miclase.getString("nombre1");
                        }
                        if (miclase.getString("apellido1").toString().trim().equalsIgnoreCase("null")){
                            apellido = "";
                        }else{
                            apellido = miclase.getString("apellido1");
                        }
                        tvclase_actual.setText(miclase.getString("materia")+" "+nombre1+" "+apellido+nombre);
                    }else{
                        tvclase_actual.setText("hoy no tienes ninguna clase!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EscanearActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id",id);

                return parameters;
            }
        };
        requestQueue.add(request1);
    }
    protected void clase_en_aula(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request1 = new StringRequest(Request.Method.POST, scan1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>=0){
                        JSONObject miclase = jArray.getJSONObject(0);
                        if (miclase.getString("observaciones").equalsIgnoreCase("null")){
                            observaciones="no hay observaciones";
                        }else{
                            observaciones=miclase.getString("observaciones");
                        }

                            traermisiguienteclase(miclase.getString("id"),miclase.getString("hora_inicio"),miclase.getString("hora_final"),miclase.getString("nombre"));

                    }else{
                        tvclase_actual.setText("hoy no tienes ninguna clase!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EscanearActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("persona",sesion.getUserDetails().get("id"));

                return parameters;
            }
        };
        requestQueue.add(request1);


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, scan, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>0){

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject clase_en_aula = jArray.getJSONObject(i);
                            if(i==0){

                            if (Integer.parseInt(hora) < Integer.parseInt(clase_en_aula.getString("hora_inicio"))){
                                tvvacio.setVisibility(View.VISIBLE);
                            }}
                            trclase = new TableRow(getApplicationContext());
                            trclase.setId(100+i);
                            TextView tvcol1 = new TextView(getApplicationContext());
                            tvcol1.setId(200+i);

                            tvcol1.setText(clase_en_aula.getString("nombre"));
                            TextView tvcol2 = new TextView(getApplicationContext());
                            tvcol2.setId(200+i);

                            if (clase_en_aula.getString("nombre1").toString().trim().equalsIgnoreCase("null")){
                                tvcol2.setText("docente no encontrado!");
                            }
                            else{
                                tvcol2.setText(clase_en_aula.getString("nombre1")+" "+clase_en_aula.getString("apellido1"));
                            }
                            TextView tvcol4 = new TextView(getApplicationContext());
                            tvcol4.setId(200+i);

                            tvcol4.setText(clase_en_aula.getString("hora_inicio"));
                            TextView tvcol5 = new TextView(getApplicationContext());
                            tvcol5.setId(200+i);

                            tvcol5.setText(clase_en_aula.getString("hora_final"));
                            trclase.addView(tvcol1);
                            trclase.addView(tvcol2);
                            trclase.addView(tvcol4);
                            trclase.addView(tvcol5);
                            tlclase.addView(trclase);
                            if (Integer.parseInt(clase_en_aula.getString("hora_inicio"))<=Integer.parseInt(hora) && Integer.parseInt(clase_en_aula.getString("hora_final"))>Integer.parseInt(hora)){
                                TextView tvcol3 = new TextView(getApplicationContext());
                                tvcol3.setId(200+i);
                                tvcol3.setText("actualmente");
                                trclase.addView(tvcol3);
                            }

                        }
                    }else{
                        Toast.makeText(EscanearActivity.this, getResources().getString(R.string.errorNoClases), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EscanearActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("estado", "1");
                parameters.put("aula", aula);
                return parameters;
            }
        };
        requestQueue.add(request);
    }
}
