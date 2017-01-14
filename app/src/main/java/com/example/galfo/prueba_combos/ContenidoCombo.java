package com.example.galfo.prueba_combos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.ETC1;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import helper.BdHelper;

public class ContenidoCombo extends AppCompatActivity {

    BdHelper miBase;
    List<String> textos = new ArrayList<String>();
    FloatingActionButton fabAgregarContenidoCombo;
    Context ctx;
    //Estos dos me sirven para crear los TextView
    String Nombre,Precio;

    TextView prueba;
    LinearLayout layout_general;

    //Lista donde se guardan los nombres de cada cosa que compone al combo para usarlos en los spinners
    //de edicion y borrar
    int cantT,indiceETs,Cantidad;

    String[][] matriz;

    //Arreglo de edit text
    EditText[] textboxs ;

    //compuesto posee los nombres del contenido del combo
    ArrayList<String> Compuesto = new ArrayList<String>();

    //para saber que boton se presiono(jalo el texto del boton)
    String idcomboCC = MainActivity.idcomboCC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_combo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //llamando a la base
        miBase = new BdHelper(this);

        cantT=miBase.cantidad_rowsCC();

        matriz = new String[cantT][2];

        indiceETs=0;

        setTitle("Contenido");

        fabAgregarContenidoCombo = (FloatingActionButton) findViewById(R.id.fabAgregarContenidoCombo);
        ctx = this;
        layout_general = (LinearLayout) findViewById(R.id.layout_ContenidoCombo);

        verContenido();
        IrAgregarContenidoCombo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contenidocombo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cc_editar) {
            Intent intent = new Intent(ctx, CC_Editar.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Compuesto", Compuesto);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.cc_borrar) {
            Intent intent = new Intent(ctx, CC_Borrar.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Compuesto", Compuesto);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void IrAgregarContenidoCombo() {
        fabAgregarContenidoCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AgregarContenidoCombo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Compuesto", Compuesto);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    //Metodo de mensaje
    public void Mensaje(String title, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(mensaje);
        builder.show();
    }

    public void verContenido() {
        //Llamando la funcion de la (miBaseD.java)
        //LlamandoDatos
        Cursor resultado = miBase.llamandoDatosCC(idcomboCC);
        if (resultado.getCount() == 0) {
            //muestra el mensaje
            Mensaje("Advertencia", "No hay datos que mostrar!");
            return;
        }

        int fila,columna;
        fila=0;columna=0;
        StringBuffer buffer = new StringBuffer();
        //Condicion while
        indiceETs=0;
        textboxs = new EditText[cantT];
        while (resultado.moveToNext()) {
            //metiendo los datos a la matriz de 2x2
            if(columna<=1&&fila<=cantT){

                if(columna==0){
                    matriz[fila][columna]=resultado.getString(1);
                    columna++;
                }if(columna==1){
                    matriz[fila][columna]=resultado.getString(2);
                    columna=0;
                    fila++;
                }
            }
            Compuesto.add(resultado.getString(1));
            Nombre=resultado.getString(1);
            Precio=resultado.getString(2);
            Cantidad=resultado.getInt(4);
            CrearTV(Nombre,Precio);
            CrearETs(Nombre,Cantidad,Precio);

        }

        //CrearBotonGuardar();
    }

    public void CrearETs(String Nombre, int cantidad, String Precio){
        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                //con estos parametros hago que el boton se adapte al parent
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        final EditText ETs = new EditText(ctx);
        ETs.setHint("Cantidad de "+Nombre);
        ETs.setTag(String.valueOf(cantidad)+"-"+Nombre);
        ETs.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        textboxs[indiceETs]=ETs;
        textboxs[indiceETs].setLayoutParams(lprams);
        layout_general.addView(textboxs[indiceETs]);
        //ETs.setLayoutParams(lprams);
        //layout_general.addView(ETs);
        indiceETs++;
    }

    public void CrearTV(String Nombre, String Precio){
        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                //con estos parametros hago que el boton se adapte al parent
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        final TextView TVs = new TextView(ctx);
        TVs.setText("\n"+Nombre+" $"+Precio);
        TVs.setTextSize(20);
        layout_general.addView(TVs);
    }

    @Override
    public void onBackPressed(){
        if(Compuesto.size()==0){
            Toast.makeText(ContenidoCombo.this,"No hay datos que agregar a la factura",Toast.LENGTH_LONG).show();
            finish();
        }else{
            String Nombre;
            int NuevaCant,Cantidad,CantidadBD;
            float Precio,Total;
            int index;
            for(int i=0;i<indiceETs;i++){
                Nombre=textboxs[i].getTag().toString();

                if(textboxs[i].getText().toString().equals("")||textboxs[i].getText().toString().equals(" ")){
                    Cantidad=0;
                }else{
                    Cantidad=Integer.parseInt(textboxs[i].getText().toString());
                }

                index=textboxs[i].getTag().toString().indexOf('-');
                CantidadBD=Integer.parseInt(Nombre.substring(0,index));
                Nombre=Nombre.substring(index+1,Nombre.length());
                NuevaCant=Cantidad+CantidadBD;

                miBase.AgNCant(Nombre,NuevaCant);
            }

            Toast.makeText(ContenidoCombo.this,"Los datos se han agregado a la factura",Toast.LENGTH_LONG).show();
            finish();
        }


        Intent intent = new Intent(ctx, MainActivity.class);
        startActivity(intent);
        finish();
    }

}