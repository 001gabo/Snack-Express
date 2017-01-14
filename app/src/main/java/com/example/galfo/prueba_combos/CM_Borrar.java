package com.example.galfo.prueba_combos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import helper.BdHelper;

public class CM_Borrar extends AppCompatActivity {

    BdHelper miBase;
    Context ctx;
    Spinner spin_combos;
    Button btnBorrarCM;
    String item_selec;

    //para saber que boton se presiono(jalo el texto del boton)
    String idcomboCC= MainActivity.idcomboCC;

    //declarando arreglo para usar en el spinner
    ArrayList<String> textos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cm__borrar);
        setTitle("Borrar");

        //Jalando el arreglo del MainActivity para llenar el spinner
        Bundle bundleObject = getIntent().getExtras();
        textos=(ArrayList<String>)bundleObject.getSerializable("textos");

        spin_combos=(Spinner)findViewById(R.id.spin_combos_borrar);
        btnBorrarCM=(Button)findViewById(R.id.btnBorrar_Edit);

        //llamando a la base
        miBase = new BdHelper(this);

        ctx=this;


        AgregarAlSpinner();
        BorrarCM();
        Vacio();
    }

    public void BorrarCM(){
        btnBorrarCM.setOnClickListener(new View.OnClickListener() {
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

    public void VerSiVacio(){
        if(spin_combos.getCount()!=0){
            item_selec=spin_combos.getSelectedItem().toString().trim();
            miBase.BorrarC(item_selec);
            Toast.makeText(CM_Borrar.this, "El combo seleccionado se borro de forma exitosa", Toast.LENGTH_LONG).show();
            Intent volver = new Intent(CM_Borrar.this, MainActivity.class);
            startActivity(volver);
            finish();
        }else{
            Toast.makeText(CM_Borrar.this,"No se pudo borrar el combo seleccionado",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
