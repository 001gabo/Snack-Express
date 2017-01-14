package com.example.galfo.prueba_combos;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class Splash extends AppCompatActivity {

    //declaracion de la pantalla de bienvenidos
    public static  final int segundos=5;

    //conversion a milisegundos
    public static final int milisegundos =segundos*1000;
    public static  final int retardo=2;

    private ProgressBar pbprogreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pbprogreso=(ProgressBar)findViewById(R.id.progressBar3);
        iniciar();
    }

    //metdo
    public void iniciar(){

//contador
        new CountDownTimer(milisegundos,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//llamando la barra progreso
                pbprogreso.setProgress(inprogreso(millisUntilFinished));
                //limite de progreso
                pbprogreso.setMax(limite());

            }

            @Override
            public void onFinish() {
//invoca nueva ventada
                Intent nuevaVentana = new Intent(Splash.this,MainActivity.class);
                startActivity(nuevaVentana);
                finish();

            }
        }.start();
    }
    //mtdo barra progreso
    public int inprogreso(long miliseg){
        //tiempo
        return (int)((milisegundos-miliseg)/1000);

    }
    public int limite(){
        return segundos-retardo;

    }

    @Override
    public void onResume(){
        super.onResume();
    }


}
