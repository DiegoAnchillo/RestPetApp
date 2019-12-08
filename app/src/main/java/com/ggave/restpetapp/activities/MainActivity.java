package com.ggave.restpetapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ggave.restpetapp.R;
import com.ggave.restpetapp.adapter.MascotaAdapter;
import com.ggave.restpetapp.model.ApiError;
import com.ggave.restpetapp.model.Mascota;
import com.ggave.restpetapp.service.ApiService;
import com.ggave.restpetapp.service.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mascotasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testRest();

        mascotasList = findViewById(R.id.recyclerview);

        mascotasList.setLayoutManager(new LinearLayoutManager(this));
        mascotasList.setAdapter(new MascotaAdapter());

        initialize();
    }

    public void initialize() {
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        service.findAllMascota().enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if(response.isSuccessful()) {

                    List<Mascota> mascota = response.body();
                    Log.d(TAG, "mascota: " + mascota);

                    MascotaAdapter adapter = (MascotaAdapter) mascotasList.getAdapter();
                    adapter.setMascota(mascota);
                    adapter.notifyDataSetChanged();

                } else {
                    ApiError error = ApiServiceGenerator.parseError(response);
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void testRest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<List<Mascota>> call = service.findAllMascota();
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call,
                                   Response<List<Mascota>> response) {
                if(response.isSuccessful()) {
                    List<Mascota> mascota = response.body();
                    Log.d("MainActivity", "mascota: " + mascota);
                } else {
                    Toast.makeText(MainActivity.this, "Error: "
                            + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error Critico: "
                        + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static final int REQUEST_REGISTER_FORM = 100;

    public void showRegister(View view){
        startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_REGISTER_FORM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_REGISTER_FORM) {
            initialize();   // refresh data from rest service
        }
    }
}
