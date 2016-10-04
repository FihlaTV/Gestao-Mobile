package com.gestao.udec.gestao_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity implements View.OnClickListener{
    Button escanear;
    Button vincular;
    Button horario;
    Button registrar;
    Button profesores;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManager sesion = new SessionManager(UserAreaActivity.this);
        sesion.checkLogin();
        if(sesion.getUserDetails().get("rol").equals("D")){
            Intent intent;
            intent = new Intent(UserAreaActivity.this, TeacherAreaActivity.class);
            UserAreaActivity.this.startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        escanear = (Button) findViewById(R.id.btescanear);
        vincular = (Button) findViewById(R.id.btvincular);
        horario = (Button) findViewById(R.id.bthorario);
        profesores = (Button) findViewById(R.id.btprofesores);
        registrar = (Button) findViewById(R.id.btreservar);

        escanear.setOnClickListener(this);
        vincular.setOnClickListener(this);
        horario.setOnClickListener(this);
        profesores.setOnClickListener(this);
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
        Intent intent;
        switch(v.getId()){
            case R.id.btescanear:

                break;
            case R.id.bthorario:
                intent = new Intent(UserAreaActivity.this, HorarioActivity.class);
                UserAreaActivity.this.startActivity(intent);
                break;
            case R.id.btreservar:
                intent = new Intent(UserAreaActivity.this, ReservarActivity.class);
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

        }
    }
}