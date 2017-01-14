package com.example.galfo.prueba_combos;

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

public class CC_Borrar extends AppCompatActivity {

    BdHelper miBase;
    Spinner spin_combo_CC_borrar;
    Button btnBorrar_CC;
    String item_selec;

    //declarando arreglo para usar en el spinner
    ArrayList<String> Compuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc__borrar);

        setTitle("Borrar");

        //Jalando el arreglo del MainActivity para llenar el spinner
        Bundle bundleObject = getIntent().getExtras();
        Compuesto=(ArrayList<String>)bundleObject.getSerializable("Compuesto");
        spin_combo_CC_borrar=(Spinner)findViewById(R.id.spin_contenido_borrar_CC);
        btnBorrar_CC=(Button)findViewById(R.id.btnBorrar_CC);

        //llamando a la base
        miBase = new BdHelper(this);

        AgregarAlSpinner();
        BorrarCC();
        Vacio();
    }

    public void AgregarAlSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,Compuesto);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_combo_CC_borrar.setAdapter(adapter);
    }

    public void BorrarCC(){
        btnBorrar_CC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               VerSiVacio();
            }
        });
    }

    public void VerSiVacio(){
        if(spin_combo_CC_borrar.getCount()!=0){
            item_selec=spin_combo_CC_borrar.getSelectedItem().toString();
            miBase.BorrarContComb(item_selec);
            Toast.makeText(CC_Borrar.this, "El combo seleccionado se borro de forma exitosa", Toast.LENGTH_LONG).show();
            Intent volver = new Intent(CC_Borrar.this, ContenidoCombo.class);
            startActivity(volver);
            finish();
        }else{
            Toast.makeText(CC_Borrar.this,"No se pudo borra el combo seleccionado",Toast.LENGTH_LONG).show();
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
        if(spin_combo_CC_borrar.getCount()==0){
            //muestra el mensaje
            Mensaje("Advertencia","No hay datos que mostrar!");
            return;
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, ContenidoCombo.class);
        startActivity(intent);
        finish();
    }
}
