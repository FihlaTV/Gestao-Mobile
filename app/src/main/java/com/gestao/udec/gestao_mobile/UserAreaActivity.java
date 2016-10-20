package com.gestao.udec.gestao_mobile;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
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


public class UserAreaActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "http://192.168.1.66/gestao/mobile/verificacion_datos_basicos.php";
    Button escanear;
    Button vincular;
    Button horario;
    Button profesores;
    Button perfil;


    SessionManager sesion;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);


        sesion = new SessionManager(UserAreaActivity.this);
        sesion.checkLogin();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        verificacionPerfil(sesion.getUserDetails().get("rol"));

        escanear = (Button) findViewById(R.id.btescanear);
        vincular = (Button) findViewById(R.id.btvincular);
        horario = (Button) findViewById(R.id.bthorario);
        profesores = (Button) findViewById(R.id.btprofesores);
        perfil = (Button) findViewById(R.id.btPerfil);

        String font_path = "fonts/Ubuntu-C.ttf";
        final Typeface TF = Typeface.createFromAsset(getAssets(),font_path);

        escanear.setTypeface(TF);
        vincular.setTypeface(TF);
        horario.setTypeface(TF);
        profesores.setTypeface(TF);
        perfil.setTypeface(TF);

        escanear.setOnClickListener(this);
        vincular.setOnClickListener(this);
        horario.setOnClickListener(this);
        profesores.setOnClickListener(this);
        perfil.setOnClickListener(this);

    }

    private void verificacionPerfil(final String rol) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject clases = jArray.getJSONObject(i);
                        if (clases.getString("permiso").equals("0")) {
                            Intent intent;
                            intent = new Intent(UserAreaActivity.this, PerfilActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            UserAreaActivity.this.startActivity(intent);

                        } else if (rol.equals("D")) {
                            Intent intent;
                            intent = new Intent(UserAreaActivity.this, TeacherAreaActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            UserAreaActivity.this.startActivity(intent);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserAreaActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_persona", sesion.getUserDetails().get("id"));
                parameters.put("rol", rol);
                return parameters;
            }
        };
        requestQueue.add(request);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btescanear:
                intent = new Intent(UserAreaActivity.this, ARSimple.class);
                UserAreaActivity.this.startActivity(intent);
                break;
            case R.id.bthorario:
                intent = new Intent(UserAreaActivity.this, HorarioActivity.class);
                UserAreaActivity.this.startActivity(intent);
                break;
            case R.id.btvincular:
                intent = new Intent(UserAreaActivity.this, VincularActivity.class);
                UserAreaActivity.this.startActivity(intent);
                break;
            case R.id.btprofesores:
                intent = new Intent(UserAreaActivity.this, ProfesoresActivity.class);
                UserAreaActivity.this.startActivity(intent);
                break;
            case R.id.btPerfil:
                intent = new Intent(UserAreaActivity.this, PerfilActivity.class);
                UserAreaActivity.this.startActivity(intent);
                break;

        }
    }
}