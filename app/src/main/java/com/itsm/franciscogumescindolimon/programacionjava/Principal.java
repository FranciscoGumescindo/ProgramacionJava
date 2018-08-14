package com.itsm.franciscogumescindolimon.programacionjava;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

//public class Principal extends AppCompatActivity {
public class Principal extends AppCompatActivity {
    private ImageButton chat;
    private Button salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        chat = (ImageButton)findViewById(R.id.chat);
        salir = (Button)findViewById(R.id.salir);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(Principal.this, MainActivity.class);
                startActivity(intents);
                finish();
            }
        });


    }
    //Para bloquear botonoes de navegacion deja en blanco
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Principal.this, SplashScreen.class);
        startActivity(intent);
        finish();
    }

}
