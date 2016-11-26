package com.gestao.udec.gestao_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "http://gestao-web.co/select_perfil.php";
    String url2 = "http://gestao-web.co/update_perfil.php";

    EditText codigo;//o
    EditText nombre1;//o
    EditText nombre2;
    EditText apellido1;//o
    EditText apellido2;
    EditText email;//o
    EditText telefono;//o
    EditText facebook;//o
    EditText twitter;
    EditText descripcion;//o
    EditText semestre;
    Button actualizar,imagen;

    RequestQueue requestQueue;
    SessionManager sesion;
    private Bitmap bitmap;
    private ImageView imageView;
    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://gestao-web.co/upload_image.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sesion = new SessionManager(PerfilActivity.this);
        imageView  = (ImageView) findViewById(R.id.imageView);
        CircularNetworkImageView im = new CircularNetworkImageView(getApplicationContext());
        Bitmap icon = BitmapFactory.decodeResource(PerfilActivity.this.getResources(),
                R.mipmap.defaul);
        icon = redimensionarImagenMaximo(icon,150,150);
        imageView.setImageBitmap(im.getCircularBitmap(icon));

        codigo = (EditText) findViewById(R.id.etCodigo);
        nombre1 = (EditText) findViewById(R.id.etNombre1);
        nombre2 = (EditText) findViewById(R.id.etNombre2);
        apellido1 = (EditText) findViewById(R.id.etApellido1);
        apellido2 = (EditText) findViewById(R.id.etApellido2);
        email = (EditText) findViewById(R.id.etEmail);
        telefono = (EditText) findViewById(R.id.etTelefono);
        facebook = (EditText) findViewById(R.id.etFacebook);
        twitter = (EditText) findViewById(R.id.etTwitter);
        descripcion = (EditText) findViewById(R.id.etDescripcion);
        semestre = (EditText) findViewById(R.id.etSemestre);
        actualizar = (Button) findViewById(R.id.btActualizar);
        imagen = (Button) findViewById(R.id.imagen);
        if (sesion.getUserDetails().get("rol").equals("D")) {
            facebook.setVisibility(View.VISIBLE);
            twitter.setVisibility(View.VISIBLE);
            descripcion.setVisibility(View.VISIBLE);
            imagen.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            semestre.setVisibility(View.VISIBLE);
        }

        String font_path = "fonts/Ubuntu-C.ttf";
        final Typeface TF = Typeface.createFromAsset(getAssets(),font_path);

        codigo.setTypeface(TF);
        nombre1.setTypeface(TF);
        nombre2.setTypeface(TF);
        apellido1.setTypeface(TF);
        apellido2.setTypeface(TF);
        email.setTypeface(TF);
        telefono.setTypeface(TF);
        facebook.setTypeface(TF);
        twitter.setTypeface(TF);
        descripcion.setTypeface(TF);
        semestre.setTypeface(TF);
        actualizar.setTypeface(TF);

        actualizar.setOnClickListener(this);
        imagen.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        obtenerDatosActuales();
    }

    protected void obtenerDatosActuales() {


        String url_photo = "http://gestao-web.co/img/" + sesion.getUserDetails().get("id") + ".jpg";
        ImageRequest imageRequest = new ImageRequest(url_photo,

                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        CircularNetworkImageView im = new CircularNetworkImageView(getApplicationContext());
                        Bitmap imgful = im.getCircularBitmap(response);

                        imageView.setImageBitmap(imgful);
                    }
                }, 150, 150, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(imageRequest);


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jArray = jsonResponse.getJSONArray("response");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject clases = jArray.getJSONObject(i);
                        codigo.setText(clases.getString("codigo"));
                        nombre1.setText(clases.getString("nombre1"));
                        nombre2.setText(clases.getString("nombre2"));
                        apellido1.setText(clases.getString("apellido1"));
                        apellido2.setText(clases.getString("apellido2"));
                        email.setText(clases.getString("email"));
                        telefono.setText(clases.getString("telefono"));

                        if (sesion.getUserDetails().get("rol").equals("D")) {
                            facebook.setText(clases.getString("facebook"));
                            twitter.setText(clases.getString("twitter"));
                            descripcion.setText(clases.getString("descripcion"));
                        } else {
                            semestre.setText(clases.getString("semestre"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PerfilActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_persona", sesion.getUserDetails().get("id"));
                parameters.put("rol", sesion.getUserDetails().get("rol"));
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btActualizar:
                if (sesion.getUserDetails().get("rol").equals("D")) {
                    uploadImage();
                }

                requestQueue = Volley.newRequestQueue(getApplicationContext());


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(codigo.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(nombre1.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(nombre2.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(apellido1.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(apellido2.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(telefono.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(facebook.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(twitter.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(descripcion.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(semestre.getWindowToken(), 0);


                boolean estado = true;


                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                ;

                Matcher mather = pattern.matcher(email.getText().toString());

                if (nombre1.getText().toString().trim().equalsIgnoreCase("")) {
                    nombre1.setError(getResources().getString(R.string.campoNoNulo));
                    estado = false;
                } else {
                    pattern = Pattern.compile("^[a-zA-Z ]*$");
                    if (pattern.matcher(nombre1.getText().toString()).find() != true) {
                        nombre1.setError(getResources().getString(R.string.campoLetras));
                        estado = false;
                    }
                }

                if (!nombre2.getText().toString().trim().equalsIgnoreCase("")) {
                    pattern = Pattern.compile("^[a-zA-Z ]*$");
                    if (pattern.matcher(nombre2.getText().toString()).find() != true) {
                        nombre2.setError(getResources().getString(R.string.campoLetras));
                        estado = false;
                    }
                }

                if (apellido1.getText().toString().trim().equalsIgnoreCase("")) {
                    apellido1.setError(getResources().getString(R.string.campoNoNulo));
                    estado = false;
                } else {
                    pattern = Pattern.compile("^[a-zA-Z ]*$");
                    if (pattern.matcher(apellido1.getText().toString()).find() != true) {
                        apellido1.setError(getResources().getString(R.string.campoLetras));
                        estado = false;
                    }
                }

                if (!apellido2.getText().toString().trim().equalsIgnoreCase("")) {
                    pattern = Pattern.compile("^[a-zA-Z ]*$");
                    if (pattern.matcher(apellido2.getText().toString()).find() != true) {
                        apellido2.setError(getResources().getString(R.string.campoLetras));
                        estado = false;
                    }
                }


                if (mather.find() != true) {
                    email.setError(getResources().getString(R.string.correoInvalido));
                    estado = false;
                }

                pattern = Pattern
                        .compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

                ;
                if (!facebook.getText().toString().equals("")) {
                    mather = pattern.matcher(facebook.getText().toString());

                    if (mather.find() != true) {
                        facebook.setError(getResources().getString(R.string.urlInvalida));
                        estado = false;
                    }
                }

                ;
                if (!twitter.getText().toString().equals("")) {
                    mather = pattern.matcher(twitter.getText().toString());

                    if (mather.find() != true) {
                        twitter.setError(getResources().getString(R.string.urlInvalida));
                        estado = false;
                    }
                }

                if (sesion.getUserDetails().get("rol").equals("E")) {
                    if (semestre.getText().toString().equals("")) {
                        estado = false;
                        semestre.setError(getResources().getString(R.string.semestreInvalido));
                    } else if (Integer.parseInt(semestre.getText().toString()) < 1 || Integer.parseInt(semestre.getText().toString()) >= 13) {
                        estado = false;
                        semestre.setError(getResources().getString(R.string.semestreInvalido));
                    }
                }


                if (estado == true) {

                    StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Intent intent;
                           //Toast.makeText(PerfilActivity.this, response, Toast.LENGTH_LONG).show();
                            if (sesion.getUserDetails().get("rol").equals("D")) {
                                intent = new Intent(PerfilActivity.this, TeacherAreaActivity.class);
                            } else {
                                intent = new Intent(PerfilActivity.this, UserAreaActivity.class);
                            }
                            PerfilActivity.this.startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PerfilActivity.this, getResources().getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("id_persona", sesion.getUserDetails().get("id"));
                            parameters.put("rol", sesion.getUserDetails().get("rol"));
                            parameters.put("codigo", codigo.getText().toString());
                            parameters.put("nombre1", nombre1.getText().toString());
                            parameters.put("nombre2", nombre2.getText().toString());
                            parameters.put("apellido1", apellido1.getText().toString());
                            parameters.put("apellido2", apellido2.getText().toString());
                            parameters.put("email", email.getText().toString());
                            parameters.put("telefono", telefono.getText().toString());
                            parameters.put("codigo", codigo.getText().toString());
                            parameters.put("facebook", facebook.getText().toString());
                            parameters.put("twitter", twitter.getText().toString());
                            parameters.put("descripcion", descripcion.getText().toString());
                            parameters.put("semestre", semestre.getText().toString());

                            return parameters;
                        }
                    };
                    requestQueue.add(request);

                }


                break;
            case R.id.imagen:
                showFileChooser();
                break;

        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                bitmap = redimensionarImagenMaximo(bitmap,150,150);
                CircularNetworkImageView im = new CircularNetworkImageView(getApplicationContext());
                imageView.setImageBitmap(im.getCircularBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                       // Toast.makeText(PerfilActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                      //  Toast.makeText(PerfilActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = sesion.getUserDetails().get("id");

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
