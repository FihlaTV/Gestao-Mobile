package com.gestao.udec.gestao_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private HashMap<String, String> clasesVinculadas;

    public CustomListAdapter(ArrayList<String> lista, HashMap<String, String> clasesVinculadas, String botonTitulo, Context context) {
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
        final Typeface TF = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-C.ttf");
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_vincular, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.tvListElementVinculadas);
        listItemText.setText(lista.get(position));
        listItemText.setTypeface(TF);


        //Handle buttons and add onClickListeners
        Button boton = (Button) view.findViewById(R.id.btListBorrar);
        boton.setText(botonTitulo);
        boton.setTypeface(TF);

        if (boton.getText().toString().equals("Borrar")) {
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://gestao-web.co/desvinculacion_clase.php";

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
        } else {
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionManager sesion = new SessionManager(context);
                    if (sesion.getUserDetails().get("rol").equals("E")) {
                        Toast.makeText(context, clasesVinculadas.get(lista.get(position)), Toast.LENGTH_LONG).show();
                    } else {
                        if (!context.getResources().getString(R.string.noTienesClaseAEstaHora).equals(clasesVinculadas.get(lista.get(position)))) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setTitle(context.getResources().getString(R.string.observacion));
                            alertDialog.setMessage(context.getResources().getString(R.string.ingreseObservacionClase));

                            final EditText input = new EditText(context);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            final String textoObservaciones = clasesVinculadas.get(lista.get(position)).split("_")[0];
                            input.setText(textoObservaciones);
                            alertDialog.setView(input);
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                            alertDialog.setPositiveButton(context.getResources().getString(R.string.aceptar),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!input.getText().toString().equals(textoObservaciones)) {
                                                String url2 = "http://gestao-web.co/actualizar_observacion.php";
                                                RequestQueue requestQueue;
                                                requestQueue = Volley.newRequestQueue(context);
                                                StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        Intent intent;
                                                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                                        intent = new Intent(context, TeacherAreaActivity.class);
                                                        context.startActivity(intent);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(context, context.getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> parameters = new HashMap<String, String>();
                                                        parameters.put("observacion", input.getText().toString());
                                                        parameters.put("fecha", clasesVinculadas.get(lista.get(position)).split("_")[1]);
                                                        parameters.put("id_clase", clasesVinculadas.get(lista.get(position)).split("_")[2]);

                                                        return parameters;
                                                    }
                                                };
                                                requestQueue.add(request);

                                                dialog.cancel();

                                            } else {
                                                dialog.cancel();
                                            }
                                        }
                                    });

                            alertDialog.setNegativeButton(context.getResources().getString(R.string.cancelar),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();

                        } else {
                            Toast.makeText(context, clasesVinculadas.get(lista.get(position)), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }


        return view;
    }
}

