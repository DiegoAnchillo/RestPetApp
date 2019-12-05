package com.ggave.restpetapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ggave.restpetapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.correo);
        password=findViewById(R.id.contraseña);
        login=findViewById(R.id.ingresar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogin();
            }
        });

        register=findViewById(R.id.registro);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegister();
            }
        });
    }

    public void callLogin(){
        
    }

    public void callRegister(){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
