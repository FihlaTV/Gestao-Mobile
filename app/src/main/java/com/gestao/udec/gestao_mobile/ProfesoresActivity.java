package com.gestao.udec.gestao_mobile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    String showteachers = "http://192.168.1.66/gestao/mobile/show_teachers.php";

    String showteacheree = "http://192.168.1.66/gestao/mobile/show_teachers_e.php";
    String showteacherec = "http://192.168.1.66/gestao/mobile/select_latest_clases_e.php";
    ImageView ifacebook;
    ImageView itwiter;
    ImageView ivfoto;
    private ListView lvclasesd;

    RequestQueue requestQueue;
    JSONArray jArray;
    TextView tvnombrefull, tvcorreo, tvdescrip, tvtelefono,tvpersonal,tvclasesd;
    AutoCompleteTextView auto;
    String id_profesor = "";
    TableLayout tlclase;
    ScrollView sv;
    TableRow trclase;

    Button btConsultar;
    RelativeLayout inf_per;
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
        sv = (ScrollView) findViewById(R.id.sv);
        tvcorreo = (TextView) findViewById(R.id.tvcorreo);
        tvpersonal = (TextView) findViewById(R.id.tvpersonal);
        tvdescrip = (TextView) findViewById(R.id.tvdescrip);
        tvclasesd = (TextView) findViewById(R.id.tvclasesd);
        inf_per = (RelativeLayout) findViewById(R.id.inf_per);
        tvtelefono = (TextView) findViewById(R.id.tvtelefono);
        tlclase = (TableLayout) findViewById(R.id.tlclases);
        trclase = (TableRow) findViewById(R.id.trclase);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String font_path = "fonts/Ubuntu-C.ttf";
        final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        auto.setTypeface(TF);
        tvcorreo.setTypeface(TF);
        tvnombrefull.setTypeface(TF);
        btConsultar.setTypeface(TF);
        tvdescrip.setTypeface(TF);
        tvpersonal.setTypeface(TF);
        tvclasesd.setTypeface(TF);
        inf_per.setVisibility(View.INVISIBLE);
        tlclase.setVisibility(View.INVISIBLE);
        tvpersonal.setVisibility(View.INVISIBLE);
        tvclasesd.setVisibility(View.INVISIBLE);
        obtener_profesores();


        itwiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = (String) itwiter.getTag();
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click));
                if (url.equals("")){
                    Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.docenteNoHaRegistradoRecurso), Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }

            }
        });
        ifacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click));
                String url = (String) ifacebook.getTag();
                if (url.equals("")){
                    Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.docenteNoHaRegistradoRecurso), Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }




            }
        });


        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sv.scrollTo(0, btConsultar.getBottom());
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(auto.getWindowToken(), 0);


                String profesor = auto.getText().toString();
                String[] profesornombre = profesor.split(" ");
                for (int i = 0; i < jArray.length(); i++) {
                    try {
                        JSONObject profesores = jArray.getJSONObject(i);

                        if ((profesores.getString("nombre").equals(profesornombre[0])) && (profesores.getString("apellido").equals(profesornombre[1]))) {
                            id_profesor = profesores.getString("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (id_profesor == "") {
                    itwiter.setVisibility(View.INVISIBLE);
                    ifacebook.setVisibility(View.INVISIBLE);
                    Toast.makeText(ProfesoresActivity.this, getResources().getString(R.string.errorDocente), Toast.LENGTH_LONG).show();
                } else {


                    obtener_profesor();


                }
            }


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

                    if (jArray.length() > 0) {
                        for (int i = 0; i < jArray.length(); i++) {
                            tvpersonal.setVisibility(View.VISIBLE);
                            inf_per.setVisibility(View.VISIBLE);
                            JSONObject profesor = jArray.getJSONObject(i);
                            itwiter.setTag(profesor.getString("twiter"));

                            ifacebook.setTag(profesor.getString("facebook"));


                            tvnombrefull.setText(profesor.getString("nombre1") + " " + profesor.getString("nombre2") + " " + profesor.getString("apellido1") + " " + profesor.getString("apellido2"));
                            tvdescrip.setText(profesor.getString("descripcion"));
                            tvcorreo.setText(profesor.getString("email"));
                            tvtelefono.setText(profesor.getString("telefono"));
                        }
                    } else {

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
            String clases_nombre[];
            @Override
            public void onResponse(String response) {
                tlclase.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");

                    if (jArray.length() >= 0) {
                        tvclasesd.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject clases = jArray.getJSONObject(i);
                            trclase = new TableRow(getApplicationContext());
                            trclase.setId(100 + i);
                            TextView tvcol1 = new TextView(getApplicationContext());
                            String font_path = "fonts/Ubuntu-C.ttf";
                            final Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
                            tvcol1.setTypeface(TF);
                            tvcorreo.setTypeface(TF);
                            tvcol1.setBackgroundResource(R.drawable.edittextstyle);
                            Color.parseColor("#000000");
                            tvcol1.setTextColor(Color.parseColor("#000000"));
                            tvcol1.setId(200 + i);
                            tvcol1.setText(" Clase: " + clases.getString("nombre_clase") + "\n" + " Sala: " + clases.getString("nombre_aula") + "\n" + " hora: " + clases.getString("hora_inicio") + " - " + clases.getString("hora_final") + "\n" + " fecha: " + clases.getString("fecha"));

                            trclase.addView(tvcol1);
                            tlclase.addView(trclase);
                        }




                    } else {
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


        String url_photo = "http://192.168.1.66/gestao/img_profiles/" + id_profesor + ".jpg";
        ImageRequest imageRequest = new ImageRequest(url_photo,

                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        CircularNetworkImageView im = new CircularNetworkImageView(getApplicationContext());
                        Bitmap imgful = im.getCircularBitmap(response);

                        ivfoto.setImageBitmap(imgful);
                    }
                }, 300, 300, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
                    String[] profesoresNombre = new String[jArray.length()];

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject profesores = jArray.getJSONObject(i);

                        profesoresNombre[i] = profesores.getString("nombre") + " " + profesores.getString("apellido");


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








