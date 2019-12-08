package com.ggave.restpetapp.service;

import com.ggave.restpetapp.model.Mascota;
import com.ggave.restpetapp.model.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    String API_BASE_URL = "http://10.0.2.2:8080";

    //usuario apis

    @GET("/usuario")  // http://10.0.2.2:8080/productos
    Call<List<Usuario>> findAllUser();

    @FormUrlEncoded
    @POST("/usuario")
    Call<Usuario> createUsuario(@Field("nombre") String nombre,
                                  @Field("email") String precio,
                                  @Field("password") String password);
    @Multipart
    @POST("/usuario")
    Call<Usuario> createUsuario(@Part("nombre") RequestBody nombre,
                                  @Part("email") RequestBody email,
                                  @Part("password") RequestBody password
    );

    @GET("/usuario/{id}")
    Call<Usuario> showUsuario(@Path("id") Long id);



    //mascota apis

    @GET("/mascota")  // http://10.0.2.2:8080/productos
    Call<List<Mascota>> findAllMascota();

    @FormUrlEncoded
    @POST("/mascota")
    Call<Mascota> createMascota(@Field("nombre") String nombre,
                                 @Field("raza") String raze,
                                 @Field("edad") String edad);
    @Multipart
    @POST("/mascota")
    Call<Mascota> createMascota(@Part("nombre") RequestBody nombre,
                                 @Part("raza") RequestBody raza,
                                 @Part("edad") RequestBody edad,
                                 @Part MultipartBody.Part imagen
    );

    @GET("/mascota/{id}")
    Call<Usuario> showMascota(@Path("id") Long id);
}
