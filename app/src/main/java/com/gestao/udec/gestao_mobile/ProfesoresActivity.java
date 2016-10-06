package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    String showteachers = "http://192.168.1.4/gestao/mobile/show_teachers.php";
    String showteacheree = "http://192.168.1.4/gestao/mobile/show_teachers_e.php";
    ImageView ifacebook;
    ImageView itwiter;
    RequestQueue requestQueue;
    JSONArray jArray;
    TextView tvnombrefull;
    AutoCompleteTextView auto;
    String id_profesor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManager sesion = new SessionManager(ProfesoresActivity.this);
        sesion.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesores);
        ifacebook = (ImageView) findViewById(R.id.ifacebook);
        itwiter = (ImageView) findViewById(R.id.itwiter);
        auto = (AutoCompleteTextView) findViewById(R.id.actProfesor);
        Button btConsultar = (Button) findViewById(R.id.btconsutar);
        tvnombrefull = (TextView) findViewById(R.id.tvnombrefull);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String font_path = "fonts/Ubuntu-C.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);
        auto.setTypeface(TF);
        tvnombrefull.setTypeface(TF);
        itwiter.setVisibility(View.INVISIBLE);
        ifacebook.setVisibility(View.INVISIBLE);
        obtener_profesores();


        itwiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = (String)itwiter.getTag();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse(url));

                startActivity(intent);

            }
        });
        ifacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = (String)ifacebook.getTag();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse(url));

                startActivity(intent);

            }
        });


        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(auto.getWindowToken(), 0);


            String profesor = auto.getText().toString();
              String[]  profesornombre = profesor.split(" ");
                for (int i = 0; i < jArray.length(); i++) {
                    try {
                        JSONObject profesores = jArray.getJSONObject(i);

                        if ((profesores.getString("nombre").equals(profesornombre[0])) && (profesores.getString("apellido").equals(profesornombre[1]))){
                          id_profesor = profesores.getString("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (id_profesor==""){
                    itwiter.setVisibility(View.INVISIBLE);
                    ifacebook.setVisibility(View.INVISIBLE);
                    Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.errorDocente), Toast.LENGTH_LONG).show();
                }else{


obtener_profesor();




            }}


        });

}


    protected void obtener_profesor() {
        StringRequest request = new StringRequest(Request.Method.POST, showteacheree, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject profesor = jArray.getJSONObject(i);
                        itwiter.setTag(profesor.getString("twiter"));
                        ifacebook.setTag(profesor.getString("facebook"));
                        itwiter.setVisibility(View.VISIBLE);
                        ifacebook.setVisibility(View.VISIBLE);
                        tvnombrefull.setText(profesor.getString("nombre1")+" "+profesor.getString("nombre2")+" "+profesor.getString("apellido1")+" "+profesor.getString("apellido2"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_profesor", id_profesor);

                return parameters;
            }
        };
        requestQueue.add(request);
    }



    protected void obtener_profesores() {
        StringRequest request = new StringRequest(Request.Method.POST, showteachers, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    jArray = jsonResponse.getJSONArray("response");
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








