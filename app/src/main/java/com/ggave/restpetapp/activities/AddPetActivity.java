package com.ggave.restpetapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ggave.restpetapp.R;
import com.ggave.restpetapp.model.Mascota;
import com.ggave.restpetapp.service.ApiService;
import com.ggave.restpetapp.service.ApiServiceGenerator;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPetActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private ImageView imagenPreview;

    private EditText nombreInput;
    private EditText razaInput;
    private EditText edadInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imagenPreview = findViewById(R.id.imgFoto);
        nombreInput = findViewById(R.id.nombre_mascota);
        razaInput = findViewById(R.id.raza_mascota);
        edadInput = findViewById(R.id.edad_mascota);

    }

    private static final int REQUEST_CAMERA = 100;

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    private Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = scaleBitmapDown(bitmap, 800);  // Redimensionar
                imagenPreview.setImageBitmap(bitmap);
            }
        }
    }

    public void callRegister(View view){

        String nombre = nombreInput.getText().toString();
        String raza = razaInput.getText().toString();
        String edad = edadInput.getText().toString();

        if (nombre.isEmpty() || raza.isEmpty()|| edad.isEmpty()) {
            Toast.makeText(this, "Nombre, Raza y Edad son campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Mascota> call;

        if(bitmap == null){
            call = service.createMascota(nombre, raza, edad);
        } else {

            // De bitmap a ByteArray
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // ByteArray a MultiPart
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "photo.jpg", requestFile);

            // Paramestros a Part
            RequestBody nombrePart = RequestBody.create(MultipartBody.FORM, nombre);
            RequestBody precioPart = RequestBody.create(MultipartBody.FORM, raza);
            RequestBody detallesPart = RequestBody.create(MultipartBody.FORM, edad);

            call = service.createMascota(nombrePart, precioPart, detallesPart, imagenPart);
        }

        call.enqueue(new Callback<Mascota>() {
            @Override
            public void onResponse(@NonNull Call<Mascota> call, @NonNull Response<Mascota> response) {
                try {
                    if(response.isSuccessful()) {

                        Mascota mascota = response.body();
                        Log.d(TAG, "Mascota: " + mascota);

                        Toast.makeText(AddPetActivity.this, "Registro guardado satisfactoriamente", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);

                        finish();

                    }else{
                        throw new Exception(ApiServiceGenerator.parseError(response).getMessage());
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                    Toast.makeText(AddPetActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Mascota> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toast.makeText(AddPetActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Redimensionar una imagen bitmap
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
