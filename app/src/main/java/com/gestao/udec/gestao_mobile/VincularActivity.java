package com.gestao.udec.gestao_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

public class VincularActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "http://192.168.1.2/gestao/mobile/select_clases_vincular.php";
    String url2 = "http://192.168.1.2/gestao/mobile/insert_vinculacion.php";
    RequestQueue requestQueue;
    AutoCompleteTextView auto;
    Button vincular;
    SessionManager sesion;

    HashMap<String, String> clasesLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincular);

        sesion = new SessionManager(VincularActivity.this);
        sesion.checkLogin();

        auto = (AutoCompleteTextView) findViewById(R.id.actClase);
        vincular = (Button) findViewById(R.id.btVincular);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String font_path = "fonts/Ubuntu-C.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        auto.setTypeface(TF);

        vincular.setOnClickListener(this);

        obtenerClases();

    }

    protected void obtenerClases() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    String[] clasesNombre = new String[jArray.length()];
                    clasesLista = new HashMap<String, String>();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject clases = jArray.getJSONObject(i);


                        clasesNombre[i] = clases.getString("nombre") + " - " + clases.getString("grupo");
                        clasesLista.put(clasesNombre[i], clases.getString("id_clase"));

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(VincularActivity.this, android.R.layout.simple_dropdown_item_1line, clasesNombre);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    auto.setThreshold(2);
                    auto.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VincularActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
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
            case R.id.btVincular:
                new AlertDialog.Builder(VincularActivity.this)
                        .setTitle(getResources().getString(R.string.vinculacionClase))
                        .setMessage(String.format(getResources().getString(R.string.vinculacionAceptacion), auto.getText().toString()))
                        .setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                requestQueue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Intent intent;
                                        Toast.makeText(VincularActivity.this, response, Toast.LENGTH_LONG).show();
                                        intent = new Intent(VincularActivity.this, UserAreaActivity.class);
                                        VincularActivity.this.startActivity(intent);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(VincularActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<String, String>();
                                        parameters.put("id_clase", clasesLista.get(auto.getText().toString()));
                                        parameters.put("id_persona", sesion.getUserDetails().get("id"));
                                        parameters.put("rol", sesion.getUserDetails().get("rol"));

                                        return parameters;
                                    }
                                };
                                requestQueue.add(request);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;

        }
    }
}

