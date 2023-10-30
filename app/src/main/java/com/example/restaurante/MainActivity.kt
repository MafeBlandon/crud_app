package com.example.restaurante


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent


class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // funcion que nois permite que al hacer click en el boton nos envia a otra vista


        val btn: Button = findViewById(R.id.btnIngresar)
        btn.setOnClickListener {
            val intent: Intent = Intent(this,Pantallados::class.java)
            startActivity(intent)

        }


    }

}