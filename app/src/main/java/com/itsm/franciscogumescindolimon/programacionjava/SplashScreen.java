package com.itsm.franciscogumescindolimon.programacionjava;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {
    private Button entrar, salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        entrar = (Button)findViewById(R.id.entrar);
        salir = (Button)findViewById(R.id.salir);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(SplashScreen.this, Principal.class);
                startActivity(intents);
                finish();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder.setTitle("JAVA-BOT!!!").
                        setMessage("¿Desea salir de la Aplicación?").
                        setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                // finish();
                            }
                        }).
                        setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });


                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

    }
    //Para bloquear botonoes de navegacion deja en blanco
    @Override
    public void onBackPressed(){ }
}
