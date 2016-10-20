package com.gestao.udec.gestao_mobile;

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
    RequestQueue requestQueue;
    TextView tvfecha, tvhora, tvaula;
    String aula, hora, minuto;
    String scan = "http://192.168.1.66/gestao/mobile/escanear.php";
    TableLayout tlclase;
    ScrollView sv;
    TableRow trclase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear);
        Bundle extras = getIntent().getExtras();
        aula = extras.getString("aula");
        tvhora = (TextView) findViewById(R.id.tvhora);
        tvfecha = (TextView) findViewById(R.id.tvfecha);
        tvaula = (TextView) findViewById(R.id.tvaula);
        tlclase = (TableLayout) findViewById(R.id.tlclases);
        trclase = (TableRow)  findViewById(R.id.trclase);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
       resumen();
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
    protected void clase_en_aula(){

        StringRequest request = new StringRequest(Request.Method.POST, scan, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>0){

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject clase_en_aula = jArray.getJSONObject(i);

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

                            trclase.addView(tvcol1);
                            trclase.addView(tvcol2);
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
