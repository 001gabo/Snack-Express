package com.example.galfo.prueba_combos;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.text.StringPrepParseException;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import helper.BdHelper;

public class AgregarContenidoCombo extends AppCompatActivity {

    BdHelper miBase;
    Context ctx;
    Button btnGuardar;
    EditText NombreContenido,Precio;
    String PrecioU;


    //con eso traigo el idboton del MainActivity
    int idboton = MainActivity.idboton;

    //trayendo el texto del boton
    String idcomboCC= MainActivity.idcomboCC;

    //Listado de los contenido del combo
    ArrayList<String> Compuesto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contenido_combo);

        setTitle("Agregar nuevo contenido");

        //llamando a la base
        miBase = new BdHelper(this);

        Bundle bundleObject = getIntent().getExtras();
        Compuesto=(ArrayList<String>)bundleObject.getSerializable("Compuesto");

        btnGuardar=(Button)findViewById(R.id.btngGuardar);
        NombreContenido=(EditText)findViewById(R.id.txtNombre);
        Precio=(EditText)findViewById(R.id.txtPrecio);



        ctx=this;

        AgregarContenidoCombo();

    }
    //Metodo de agregar datos
    public void AgregarContenidoCombo() {
        btnGuardar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted;
                        String prueba;
                        prueba = Precio.getText().toString();
                        if(prueba.equals("")||prueba.equals(" ")){
                            PrecioU=String.valueOf(0);
                        }else{
                            PrecioU=String.format(Locale.ROOT,"%.2f",Double.valueOf(Precio.getText().toString()));
                       }

                        if(NombreContenido.length()==0 && Precio.length()==0){
                            isInserted=false;
                            Toast.makeText(AgregarContenidoCombo.this,"Los datos no fueron insertados",Toast.LENGTH_LONG).show();
                        }if(NombreContenido.length()==0 || Precio.length()==0){
                            isInserted=false;
                            Toast.makeText(AgregarContenidoCombo.this,"Los datos no fueron insertados",Toast.LENGTH_LONG).show();
                        }if(Compuesto.contains(NombreContenido.getText().toString())){
                            isInserted=false;
                            Toast.makeText(AgregarContenidoCombo.this,"El elemento que intentas ingresar ya existe",Toast.LENGTH_LONG).show();
                        }if(NombreContenido.length()!=0 && Precio.length()!=0){
                            isInserted=miBase.insertDataCC(NombreContenido.getText().toString(),PrecioU,idcomboCC);
                            isInserted=true;
                            Toast.makeText(AgregarContenidoCombo.this,"Los datos han sido insertados",Toast.LENGTH_LONG).show();
                            Intent volver = new Intent(AgregarContenidoCombo.this,ContenidoCombo.class);
                            startActivity(volver);
                            finish();
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, ContenidoCombo.class);
        startActivity(intent);
        finish();
    }
}
