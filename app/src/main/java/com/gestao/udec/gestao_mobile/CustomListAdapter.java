package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robayo on 25/10/2016.
 */

public class CustomListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> lista = new ArrayList<String>();
    private Context context;
    private String botonTitulo;
    private HashMap<String,String> clasesVinculadas;

    public CustomListAdapter(ArrayList<String> lista, HashMap<String, String> clasesVinculadas,String botonTitulo, Context context){
        this.lista = lista;
        this.context = context;
        this.clasesVinculadas = clasesVinculadas;
        this.botonTitulo = botonTitulo;
    }
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Typeface TF = Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-C.ttf");
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_vincular, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.tvListElementVinculadas);
        listItemText.setText(lista.get(position));
        listItemText.setTypeface(TF);


        //Handle buttons and add onClickListeners
        Button boton = (Button)view.findViewById(R.id.btListBorrar);
        boton.setText(botonTitulo);
        boton.setTypeface(TF);

        if(boton.getText().toString().equals("Borrar")) {
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://192.168.1.66/gestao/mobile/desvinculacion_clase.php";

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            lista.remove(position);
                            notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, context.getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            SessionManager sesion;
                            sesion = new SessionManager(context);
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("id_clase", clasesVinculadas.get(lista.get(position)));
                            parameters.put("id_persona", sesion.getUserDetails().get("id"));
                            parameters.put("rol", sesion.getUserDetails().get("rol"));

                            return parameters;
                        }
                    };
                    requestQueue.add(request);


                }
            });
        }else{
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, clasesVinculadas.get(lista.get(position)) , Toast.LENGTH_LONG).show();
                }
            });
        }


        return view;
    }
}

