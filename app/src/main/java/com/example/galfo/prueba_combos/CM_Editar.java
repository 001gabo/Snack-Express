package com.example.galfo.prueba_combos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import helper.BdHelper;

public class CM_Editar extends AppCompatActivity implements Serializable {

    BdHelper miBase;
    Context ctx;
    Button btnGuardarCambios;
    EditText NuevoN;
    Spinner spin_combos;
    String ViejoN;

    //declarando arreglo para usar en el spinner
    ArrayList<String> textos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cm__editar);

        setTitle("Editar");


        //Jalando el arreglo del MainActivity para llenar el spinner
        Bundle bundleObject = getIntent().getExtras();
        textos=(ArrayList<String>)bundleObject.getSerializable("textos");

        btnGuardarCambios=(Button)findViewById(R.id.btnGuardar_Edit);
        NuevoN=(EditText)findViewById(R.id.txtNuevoN);
        ctx=this;
        spin_combos=(Spinner)findViewById(R.id.spin_combos);

        //llamando a la base
        miBase = new BdHelper(this);

        //Agregando elementos al spinner


        GuardarCambios();
        AgregarAlSpinner();
        Vacio();
    }

    public void GuardarCambios(){
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            VerSiVacio();

                }
        });
    }

    public void AgregarAlSpinner(){
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this,android.R.layout.simple_spinner_item,textos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_combos.setAdapter(adapter);
    }

    public void VerSiVacio(){
        if(spin_combos.getCount()!=0 && NuevoN.length()!=0){
            ViejoN=spin_combos.getSelectedItem().toString();
            miBase.EditC(NuevoN.getText().toString(),ViejoN);
            Toast.makeText(CM_Editar.this, "La edición fue exitosa", Toast.LENGTH_LONG).show();
            Intent volver = new Intent(CM_Editar.this, MainActivity.class);
            startActivity(volver);
            finish();
        }else{
            Toast.makeText(CM_Editar.this,"No se pudo realizar la edición",Toast.LENGTH_LONG).show();
        }
    }

    //Metodo de mensaje
    public void Mensaje(String title, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(mensaje);
        builder.show();
    }

    public void Vacio(){
        if(spin_combos.getCount()==0){
            //muestra el mensaje
            Mensaje("Advertencia","No hay datos que mostrar!");
            return;
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}


