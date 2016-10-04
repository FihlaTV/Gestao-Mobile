package com.gestao.udec.gestao_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ReservarActivity extends AppCompatActivity {
    Spinner clase;
    Spinner diaSemana;
    EditText horaInicial;
    EditText horaFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManager sesion = new SessionManager(ReservarActivity.this);
        sesion.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        clase = (Spinner) findViewById(R.id.spClase);
        diaSemana = (Spinner) findViewById(R.id.spDiaSemana);
        horaInicial = (EditText) findViewById(R.id.etHoraInicial);
        horaFinal = (EditText) findViewById(R.id.etHoraFinal);

        String[] diasSemana= {getResources().getString(R.string.lunes),getResources().getString(R.string.martes),getResources().getString(R.string.miercoles),getResources().getString(R.string.jueves),getResources().getString(R.string.viernes),getResources().getString(R.string.sabado)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,diasSemana);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaSemana.setAdapter(adapter);

    }
}
