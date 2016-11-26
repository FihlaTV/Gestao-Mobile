package com.gestao.udec.gestao_mobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class SalasSeleccionActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "http://gestao-web.co/select_aulas.php";
    TextView escogerSalas;
    Spinner salasSpinner;
    Button mostrarHorario;
    String[] arregloSalas;
    HashMap<String, String> salasId;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas_seleccion);

        SessionManager sesion = new SessionManager(SalasSeleccionActivity.this);
        sesion.checkLogin();

        escogerSalas = (TextView) findViewById(R.id.tvEscogerSala);
        salasSpinner = (Spinner) findViewById(R.id.spSalas);
        mostrarHorario = (Button) findViewById(R.id.btHorarioSalas);

        mostrarHorario.setOnClickListener(this);
        String font_path = "fonts/Ubuntu-C.ttf";
        final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);

        escogerSalas.setTypeface(TF);
        mostrarHorario.setTypeface(TF);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        obtenerSalas();




    }

    private void obtenerSalas() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    arregloSalas = new String[jArray.length()];
                    salasId = new HashMap<String, String>();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject e = jArray.getJSONObject(i);
                        arregloSalas[i] = e.getString("nombre");
                        salasId.put(e.getString("nombre"), e.getString("id"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SalasSeleccionActivity.this, android.R.layout.simple_spinner_dropdown_item, arregloSalas) {
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            ((TextView) v).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-C.ttf"));

                            return v;
                        }


                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View v = super.getDropDownView(position, convertView, parent);
                            ((TextView) v).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-C.ttf"));

                            return v;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    salasSpinner.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SalasSeleccionActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btHorarioSalas:
                Intent intent = new Intent(SalasSeleccionActivity.this, SalasHorarioActivity.class);
                intent.putExtra("sala",salasId.get(salasSpinner.getSelectedItem()));
                SalasSeleccionActivity.this.startActivity(intent);
                break;
        }
    }
}
