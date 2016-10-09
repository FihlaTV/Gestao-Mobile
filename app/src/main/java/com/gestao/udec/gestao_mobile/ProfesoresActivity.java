package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;

public class ProfesoresActivity extends AppCompatActivity {
    String showteachers = "http://192.168.1.4/gestao/mobile/show_teachers.php";

    String showteacheree = "http://192.168.1.4/gestao/mobile/show_teachers_e.php";
    String showteacherec = "http://192.168.1.4/gestao/mobile/select_latest_clases_e.php";
    ImageView ifacebook;
    ImageView itwiter;
    ImageView ivfoto;

    RequestQueue requestQueue;
    JSONArray jArray;
    TextView tvnombrefull, tvcorreo, tvdescrip, tvtelefono;
    AutoCompleteTextView auto;
    String id_profesor = "";
    TableLayout tlclase;
    ScrollView sv;
    TableRow trclase;
    Button btConsultar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManager sesion = new SessionManager(ProfesoresActivity.this);
        sesion.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesores);

        ifacebook = (ImageView) findViewById(R.id.ifacebook);
        itwiter = (ImageView) findViewById(R.id.itwiter);
        ivfoto = (ImageView) findViewById(R.id.ivfoto);
        auto = (AutoCompleteTextView) findViewById(R.id.actProfesor);
        btConsultar = (Button) findViewById(R.id.btconsutar);
        tvnombrefull = (TextView) findViewById(R.id.tvnombrefull);
         sv = (ScrollView)findViewById(R.id.sv);
        tvcorreo = (TextView) findViewById(R.id.tvcorreo);
        tvdescrip = (TextView) findViewById(R.id.tvdescrip);
        tvtelefono = (TextView) findViewById(R.id.tvtelefono);
        tlclase = (TableLayout) findViewById(R.id.tlclases);
        trclase = (TableRow)  findViewById(R.id.trclase);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String font_path = "fonts/Ubuntu-C.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);
        auto.setTypeface(TF);
        tvcorreo.setTypeface(TF);
        tvnombrefull.setTypeface(TF);
        btConsultar.setTypeface(TF);
        tvdescrip.setTypeface(TF);
        itwiter.setVisibility(View.INVISIBLE);
        ifacebook.setVisibility(View.INVISIBLE);
        tlclase.setVisibility(View.INVISIBLE);
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

                sv.scrollTo(0, btConsultar.getBottom());
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
        while (tlclase.getChildCount() > 1)
            tlclase.removeView(tlclase.getChildAt(tlclase.getChildCount() - 1));
        StringRequest request = new StringRequest(Request.Method.POST, showteacheree, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>0){
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject profesor = jArray.getJSONObject(i);
                            itwiter.setTag(profesor.getString("twiter"));

                            ifacebook.setTag(profesor.getString("facebook"));

                            itwiter.setVisibility(View.VISIBLE);
                            ifacebook.setVisibility(View.VISIBLE);
                            tvnombrefull.setText(profesor.getString("nombre1")+" "+profesor.getString("nombre2")+" "+profesor.getString("apellido1")+" "+profesor.getString("apellido2"));
                            tvdescrip.setText(profesor.getString("descripcion"));
                            tvcorreo.setText(profesor.getString("email"));
                            tvtelefono.setText(profesor.getString("telefono"));
                        }
                    }else{

                        Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.erroNoDocente), Toast.LENGTH_LONG).show();
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


        ////////////////////////
        StringRequest requesttwo = new StringRequest(Request.Method.POST, showteacherec, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                tlclase.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if(jArray.length()>0){

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject clases = jArray.getJSONObject(i);


                        trclase = new TableRow(getApplicationContext());
                        trclase.setId(100+i);

                        TextView tvcol1 = new TextView(getApplicationContext());
                        tvcol1.setId(200+i);

                        tvcol1.setText(clases.getString("nombre_clase"));
                        TextView tvcol2 = new TextView(getApplicationContext());
                        tvcol2.setId(200+i);
                        tvcol2.setText(clases.getString("nombre_aula"));
                        TextView tvcol3 = new TextView(getApplicationContext());
                        tvcol3.setId(200+i);
                        tvcol3.setText(clases.getString("hora_inicio") + " - "+clases.getString("hora_final"));

                        TextView tvcol5 = new TextView(getApplicationContext());
                        tvcol5.setId(200+i);
                        tvcol5.setText(clases.getString("fecha"));
                        trclase.addView(tvcol1);
                        trclase.addView(tvcol2);
                        trclase.addView(tvcol3);


                        trclase.addView(tvcol5);
                        tlclase.addView(trclase);

                    }
                    }else{
                        Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.errorNoClases), Toast.LENGTH_LONG).show();
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
        requestQueue.add(requesttwo);


        String url_photo = "http://192.168.1.4/gestao/img_profiles/"+id_profesor+".jpg";
        ImageRequest imageRequest = new ImageRequest(url_photo,

                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        CircularNetworkImageView im = new CircularNetworkImageView(getApplicationContext());
                Bitmap imgful = im.getCircularBitmap(response);

                ivfoto.setImageBitmap(imgful);
                    }
                },200,200, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(imageRequest);
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








