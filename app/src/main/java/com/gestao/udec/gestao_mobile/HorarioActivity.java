package com.gestao.udec.gestao_mobile;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorarioActivity extends Activity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_horario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Horario Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ListView horario;
        TextView fecha;
        private String url = "http://192.168.1.66/gestao/mobile/select_horario_persona.php";
        View rootView;
        HashMap<String,String> clasesVinculadas;

        String fechaABuscar;

        RequestQueue requestQueue;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.fragment_horario, container, false);
            fecha = (TextView) rootView.findViewById(R.id.tvFecha);
            horario = (ListView) rootView.findViewById(R.id.lvHorario);

            requestQueue = Volley.newRequestQueue(rootView.getContext());
            Date fechaActual = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(fechaActual);
            if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                c.add(Calendar.DATE,1);
            }
            for (int i=0; i<getArguments().getInt(ARG_SECTION_NUMBER);i++){
                c.add(Calendar.DATE,1);
                if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                    c.add(Calendar.DATE,1);
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fechaABuscar= sdf.format(c.getTime());
            fecha.setText(obtenerDiaSemana(c.get(Calendar.DAY_OF_WEEK)) +" - " + fechaABuscar);
            obtenerHorario();

            return rootView;
        }

        private String obtenerDiaSemana(int valor) {
            String day = "";
            switch (valor) {
                case 2:
                    day = rootView.getContext().getResources().getString(R.string.lunes);
                    break;
                case 3:
                    day = rootView.getContext().getResources().getString(R.string.martes);
                    break;
                case 4:
                    day = rootView.getContext().getResources().getString(R.string.miercoles);
                    break;
                case 5:
                    day = rootView.getContext().getResources().getString(R.string.jueves);
                    break;
                case 6:
                    day = rootView.getContext().getResources().getString(R.string.viernes);
                    break;
                case 7:
                    day = rootView.getContext().getResources().getString(R.string.sabado);
                    break;
                default:
                    day = rootView.getContext().getResources().getString(R.string.diaNoDefinido);
                    break;
            }
            return day;
        }

        protected void obtenerHorario() {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jArray = jsonResponse.getJSONArray("response");
                        SessionManager sesion = new SessionManager(rootView.getContext());
                        String[] claseHoras= new String[15];
                        clasesVinculadas = new HashMap<String, String>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject e = jArray.getJSONObject(i);
                            if (sesion.getUserDetails().get("rol").equals("D")) {
                                for (int j=e.getInt("hora_inicio");j<e.getInt("hora_final");j++){
                                    String textoLista = String.format("%02d", j)+" - "+ String.format("%02d", (j+1))+" "+e.getString("clase")+" - "+ e.getString("grupo")+" - "+e.getString("aula");
                                    claseHoras[j-7]= textoLista;
                                    clasesVinculadas.put(textoLista,e.getString("observaciones")+"_"+fechaABuscar+"_"+e.getString("clase_id"));
                                }
                            } else {
                                for (int j=e.getInt("hora_inicio");j<e.getInt("hora_final");j++){
                                    String textoLista = String.format("%02d", j)+" - "+ String.format("%02d", (j+1))+" "+e.getString("clase")+" - "+ e.getString("grupo")+" - "+e.getString("aula")+" - "+e.getString("docente");
                                    claseHoras[j-7]= textoLista;
                                    clasesVinculadas.put(textoLista,e.getString("observaciones"));
                                }
                            }
                        }
                        for(int j=0;j<claseHoras.length;j++){
                            if(claseHoras[j]== null){
                                String output = String.format("%02d", (j+7))+" - "+ String.format("%02d", (j+8))+" Libre";
                                claseHoras[j]= output;
                                clasesVinculadas.put(output,getResources().getString(R.string.noTienesClaseAEstaHora));
                            }
                        }
                        List<String> wordList = Arrays.asList(claseHoras);
                        ArrayList<String> al = new ArrayList<String>(wordList);
                        CustomListAdapter adapter = new CustomListAdapter(al, clasesVinculadas,"Observacion", rootView.getContext());
                        horario.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(rootView.getContext(), getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    SessionManager sesion = new SessionManager(rootView.getContext());
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("id_persona", sesion.getUserDetails().get("id"));
                    parameters.put("rol", sesion.getUserDetails().get("rol"));
                    parameters.put("fecha",fechaABuscar);

                    return parameters;
                }
            };
            requestQueue.add(request);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
