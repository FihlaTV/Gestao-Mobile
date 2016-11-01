package com.gestao.udec.gestao_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    String observaciones="",clase="",profesor="",aula_c="",hora_iniciof="",hora_finalf="";
    RequestQueue requestQueue;
    TextView tvfecha, tvhora, tvaula, tvclase_actual,tvclasess;
    String aula, hora, minuto;
    String scan = "http://192.168.1.66/gestao/mobile/escanear.php";
    String scan1 = "http://192.168.1.66/gestao/mobile/escanear_mis_clases.php";
    String scan2 = "http://192.168.1.66/gestao/mobile/escanear_mi_clase.php";
    RelativeLayout rlclasem;
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
        tvclasess = (TextView) findViewById(R.id.tvclasess);
        rlclasem = (RelativeLayout) findViewById(R.id.rlclasem);
        rlclasem.setVisibility(View.INVISIBLE);
        tvclase_actual= (TextView) findViewById(R.id.tvclase_actual);

        tlclase = (TableLayout) findViewById(R.id.tlclases);

        trclase = (TableRow)  findViewById(R.id.trclase);
        tvclase_actual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EscanearActivity.this);
                builder.setMessage("Clase: "+clase+"\n"+"Profesor: "+profesor+"\n"+"Sala: "+aula_c+"\n"+"Hora de inicio: "+hora_iniciof+"\n"+"Hora final:"+hora_finalf+"\n"+"Observaciones: "+observaciones)
                        .setTitle(clase)
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
                            tvhora.setText(tvhora.getText()+": "+resumen.getString("hora")+":"+resumen.getString("minuto"));
                            hora = resumen.getString("hora");
                            minuto = resumen.getString("minuto");
                        }
                    }else{

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

    protected void traermisiguienteclase(final String id, final String hora_inicio, final String hora_final, final String nombre){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request1 = new StringRequest(Request.Method.POST, scan2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>0){
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
                        rlclasem.setVisibility(View.VISIBLE);
                        tvclase_actual.setVisibility(View.VISIBLE);
                        clase=miclase.getString("materia");
                        profesor=nombre1+" "+apellido;
                        aula_c=nombre;
                        hora_iniciof=hora_inicio;
                        hora_finalf=hora_final;
                        int horaserver=Integer.parseInt(hora);
                        int horabd=Integer.parseInt(hora_inicio);
                        int horas_diferencia= horabd-horaserver;
                        int minutos_diferencia = 60-Integer.parseInt(minuto);
                        String horast;
                        if (Integer.parseInt(hora)>=Integer.parseInt(hora_inicio)){
                            horast = " ya mismo en "+nombre;
                        }
                        else{
                            if(horas_diferencia--==0){
                                horast = " en : "+String.valueOf(minutos_diferencia)+" minutos";
                            }else{
                                horast = " en : "+String.valueOf(horas_diferencia)+" horas "+String.valueOf(minutos_diferencia)+" minutos";
                            }

                        }
                        tvclase_actual.setText(miclase.getString("materia")+horast);
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

                    if(jArray.length()>0){
                        rlclasem.setLayoutParams(new TableLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
                        JSONObject miclase = jArray.getJSONObject(0);
                        if (miclase.getString("observaciones").equalsIgnoreCase("null")){
                            observaciones="no hay observaciones";
                        }else{
                            observaciones=miclase.getString("observaciones");
                        }


                            traermisiguienteclase(miclase.getString("id"),miclase.getString("hora_inicio"),miclase.getString("hora_final"),miclase.getString("nombre"));

                    }else{


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
                        tvclasess.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject clase_en_aula = jArray.getJSONObject(i);
                            if(i==0){
                               int horaserver = Integer.parseInt(hora);

                                int horabd= Integer.parseInt(clase_en_aula.getString("hora_inicio"));
                                if (horabd>horaserver){
                                    trclase = new TableRow(getApplicationContext());
                                    trclase.setId(100+i+1);
                                    trclase.setBackgroundResource(R.drawable.edittextstyle);
                                    TextView tvcol8 = new TextView(getApplicationContext());
                                    tvcol8.setId(200+i);
                                    String font_path = "fonts/Ubuntu-C.ttf";
                                    final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
                                    tvcol8.setTypeface(TF);
                                    Color.parseColor("#000000");
                                    tvcol8.setTextColor(Color.parseColor("#000000"));
                                    tvcol8.setText(" Vacio actualmente!");
                                    trclase.addView(tvcol8);
                                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    llp.setMargins(30, 30, 30, 30); // llp.setMargins(left, top, right, bottom);

                                    trclase.setLayoutParams(llp);
                                    tlclase.addView(trclase);

                                }

                           }
                            trclase = new TableRow(getApplicationContext());

                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            llp.setMargins(30, 30, 30, 30); // llp.setMargins(left, top, right, bottom);

                                                      trclase.setLayoutParams(llp);
                            trclase.setBackgroundResource(R.drawable.edittextstyle);
                            trclase.setId(100+i);
                            TextView tvcol1 = new TextView(getApplicationContext());
                            tvcol1.setCompoundDrawablesWithIntrinsicBounds(  R.mipmap.cls, 0,0, 0);
                            tvcol1.setId(200+i);
                            String font_path = "fonts/Ubuntu-C.ttf";
                            final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
                            tvcol1.setTypeface(TF);
                            Color.parseColor("#000000");
                            tvcol1.setTextColor(Color.parseColor("#000000"));
                            String nombretemp;
                            if (clase_en_aula.getString("nombre1").toString().trim().equalsIgnoreCase("null")){
                                nombretemp ="docente no encontrado!";
                            }
                            else{
                                nombretemp =clase_en_aula.getString("nombre1")+" "+clase_en_aula.getString("apellido1");
                            }
                            tvcol1.setText(" Clase: "+clase_en_aula.getString("nombre")+"\n"+" Docente: "+nombretemp+"\n"+" Hora inicio: "+clase_en_aula.getString("hora_inicio")+"\n"+" Hora final: "+clase_en_aula.getString("hora_final"));

                            if (Integer.parseInt(clase_en_aula.getString("hora_inicio"))<=Integer.parseInt(hora) && Integer.parseInt(clase_en_aula.getString("hora_final"))>Integer.parseInt(hora)){
                                tvcol1.setText(tvcol1.getText()+"\n"+" ACTUALMENTE");
                            }

                            trclase.addView(tvcol1);
                            tlclase.addView(trclase);


                        }
                    }else{
                        Toast.makeText(EscanearActivity.this, "No se encontraron clases para esta aula", Toast.LENGTH_LONG).show();
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
