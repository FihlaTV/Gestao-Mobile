package com.gestao.udec.gestao_mobile;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfesoresActivity extends AppCompatActivity {
    String showteachers = "http://192.168.1.7/gestao/mobile/show_teachers.php";
    RequestQueue requestQueue;

    AutoCompleteTextView auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SessionManager sesion = new SessionManager(ProfesoresActivity.this);
        sesion.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesores);





        auto = (AutoCompleteTextView) findViewById(R.id.actProfesor);
        Button btConsultar = (Button) findViewById(R.id.btconsutar);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String font_path = "fonts/Ubuntu-C.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);
        auto.setTypeface(TF);


obtener_profesores();

}


    protected void obtener_profesores() {
        StringRequest request = new StringRequest(Request.Method.POST, showteachers, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    String[] profesoresNombre  = new String[jArray.length()];

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject profesores = jArray.getJSONObject(i);


                                profesoresNombre[i] = profesores.getString("nombre")+" "+profesores.getString("apellido");


                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfesoresActivity.this, android.R.layout.simple_dropdown_item_1line, profesoresNombre);
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
                Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }
}








