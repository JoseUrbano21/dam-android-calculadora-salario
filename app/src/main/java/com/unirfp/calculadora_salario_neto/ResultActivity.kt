package com.unirfp.calculadora_salario_neto

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Se recoge los valores enviados por la primera vista
        val salarioBruto = intent.getDoubleExtra("Salario bruto", 0.0)
        val salarioNeto = intent.getDoubleExtra("Salario neto", 0.0)
        val irpf = intent.getDoubleExtra("IRPF", 0.0)
        val deducAplicadas = intent.getStringExtra("Deducciones aplicadas")

        // Se muestran los valores en la vista
        val textoSalarioBruto = findViewById<TextView>(R.id.resultSalarioBruto)
        textoSalarioBruto.text = "SALARIO BRUTO: %.2f€".format(salarioBruto)

        val textoSalarioNeto = findViewById<TextView>(R.id.resultSalarioNeto)
        textoSalarioNeto.text = "SALARIO NETO: %.2f€".format(salarioNeto)

        val textoIrpf = findViewById<TextView>(R.id.resultIrpf)
        textoIrpf.text = "RETENCIÓN IRPF: %.2f%%".format(irpf)

        val textoDeducciones = findViewById<TextView>(R.id.posiblesDeduc)
        textoDeducciones.text = "DEDUCCIONES APLICADAS:\n$deducAplicadas"
    }
}