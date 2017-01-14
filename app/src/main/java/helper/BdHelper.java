package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.galfo.prueba_combos.MainActivity;

/**
 * Created by galfo on 19/12/2016.
 */

public class BdHelper extends SQLiteOpenHelper {
    //para saber que boton se presiono
    String idcomboCC= MainActivity.idcomboCC;

    //Nombre de la base de datos
    private static final String DATABASE_NAME ="BaseData.db";

    //Declarando version de la Bdd
    private static final int DATABASE_VERSION=1;

    //Nombre de las tablas
    private static final String TABLE_COMBOSGRAL="CombosGral";
    private static final String TABLE_CONTENIDOCOMBOS="ContenidoCombos";

    //Columnas de la tabla de combos general
    private static final String COL_COMBOSGRAL_ID="idcombo";
    private static final String COL_COMBOSGRAL_NOMBRE="nombreComboGral";

    //Columnas de la tabla del contenido del combo
    private static final String COL_CONTENIDOCOMBOS_ID="id";
    private static final String COL_CONTENIDOCOMBOS_NOMBRE="nombreComboContenido";
    private static final String COL_CONTENIDOCOMBOS_PRECIO="precio";
    private static final String COL_CONTENIDOCOMBOS_IDCOMBO="idcomboCC";
    private static final String COL_CONTENIDOCOMBOS_CANTDIA="cantdia";
    private static final String COL_CONTENIDOCOMBOS_TOTAL="total";

    //query para la creacion de tablas
    //creando la tabla de CombosGral
    private static final String CREATE_TABLE_COMBOSGRAL = "CREATE TABLE "+ TABLE_COMBOSGRAL+"( "+ COL_COMBOSGRAL_ID +" INTEGER PRIMARY "+ "KEY AUTOINCREMENT,"+COL_COMBOSGRAL_NOMBRE+" TEXT"+" ) ;";

    //creando la tabla de ContenidoCombos
    private static final String CREATE_TABLE_CONTENIDO_COMBOS = "CREATE TABLE "+ TABLE_CONTENIDOCOMBOS+"( "+ COL_CONTENIDOCOMBOS_ID+" INTEGER PRIMARY "+ "KEY AUTOINCREMENT,"+COL_CONTENIDOCOMBOS_NOMBRE+ " TEXT, "+
            COL_CONTENIDOCOMBOS_PRECIO+ " BLOB, "+COL_CONTENIDOCOMBOS_IDCOMBO+" TEXT, "+COL_CONTENIDOCOMBOS_CANTDIA+" INT );";

    public BdHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(CREATE_TABLE_COMBOSGRAL);
        db.execSQL(CREATE_TABLE_CONTENIDO_COMBOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IS EXISTS"+TABLE_COMBOSGRAL);
        db.execSQL("DROP TABLE IS EXISTS"+TABLE_CONTENIDOCOMBOS);
        onCreate(db);
    }

    //metodo de insercion de datos
    public Boolean insertData (String nombreComboGral){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_COMBOSGRAL_NOMBRE,nombreComboGral);
        long result = db.insert(TABLE_COMBOSGRAL, null,contentValues);
        //Condicion
        if (result == -1)
            return false;
        else
            return true;
    }

    //Metodo que llama a los datos ingresados
    public Cursor llamandoDatos(){
        SQLiteDatabase db= this.getWritableDatabase();

        //Llamando al cursor
        Cursor resultado= db.rawQuery(" select * from "+TABLE_COMBOSGRAL,null);

        return resultado;
    }


    public Integer cantidad_rows(){
        int cantidadTotal=0;

        String sql = " select count (*) from "+TABLE_COMBOSGRAL;
        Cursor cursor=getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            cantidadTotal=cursor.getInt(0);
        }

        cursor.close();
        return cantidadTotal;
    }

    public void EditC(String NuevoN,String ViejoN){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" UPDATE "+TABLE_COMBOSGRAL+" SET "+COL_COMBOSGRAL_NOMBRE+" = '"+NuevoN+"' WHERE "+COL_COMBOSGRAL_NOMBRE+" = '"+ViejoN+"' ;");
        db.execSQL(" UPDATE "+TABLE_CONTENIDOCOMBOS+" SET "+COL_CONTENIDOCOMBOS_IDCOMBO+" = '"+NuevoN+"' WHERE "+COL_CONTENIDOCOMBOS_IDCOMBO+" = '"+ViejoN+"' ;");
    }

    public void BorrarC(String NombreC){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" DELETE FROM "+TABLE_COMBOSGRAL+" WHERE "+COL_COMBOSGRAL_NOMBRE+" = '"+NombreC+"' ;");
        db.execSQL(" DELETE FROM "+TABLE_CONTENIDOCOMBOS+" WHERE "+COL_CONTENIDOCOMBOS_IDCOMBO+" = '"+NombreC+"' ;");
    }
    //METODOS PARA LA TABLA DE CONTENIDO COMBO

    //metodo de insercion de datos
    public Boolean insertDataCC (String nombreComboContenido,String precio,String idcomboCC){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTENIDOCOMBOS_NOMBRE,nombreComboContenido);
        contentValues.put(COL_CONTENIDOCOMBOS_PRECIO,precio);
        contentValues.put(COL_CONTENIDOCOMBOS_IDCOMBO,idcomboCC);
        contentValues.put(COL_CONTENIDOCOMBOS_CANTDIA,0);
        long result = db.insert(TABLE_CONTENIDOCOMBOS, null,contentValues);
        //Condicion
        if (result == -1)
            return false;
        else
            return true;
    }

    //Metodo que llama a los datos ingresados
   public Cursor llamandoDatosCC(String Combo){
        SQLiteDatabase db= this.getWritableDatabase();

        //Llamando al cursor
        Cursor resultado= db.rawQuery(" select * from "+TABLE_CONTENIDOCOMBOS+" where "+COL_CONTENIDOCOMBOS_IDCOMBO+" = '"+Combo+"' ;",null);

        return resultado;
    }

    //Con esto cuento la cantidad de registros que hay para crear los botones con sus nombres
    public Integer cantidad_rowsCC(){
        int cantidadTotal=0;

        String sql = " select count (*) from "+TABLE_CONTENIDOCOMBOS+" where "+COL_CONTENIDOCOMBOS_IDCOMBO+" = '"+idcomboCC+"' ;";
        Cursor cursor=getReadableDatabase().rawQuery(sql,null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            cantidadTotal=cursor.getInt(0);
        }

        cursor.close();
        return cantidadTotal;
    }

    public void BorrarContComb(String ContenidoN){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" DELETE FROM "+TABLE_CONTENIDOCOMBOS+" WHERE "+COL_CONTENIDOCOMBOS_NOMBRE+" = '"+ContenidoN+"' ;");
    }

    public void EditarCompletoCC(String NuevoN,String ViejoN,String PrecioN){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" UPDATE "+TABLE_CONTENIDOCOMBOS+" SET "+COL_CONTENIDOCOMBOS_NOMBRE+" = '"+NuevoN+"', "+COL_CONTENIDOCOMBOS_PRECIO+" = '"+PrecioN+"' WHERE "
                +COL_CONTENIDOCOMBOS_NOMBRE+" = '"+ViejoN+"' ;");
    }

    public void SoloNombreCC(String NuevoN,String ViejoN){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" UPDATE "+TABLE_CONTENIDOCOMBOS+" SET "+COL_CONTENIDOCOMBOS_NOMBRE+" = '"+NuevoN+"' WHERE " +COL_CONTENIDOCOMBOS_NOMBRE+" = '"+ViejoN+"' ;");
    }

    public void SoloPrecioCC(String ViejoN,String PrecioN){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" UPDATE "+TABLE_CONTENIDOCOMBOS+" SET "+COL_CONTENIDOCOMBOS_PRECIO+" = '"+PrecioN+"' WHERE " +COL_CONTENIDOCOMBOS_NOMBRE+" = '"+ViejoN+"' ;");
    }

    //METODOS PARA LA FACTURA DE LA TABLA DEL CONTENIDO DEL COMBO
    public void AgNCant(String NombreCC,int NuevaCant){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" UPDATE "+TABLE_CONTENIDOCOMBOS+" SET "+COL_CONTENIDOCOMBOS_CANTDIA+" = "+NuevaCant+" WHERE "+COL_CONTENIDOCOMBOS_NOMBRE+" ='"+NombreCC+"' ;");
    }

    public void resetear(){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(" UPDATE "+TABLE_CONTENIDOCOMBOS+" SET "+COL_CONTENIDOCOMBOS_CANTDIA+" = 0; ");

    }
}
