package com.example.restaurante

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class SQLHelper(context: Pantallados): SQLiteOpenHelper(context, DB_NAMEdos, null, DB_VERSIONdos){  //abre la conexion a la base de datos en este contexto , el factory es cuando tenemos la base de datos en otra parte

    companion object{//se va a encapsular y los objetos que se mantiene ahi son constantes  se va a poder compartir  (variables de tipo const)

        private const val DB_VERSIONdos = 1 //la variable constante deben ser mayusculas
        private const val DB_NAMEdos ="empleado.db"//nombre de la base de datos
        private const val DB_TABLEdos ="tbl_empleado"//nombre de la tabla
        private const val ID_EMP ="id"//nombre de la tabla
        private const val NOMBRE_EMP ="nombre"//nombre de la tabla
        private const val DIRECCION_EMP ="direccion"//nombre de la tabla
        private const val OFICIO_EMP ="oficio"//nombre de la tabla



    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = (
                "CREATE TABLE $DB_TABLEdos("+
                        "$ID_EMP INTEGER PRIMARY  KEY AUTOINCREMENT,"+
                        "$NOMBRE_EMP TEXT," +
                        "$DIRECCION_EMP TEXT," +
                        "$OFICIO_EMP TEXT)"
                )    //escrip que me permite la creacion de la tabla
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $DB_TABLEdos") //cuando elimine la aplicacion elimine los dato y lo vuelva a crear
        onCreate(db)
    }
    @SuppressLint("Range")//esta suprimiento o limitando la libreria lint que es la que nos ayuda a detectar errores
    fun getListEst(): ArrayList<EmpModel>{
        val listaEmp: ArrayList<EmpModel> = ArrayList() //variale que vamos a devolver
        val sql = "SELECT*FROM $DB_TABLEdos ORDER BY $ID_EMP  DESC" //sentencia sql
        val db = this.readableDatabase //va leer la base de datos
        val cursor: Cursor //creamos el cursor  variable de interacin que recorre la informacion de l base dedatos  permite guardar la respuesta de la base de dato
        //si genera algun error que lo capture
        try{
            cursor = db.rawQuery(sql,null)
        }catch (e: Exception){
            e.printStackTrace()
            return  ArrayList()

        }

        var id: Int
        var nombre: String
        var direccion: String
        var oficio: String

        //se empieza el ciclo para recorrer la informacon
        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex(ID_EMP))
                nombre = cursor.getString(cursor.getColumnIndex(NOMBRE_EMP))
                direccion = cursor.getString(cursor.getColumnIndex(DIRECCION_EMP))
                oficio = cursor.getString(cursor.getColumnIndex(OFICIO_EMP))

                val emp = EmpModel(id,nombre,direccion,oficio)
                listaEmp.add(emp)
            }while(cursor.moveToNext())
        }
        return listaEmp
    }

    fun guardarEmp(emp:EmpModel): Long{
        // 6 le decimos a la base de datos si vamos a leer o vamos a escribir los datos


        val db = this.writableDatabase //le indicamos que se va a escribir en la base de datos

        //contenedor de valores  segun el modelo
        val contentValues = ContentValues() //agregamos cad auno de los campos que vamos a guardar en la base de datos

        contentValues.put(ID_EMP,emp.id) //asignamos a que valor corresponde cada columna
        contentValues.put(NOMBRE_EMP,emp.nombre)
        contentValues.put(DIRECCION_EMP,emp.direccion)
        contentValues.put(OFICIO_EMP,emp.oficio)

        //creamos una variable para guardar el resultado
        val success = db.insert(DB_TABLEdos,null,contentValues)

        db.close()//cerramos la conexion

        return success //devolvemos respuesta

    }//guardamos un esttudiante

    fun actualizarEmp(emp: EmpModel): Int{
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOMBRE_EMP, emp.nombre)
        contentValues.put(DIRECCION_EMP, emp.direccion)
        contentValues.put(OFICIO_EMP, emp.oficio)

        val success = db.update(DB_TABLEdos,contentValues,"id="+emp.id,null)
        db.close()
        return success
    }

    fun borrarEmpPorId(id: Int):Int{
        val db = this.writableDatabase
        val success = db.delete(DB_TABLEdos,"$ID_EMP = $id", null)
        db.close()
        return success
    }
}