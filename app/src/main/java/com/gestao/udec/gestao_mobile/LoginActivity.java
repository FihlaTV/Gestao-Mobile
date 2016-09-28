package com.gestao.udec.gestao_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etcorreo = (EditText) findViewById(R.id.etCorreo);
        final EditText etclave = (EditText) findViewById(R.id.etClave);
        final Button btnregistro = (Button) findViewById(R.id.btnIngresar);
        final TextView etregistro = (TextView) findViewById(R.id.etRegistro);
        etregistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }
}
