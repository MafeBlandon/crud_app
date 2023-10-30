package com.example.restaurante


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.muddz.styleabletoast.StyleableToast

class Pantallados : AppCompatActivity() {


    private  lateinit var epNombreEmp: EditText
    private  lateinit var epDireccionEmp: EditText
    private  lateinit var epOficioEmp: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnListar: Button
    private lateinit var tvActu: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var SQLiteHelper: SQLHelper
    private  lateinit var empModel: EmpModel
    private  lateinit var tvEliminar: ImageButton
    private  var adapter: EmpAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantallados)

        initView()
        initRecyclerView()

        SQLiteHelper = SQLHelper(this) //instancia  al clase sql
        getEmpleado()//actualizar 1
        btnAgregar.setOnClickListener { addEmpleado() } //1
        btnListar.setOnClickListener { getEmpleado() }

        adapter?.setOnClickItem { //llamamos al la funcion que tiene el modelo
            StyleableToast.makeText(this,it.nombre,R.style.infoToast).show()
            epNombreEmp.setText(it.nombre)
            epDireccionEmp.setText(it.direccion)
            epOficioEmp.setText(it.oficio)
            empModel = it
        } //actu 5

        adapter?.setOnClickDeleteItem {
            it.id?.let { it1 -> deleteEmpleado(it1) }//bloque de codigo que le permite la captura de la informacion
        }
        adapter?.setonClickUpdateItem {
            it.id?.let { id ->
                val nombre = epNombreEmp.text.toString()
                val direccion = epDireccionEmp.text.toString()
                val oficio = epOficioEmp.text.toString()

                updateEmpleado(id,nombre, direccion,oficio)
            }
        }


    }

    private fun deleteEmpleado(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Deseas eliminar el empleado ? ")
        builder.setCancelable(true)
        builder.setPositiveButton("si"){dialog,_ ->
            SQLiteHelper.borrarEmpPorId(id)
            getEmpleado()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog, _ ->
            dialog.dismiss()

        }
        val alert = builder.create()
        alert.show()

    }


    private fun updateEmpleado(id: Int,nombre: String,direccion:String,oficio:String) {

        if(nombre.equals("") && direccion.equals("") && oficio.equals("")){
            StyleableToast.makeText(this,"selecciona un empleado abajo",R.style.errorToast).show()
            return
        }

        if (nombre == empModel.nombre && oficio == empModel.oficio && direccion == empModel.oficio){
            StyleableToast.makeText(this,"no se actualizo ",R.style.errorToast).show()
            clearText()
            return
        }

        val EmpActu = EmpModel(empModel.id,nombre,direccion,oficio)
        val status = SQLiteHelper.actualizarEmp(EmpActu)

        if (status > 0){
            StyleableToast.makeText(this,"Datos de Empleado actualizado ",R.style.successToast).show()
            clearText()
            getEmpleado()
        }else{
            StyleableToast.makeText(this,"Datos de Empleado no actualizado ",R.style.errorToast).show()

        }
    }

    private fun getEmpleado() {
        val empList = SQLiteHelper.getListEst()
        adapter?.addItems(empList)
    }

    private fun addEmpleado() {
        // 2 recolectamos la data  (capturamos lo que el usuario ingreso )
        val nombre = epNombreEmp.text.toString()
        val direccion = epDireccionEmp.text.toString()
        val oficio = epOficioEmp.text.toString()
        // 3 validacion simple si està vacio
        if(nombre.isEmpty() || oficio.isEmpty() ){
            Toast.makeText(this,"por favor ingrese todos los campos ", Toast.LENGTH_SHORT).show()


        }else{
            // 4 si no esta vacio creamos el estudiante
            val emp = EmpModel(null,nombre,direccion,oficio)
            val status = SQLiteHelper.guardarEmp(emp) // 5 llamamos el metodo para guardar en la base de dato

            // ultimo paso  si es mayor a çero es porque se creo en la base de datos
            if(status >- 1){
                Toast.makeText(this,"Se agrego un nuevo empleado ",Toast.LENGTH_SHORT).show() //Toast es una alerta
                clearText()// actu 3
                getEmpleado()
            }else{
                Toast.makeText(this,"Ocurrio un error al guardar  ",Toast.LENGTH_SHORT).show()
            }

        }

    }

    // actu 2 permite limpiar el texto
    private fun clearText(){
        epNombreEmp.setText("")
        epOficioEmp.setText("")
        epDireccionEmp.setText("")
        epNombreEmp.requestFocus()

    }
    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EmpAdapter()
        recyclerView.adapter = adapter
    }

    private  fun initView(){  //inicializamos las variables
        epNombreEmp = findViewById(R.id.epNombreEmp) //etnombre tiene las propiedades de etnombre
        epDireccionEmp = findViewById(R.id.epDireccionEmp)
        epOficioEmp = findViewById(R.id.epOficioEmp)
        btnAgregar = findViewById(R.id.btnGuardar)
        btnListar = findViewById(R.id.btnMostar)

        recyclerView = findViewById(R.id.rvPrincipall)
    }


}