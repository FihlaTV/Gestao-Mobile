package com.gestao.udec.gestao_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText etnombre1 = (EditText) findViewById(R.id.etNombre1);
        final EditText etnombre2 = (EditText) findViewById(R.id.etNombre2);
        final EditText etapellido1 = (EditText) findViewById(R.id.etApellido1);
        final EditText etapellido2 = (EditText) findViewById(R.id.etApellido2);
        final EditText etcorreo = (EditText) findViewById(R.id.etCorreo);
        final EditText etclave = (EditText) findViewById(R.id.etClave);
        final RadioButton rbestudiante = (RadioButton) findViewById(R.id.rbEstudiante);
        final RadioButton rbdocente = (RadioButton) findViewById(R.id.rbDocente);
        final Button btnregistro = (Button) findViewById(R.id.btnRegistro);
    }
}
