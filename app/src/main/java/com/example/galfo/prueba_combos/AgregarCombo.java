package com.example.galfo.prueba_combos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import helper.BdHelper;

public class AgregarCombo extends AppCompatActivity {

    BdHelper miBase;
    EditText NombreCombo;
    Button btnAgregarNuevoCombo;

    //Listado de nombres de los combos
    ArrayList<String> textos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_combo);


        setTitle("Agregar nuevo");

        //llamando a la base
        miBase = new BdHelper(this);

        NombreCombo=(EditText)findViewById(R.id.txtNombreCombo);
        btnAgregarNuevoCombo=(Button)findViewById(R.id.btnAgregarNuevoCombo);

        Bundle bundleObject = getIntent().getExtras();
        textos=(ArrayList<String>)bundleObject.getSerializable("textos");

        AgDatos();
    }

    //Metodo de agregar datos
    public void AgDatos() {
        btnAgregarNuevoCombo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted;

                        //condicion
                        if(NombreCombo.length()==0){
                            isInserted=false;
                            Toast.makeText(AgregarCombo.this,"Los datos no fueron insertados",Toast.LENGTH_LONG).show();
                        }else if(textos.contains(NombreCombo.getText().toString())){
                            isInserted=false;
                            Toast.makeText(AgregarCombo.this,"El elemento que intentas ingresar ya existe",Toast.LENGTH_LONG).show();
                        }else if(NombreCombo.length()!=0){
                            isInserted=miBase.insertData(NombreCombo.getText().toString());
                            isInserted=true;
                            Toast.makeText(AgregarCombo.this,"Los datos han sido insertados",Toast.LENGTH_LONG).show();
                            Intent volver = new Intent(AgregarCombo.this,MainActivity.class);
                            startActivity(volver);
                            finish();
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
