package com.ggave.restpetapp.activities;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ggave.restpetapp.R;
import com.ggave.restpetapp.model.Usuario;
import com.ggave.restpetapp.service.ApiService;
import com.ggave.restpetapp.service.ApiServiceGenerator;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();


    private EditText nombre;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre = findViewById(R.id.nombre_input);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

    }
    private Bitmap bitmap;

    public void callRegister(View view){

        String nombreuser = nombre.getText().toString();
        String emailuser = email.getText().toString();
        String passworduser = password.getText().toString();

        if (nombreuser.isEmpty() || emailuser.isEmpty()|| passworduser.isEmpty()) {
            Toast.makeText(this, "Su nombre , su correo y contraseña son campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Usuario> call;

        if(bitmap == null){
            call = service.createUsuario(nombreuser, emailuser, passworduser);
        } else {

            // Paramestros a Part
            RequestBody nombrePart = RequestBody.create(MultipartBody.FORM,nombreuser);
            RequestBody emailPart = RequestBody.create(MultipartBody.FORM,emailuser);
            RequestBody passwordPart = RequestBody.create(MultipartBody.FORM,passworduser);

            call = service.createUsuario(nombrePart, emailPart, passwordPart);
        }

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                try {
                    if(response.isSuccessful()) {

                        Usuario usuario = response.body();
                        Log.d(TAG, "Usuario: " + usuario);

                        Toast.makeText(RegisterActivity.this, "Registro guardado satisfactoriamente", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);

                        finish();

                    }else{
                        throw new Exception(ApiServiceGenerator.parseError(response).getMessage());
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
