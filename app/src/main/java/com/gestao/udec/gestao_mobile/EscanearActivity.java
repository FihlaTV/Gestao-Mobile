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
import android.view.animation.AnimationUtils;
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
    String observaciones = "", clase = "", profesor = "", aula_c = "", hora_iniciof = "", hora_finalf = "";
    RequestQueue requestQueue;
    TextView tvfecha, tvhora, tvaula, tvclase_actual, tvclasess;
    String aula, hora, minuto;
    String scan = "http://gestao-web.co/escanear.php";
    String scan1 = "http://gestao-web.co/escanear_mis_clases.php";
    String scan2 = "http://gestao-web.co/escanear_mi_clase.php";
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
        tvclase_actual = (TextView) findViewById(R.id.tvclase_actual);

        tlclase = (TableLayout) findViewById(R.id.tlclases);

        trclase = (TableRow) findViewById(R.id.trclase);
        tvclase_actual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click));
                AlertDialog.Builder builder = new AlertDialog.Builder(EscanearActivity.this);
                builder.setMessage(getResources().getString(R.string.clase) + ": " + clase + "\n" + getResources().getString(R.string.profesor) + ": " + profesor + "\n" + getResources().getString(R.string.aula) + ": " + aula_c + "\n" + getResources().getString(R.string.horaInicio) + ": " + hora_iniciof + "\n" + getResources().getString(R.string.horaFinal) + ":" + hora_finalf + "\n" + getResources().getString(R.string.observaciones) + ": " + observaciones)
                        .setTitle(clase)
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.aceptar),
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

                    if (jArray.length() > 0) {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject resumen = jArray.getJSONObject(i);
                            tvaula.setText(tvaula.getText() + ": " + aula);
                            tvfecha.setText(tvfecha.getText() + ": " + resumen.getString("fecha"));
                            tvhora.setText(tvhora.getText() + ": " + resumen.getString("hora") + ":" + resumen.getString("minuto"));
                            hora = resumen.getString("hora");
                            minuto = resumen.getString("minuto");
                        }
                    } else {

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

    protected void traermisiguienteclase(final String id, final String hora_inicio, final String hora_final, final String nombre) {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request1 = new StringRequest(Request.Method.POST, scan2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if (jArray.length() > 0) {
                        String nombre1;

                        String apellido;
                        JSONObject miclase = jArray.getJSONObject(0);
                        if (miclase.getString("nombre1").toString().trim().equalsIgnoreCase("null")) {
                            nombre1 = "";
                        } else {
                            nombre1 = miclase.getString("nombre1");
                        }
                        if (miclase.getString("apellido1").toString().trim().equalsIgnoreCase("null")) {
                            apellido = "";
                        } else {
                            apellido = miclase.getString("apellido1");
                        }
                        rlclasem.setVisibility(View.VISIBLE);
                        tvclase_actual.setVisibility(View.VISIBLE);
                        clase = miclase.getString("materia");
                        profesor = nombre1 + " " + apellido;
                        aula_c = nombre;
                        hora_iniciof = hora_inicio;
                        hora_finalf = hora_final;
                        int horaserver = Integer.parseInt(hora);
                        int horabd = Integer.parseInt(hora_inicio);
                        int horas_diferencia = horabd - horaserver;
                        int minutos_diferencia = 60 - Integer.parseInt(minuto);
                        String horast;
                        if (Integer.parseInt(hora) >= Integer.parseInt(hora_inicio)) {
                            horast = getResources().getString(R.string.yaMismoEn) + nombre;
                        } else {
                            if (horas_diferencia-- == 0) {
                                horast = getResources().getString(R.string.en) + ": " + String.valueOf(minutos_diferencia) + " " + getResources().getString(R.string.minutos);
                            } else {
                                horast = getResources().getString(R.string.en) + ": " + String.valueOf(horas_diferencia) + " " + getResources().getString(R.string.horasMin) + " "+ String.valueOf(minutos_diferencia) + " " + getResources().getString(R.string.minutos);
                            }

                        }
                        tvclase_actual.setText(miclase.getString("materia") + " " +horast);
                    } else {
                        tvclase_actual.setText(getResources().getString(R.string.hoyNoTienesNingunaClase));
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
                parameters.put("id", id);

                return parameters;
            }
        };
        requestQueue.add(request1);
    }

    protected void clase_en_aula() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request1 = new StringRequest(Request.Method.POST, scan1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if (jArray.length() > 0) {
                        rlclasem.setLayoutParams(new TableLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
                        JSONObject miclase = jArray.getJSONObject(0);
                        if (miclase.getString("observaciones").equalsIgnoreCase("null")) {
                            observaciones = getResources().getString(R.string.noHayObservaciones);
                        } else {
                            observaciones = miclase.getString("observaciones");
                        }


                        traermisiguienteclase(miclase.getString("id"), miclase.getString("hora_inicio"), miclase.getString("hora_final"), miclase.getString("nombre"));

                    } else {


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
                parameters.put("persona", sesion.getUserDetails().get("id"));
                parameters.put("rol", sesion.getUserDetails().get("rol"));
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

                    if (jArray.length() > 0) {
                        tvclasess.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject clase_en_aula = jArray.getJSONObject(i);
                            if (i == 0) {
                                int horaserver = Integer.parseInt(hora);

                                int horabd = Integer.parseInt(clase_en_aula.getString("hora_inicio"));
                                if (horabd > horaserver) {
                                    trclase = new TableRow(getApplicationContext());
                                    trclase.setId(100 + i + 1);
                                    trclase.setBackgroundResource(R.drawable.edittextstyle);

                                    TextView tvcol8 = new TextView(getApplicationContext());
                                    tvcol8.setId(200 + i);
                                    tvcol8.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dispon, 0, 0, 0);
                                    String font_path = "fonts/Ubuntu-C.ttf";
                                    final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
                                    tvcol8.setTypeface(TF);
                                    Color.parseColor("#000000");
                                    tvcol8.setTextColor(Color.parseColor("#000000"));
                                    tvcol8.setText(getResources().getString(R.string.vacioActualmente));
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
                            trclase.setId(100 + i);
                            TextView tvcol1 = new TextView(getApplicationContext());
                            tvcol1.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.cls, 0, 0, 0);
                            tvcol1.setId(200 + i);
                            String font_path = "fonts/Ubuntu-C.ttf";
                            final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
                            tvcol1.setTypeface(TF);
                            Color.parseColor("#000000");
                            tvcol1.setTextColor(Color.parseColor("#000000"));
                            String nombretemp;
                            if (clase_en_aula.getString("nombre1").toString().trim().equalsIgnoreCase("null")) {
                                nombretemp = getResources().getString(R.string.docenteNoEncontrado);
                            } else {
                                nombretemp = clase_en_aula.getString("nombre1") + " " + clase_en_aula.getString("apellido1");
                            }
                            tvcol1.setText(getResources().getString(R.string.clase) + ": " + clase_en_aula.getString("nombre") + "\n" + getResources().getString(R.string.profesor) + ": " + nombretemp + "\n" + getResources().getString(R.string.horaInicio) + ": " + clase_en_aula.getString("hora_inicio") + "\n" + getResources().getString(R.string.horaFinal) + ": " + clase_en_aula.getString("hora_final"));

                            if (Integer.parseInt(clase_en_aula.getString("hora_inicio")) <= Integer.parseInt(hora) && Integer.parseInt(clase_en_aula.getString("hora_final")) > Integer.parseInt(hora)) {
                                tvcol1.setText(tvcol1.getText() + "\n" + getResources().getString(R.string.actualmenteMay));
                            }

                            trclase.addView(tvcol1);
                            tlclase.addView(trclase);


                        }
                    } else {
                        Toast.makeText(EscanearActivity.this, getResources().getString(R.string.noSeEncontraronClasesParaEstaAula), Toast.LENGTH_LONG).show();
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
