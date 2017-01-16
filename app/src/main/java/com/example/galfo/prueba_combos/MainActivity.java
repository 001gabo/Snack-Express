package com.example.galfo.prueba_combos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
//import com.facebook.stetho.inspector.elements.Document;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import helper.BdHelper;

public class MainActivity extends AppCompatActivity implements Serializable {

    BdHelper miBase;
    String formattedDate,NombreDoc,NombreToast,NombreCC,Precio,t,td;
    int Cantidad;
    float Total,TotalD,PrecioU;

    //paso la lista para usarlo en el spinner de CM_Editar
    //Aqui se contienen los nombres de los combos
    public ArrayList<String> textos = new ArrayList<String>();


    FloatingActionButton fabAgregarCombo;
    Context ctx;
    LinearLayout layout_general;

    //llevando el nombre del boton
    public static String idcomboCC;

    //Con esto paso datos a otras clases
    public static int idboton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //esto sirve para el diagnostico con stetho
        Stetho.initializeWithDefaults(this);

        //llamando a la base
        miBase = new BdHelper(this);

        setTitle("Inicio");

        //ListTotal=(TextView)findViewById(R.id.txtTotal);
        //Cantidad=(TextView)findViewById(R.id.txtCantidad);
        fabAgregarCombo=(FloatingActionButton) findViewById(R.id.fabAgregarCombo);
        ctx=this;
        layout_general = (LinearLayout)findViewById(R.id.layout_general);
        TotalD=0;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formattedDate = df.format(c.getTime());
        NombreDoc="/Reporte-"+formattedDate+".pdf";
        NombreToast="Reporte-"+formattedDate+".pdf";

        AgregarcomboM();
        verTodo();
        creador_botones();
        FacturaAutomatica();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cm_borrar) {
            Intent intent = new Intent(ctx, CM_Borrar.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("textos",textos);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.cm_editar) {
            Intent intent = new Intent(ctx, CM_Editar.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("textos",textos);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CrearCarpeta(){


        //PARA CREAR UNA CARPETA
        File f = new File(Environment.getExternalStorageDirectory() + "/Snack Express");
        // Comprobamos si la carpeta está ya creada
        // Si la carpeta no está creada, la creamos.
        String newFolder = "/Snack Express"; //cualquierCarpeta es el nombre de la Carpeta que vamos a crear
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String outPath = Environment.getExternalStorageDirectory().toString()+"/Snack Express";
        File myNewFolder = new File(extStorageDirectory + newFolder);

        if(!f.isDirectory()) {
            myNewFolder.mkdir(); //creamos la carpeta
            Toast.makeText(MainActivity.this,"Se ha creado la carpeta 'Snack Express' y dentro de ella se encuentra la factura",Toast.LENGTH_LONG).show();
            crearPDF(NombreDoc,outPath);

        }else{
            Toast.makeText(MainActivity.this,"Se ha creado el documento dentro de la carpeta 'Snack Express'",Toast.LENGTH_LONG).show();
            crearPDF(NombreDoc,outPath);
        }

    }

    public void crearPDF(String NombreDoc,String outPath){
        Document doc = new Document();
        String Salida = outPath+NombreDoc;
        try{
            PdfWriter.getInstance(doc,new FileOutputStream(Salida));
            doc.open();
            doc.add(new Paragraph("Generada: "+formattedDate ));
            doc.add(new Paragraph("\n\n" ));

            for (int i=0;i<textos.size();i++){

                Cursor resultado = miBase.llamandoDatosCC(textos.get(i));
                StringBuffer buffer = new StringBuffer();
                if(i>0){
                    doc.add(new Paragraph("\n"));
                }
                doc.add(new Paragraph("Ventas de: "+textos.get(i)));
                doc.add(new Paragraph("\n"));

                while(resultado.moveToNext()){
                    NombreCC=resultado.getString(1);
                    Precio=resultado.getString(2);
                    Cantidad=resultado.getInt(4);
                    PrecioU=Float.parseFloat(Precio);
                    Total=PrecioU*Cantidad;
                    t=String.format(Locale.ROOT,"%.2f",Total);
                    TotalD=TotalD+Total;
                    td=String.format(Locale.ROOT,"%.2f",TotalD);
                    doc.add(new Paragraph("Nombre: "+NombreCC));
                    doc.add(new Paragraph("Precio: "+Precio));
                    doc.add(new Paragraph("Cantidad: "+Cantidad));
                    doc.add(new Paragraph("Total $:"+t));
                    doc.add(new Paragraph("\n"));
                }

            }
            doc.add(new Paragraph("Total de ventas en el dia: $"+td));
            doc.add(new Paragraph("\n\n"));
            doc.add(new Paragraph("Generada: "+formattedDate ));
            doc.close();
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
       miBase.resetear();
    }

    //metodo que muestra los datos
    public  void verTodo(){
        //Llamando la funcion de la (miBaseD.java)
        //LlamandoDatos
        Cursor resultado= miBase.llamandoDatos();
        if(resultado.getCount()==0){
            //muestra el mensaje
            Mensaje("Advertencia","No hay datos que mostrar!");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        //Condicion while

        while(resultado.moveToNext()){
            //ListTotal.append("Nombre: "+resultado.getString(1)+"\n");

            //con esto agrego los resultados de la query en un arreglo, estos "textos" son
            //el texto de cada boton
            textos.add(resultado.getString(1));
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

    public void AgregarcomboM(){
        fabAgregarCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AgregarCombo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("textos",textos);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }


    //con esto creo los botones
    public void creador_botones(){
        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                //con estos parametros hago que el boton se adapte al parent
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        for(int i=0;i<miBase.cantidad_rows();i++){
          final Button btn = new Button(this);
            btn.setId(i+1);
            btn.setText(textos.get(i));
            btn.setLayoutParams(lprams);

            //con esto se hace el listener de cada boton de manera iterativa
            final int index = i+1;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idcomboCC=btn.getText().toString();
                    Log.i("TAG","The index is" + index);
                   Intent intent = new Intent(ctx, ContenidoCombo.class);
                   startActivity(intent);
                    finish();
                }
            });
            layout_general.addView(btn);

        }
    }

    public void FacturaAutomatica() {
        Date when = new Date(System.currentTimeMillis());
        long intervalo = 10000;

        try {
            Intent someIntent = new Intent(ctx, BroadcastReciever.class); // intent to be launched

            // note this could be getActivity if you want to launch an activity
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, someIntent, 0); // PendintIntent flag

            AlarmManager alarms = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + intervalo, intervalo, pendingIntent);
            CrearCarpeta();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void onBackPressed(){
        finish();
    }

}
