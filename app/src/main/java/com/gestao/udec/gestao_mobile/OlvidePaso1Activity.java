package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OlvidePaso1Activity extends AppCompatActivity implements View.OnClickListener {
    String url ="http://192.168.1.66/gestao/mobile/olvido_contrasena_envio.php";
    EditText correo;
    Button enviar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_paso1);

        correo = (EditText) findViewById(R.id.etCorreo);
        enviar = (Button) findViewById(R.id.btGenerarCodigo);

        String font_path = "fonts/Ubuntu-C.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        correo.setTypeface(TF);
        enviar.setTypeface(TF);

        correo.setText(getIntent().getStringExtra("correo"));

        enviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btGenerarCodigo:
                requestQueue = Volley.newRequestQueue(getApplicationContext());


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(correo.getWindowToken(), 0);


                boolean estado = true;


                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                ;

                Matcher mather = pattern.matcher(correo.getText().toString());

                if (mather.find() != true) {
                    correo.setError(getResources().getString(R.string.correoInvalido));
                    estado = false;
                }



                if (estado == true) {

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Intent intent;
                            Toast.makeText(OlvidePaso1Activity.this, response, Toast.LENGTH_LONG).show();
                            intent = new Intent(OlvidePaso1Activity.this, TeacherAreaActivity.class);
                            OlvidePaso1Activity.this.startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(OlvidePaso1Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("email", correo.getText().toString());


                            return parameters;
                        }
                    };
                    requestQueue.add(request);


                }


                break;

        }
    }
}
