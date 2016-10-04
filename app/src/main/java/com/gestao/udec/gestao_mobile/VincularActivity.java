package com.gestao.udec.gestao_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VincularActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManager sesion = new SessionManager(VincularActivity.this);
        sesion.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincular);
    }
}
