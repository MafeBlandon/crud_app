package com.example.restaurante


import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//controla las vistas
class EmpAdapter: RecyclerView.Adapter<EmpAdapter.EmpViewHolder>() {
    private var onClickItem:((EmpModel)->Unit)? =null//creamos una variable privada que recibe una funcion que a su vez recibe un modelo y no retorna nada
    //unit= no retorna nada


    private  var empList: ArrayList<EmpModel> = ArrayList()
    private  var onClickDeleteItem: ((EmpModel)->Unit)?= null
     private  var onClickUpdateItem: ((EmpModel)->Unit)?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_empleado,parent,false)
        return  EmpViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpViewHolder, position: Int) {//estructura la vista
        val itemList = empList[position]
        holder.bindView(itemList) // la infromacion se la pasa a la lista
        holder.itemView.setOnClickListener{onClickItem?.invoke((itemList))} //cuando le de click a la vista va a invocar el estudiante (coje el dato y lo guarda )
        //vista item estuden
        holder.tvEliminar.setOnClickListener { onClickDeleteItem?.invoke((itemList))}
        holder.tvActu.setOnClickListener { onClickUpdateItem?.invoke(itemList) }

    }

    override fun getItemCount() = empList.size
    fun addItems(items: ArrayList<EmpModel>){
        this.empList = items
        notifyDataSetChanged()
    }


    fun setOnClickItem(callback: (EmpModel)->Unit){//funcion que recibe la funcion  para enviarla a la variable  onClickItem y a su vez su modelo
        this.onClickItem = callback

    }//actu4

    fun setOnClickDeleteItem(callback: (EmpModel) -> Unit){
        this.onClickDeleteItem = callback
    }
    fun setonClickUpdateItem(callback: (EmpModel) -> Unit){
        this.onClickUpdateItem = callback
    }

    //viewHolder contiene todas las propiedades de la vista, vinculo entre el adpatador y las propiedades de las vista
    inner class EmpViewHolder(view: View): RecyclerView.ViewHolder(view){ //vista de tipo View ( es una clase)
        private var id = view.findViewById<TextView>(R.id.tvIdEmp)
        private var nombreEmp = view.findViewById<TextView>(R.id.tvNombreEmp)
        private var direccionEmp = view.findViewById<TextView>(R.id.tvDireccionEmp)
        private var oficioEmp = view.findViewById<TextView>(R.id.tvRolEmp)
        var tvEliminar = view.findViewById<ImageButton>(R.id.ibEliminar)
        var tvActu = view.findViewById<ImageButton>(R.id.ibActualizar)
        fun bindView(emp : EmpModel){ //modelo de datos
            id.text = emp.id.toString()
            nombreEmp.text = emp.nombre
            direccionEmp.text = emp.direccion
            oficioEmp.text = emp.oficio
        }

    }


}