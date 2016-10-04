package com.gestao.udec.gestao_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TeacherAreaActivity extends AppCompatActivity implements View.OnClickListener{
    Button escanear;
    Button vincular;
    Button horario;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        escanear = (Button) findViewById(R.id.btescanear);
        vincular = (Button) findViewById(R.id.btvincular);
        horario = (Button) findViewById(R.id.bthorario);
        registrar = (Button) findViewById(R.id.btreservar);
        escanear.setOnClickListener(this);
        vincular.setOnClickListener(this);
        horario.setOnClickListener(this);
        registrar.setOnClickListener(this);
        /*
        Intent intent = getIntent();
        String name1 = intent.getStringExtra("name1");



        TextView tvhola = (TextView) findViewById(R.id.tvhola);


        // Display user details
        String message = name1 + " welcome to your user area";
        tvhola.setText(message);
*/
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btescanear:
                break;
            case R.id.bthorario:
                break;
            case R.id.btreservar:
                break;
            case R.id.btvincular:
                break;

        }
    }
}