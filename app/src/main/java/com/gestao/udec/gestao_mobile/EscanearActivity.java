package com.gestao.udec.gestao_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EscanearActivity extends AppCompatActivity {
    TextView aulaNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear);
        Bundle extras = getIntent().getExtras();
        String aula = extras.getString("aula");
        aulaNombre = (TextView) findViewById(R.id.tvAula);

        aulaNombre.setText(aula);

    }
}
