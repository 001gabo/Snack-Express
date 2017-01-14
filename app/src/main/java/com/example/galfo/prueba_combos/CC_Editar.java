package com.example.galfo.prueba_combos;

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

import java.util.ArrayList;
import java.util.Locale;

import helper.BdHelper;

public class CC_Editar extends AppCompatActivity {

    BdHelper miBase;
    Spinner spin_combos_edit_CC;
    EditText NuevoN, NuevoP;
    Button btnGuardarEdicionCC;
    String ViejoN,PrecioN;

    //declarando arreglo para usar en el spinner
    ArrayList<String> Compuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc__editar);

        setTitle("Editar");

        //Jalando el arreglo del MainActivity para llenar el spinner
        Bundle bundleObject = getIntent().getExtras();
        Compuesto=(ArrayList<String>)bundleObject.getSerializable("Compuesto");

        //llamando a la base
        miBase = new BdHelper(this);

        spin_combos_edit_CC=(Spinner)findViewById(R.id.spin_combos_edit_CC);
        NuevoN=(EditText)findViewById(R.id.txtNombreNCC);
        NuevoP=(EditText)findViewById(R.id.txtPrecioNCC);
        btnGuardarEdicionCC=(Button)findViewById(R.id.btnGuardar_CC);

        AgregarAlSpinner();
        GuardarCambios();
        Vacio();
    }

    public void AgregarAlSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,Compuesto);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_combos_edit_CC.setAdapter(adapter);
    }


    public void GuardarCambios(){
        btnGuardarEdicionCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerSiVacio();
            }
        });
    }

    public void VerSiVacio() {
        if (spin_combos_edit_CC.getCount() != 0) {
            ViejoN = spin_combos_edit_CC.getSelectedItem().toString();
            if (NuevoN.length() == 0 && NuevoP.length() != 0) {
                miBase.SoloPrecioCC(ViejoN, String.format(Locale.ROOT,"%.2f",Double.valueOf(NuevoP.getText().toString())));
                Toast.makeText(CC_Editar.this, "La edición fue exitosa", Toast.LENGTH_LONG).show();
                Intent volver = new Intent(CC_Editar.this, ContenidoCombo.class);
                startActivity(volver);
                finish();

            } else if (NuevoP.length() == 0 && NuevoN.length() != 0) {
                miBase.SoloNombreCC(NuevoN.getText().toString(), ViejoN);
                Toast.makeText(CC_Editar.this, "La edición fue exitosa", Toast.LENGTH_LONG).show();
                Intent volver = new Intent(CC_Editar.this, ContenidoCombo.class);
                startActivity(volver);
                finish();
            } else if (NuevoN.length() != 0 && NuevoP.length() != 0) {
                miBase.EditarCompletoCC(NuevoN.getText().toString(), ViejoN, String.format(Locale.ROOT,"%.2f",Double.valueOf(NuevoP.getText().toString())));
                Toast.makeText(CC_Editar.this, "La edición fue exitosa", Toast.LENGTH_LONG).show();
                Intent volver = new Intent(CC_Editar.this, ContenidoCombo.class);
                startActivity(volver);
                finish();
            }
        } if(NuevoN.length() == 0 && NuevoP.length() == 0) {
            Toast.makeText(CC_Editar.this, "No se pudo realizar la edición", Toast.LENGTH_LONG).show();
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
        if(spin_combos_edit_CC.getCount()==0){
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
