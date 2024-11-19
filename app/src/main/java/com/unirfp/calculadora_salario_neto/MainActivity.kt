package com.unirfp.calculadora_salario_neto

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del elemento para seleccionar la EDA
        val edad = findViewById<NumberPicker>(R.id.edadNumberPicker)
        edad.minValue = 18
        edad.maxValue = 66
        edad.wrapSelectorWheel = false

        // Configuración del elemento para seleccionar el ESTADO CIVIL
        val opcionesEstadoCivil = arrayOf("Soltero/a", "Casado/a")
        val estadoCivil = findViewById<Spinner>(R.id.desplegableEstadoCivil)
        // Adaptador para "meter" el array de opciones dentro del spinner (desplegable)
        val adaptadorEstadoCivil =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesEstadoCivil)
        // Comportamiento del spinner al pulsar el desplegable
        adaptadorEstadoCivil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        estadoCivil.adapter = adaptadorEstadoCivil

        // Configuración del elemento para seleccionar el NÚMERO DE HIJOS
        val hijos = findViewById<NumberPicker>(R.id.hijosNumberPicker)
        hijos.minValue = 0
        hijos.maxValue = 10
        hijos.wrapSelectorWheel = false

        // Configuración del elemento para seleccionar el GRADO DE DISCAPACIDAD
        val inputGradoDiscapacidad = findViewById<EditText>(R.id.inputGradoDiscapacidad)

        // Configuración del elemento para seleccionar el GRUPO PROFESIONAL
        val opcionesGrupoProf = arrayOf("Grupo I", "Grupo II")
        val grupoProf = findViewById<Spinner>(R.id.desplegableGrupoProfesional)
        // Adaptador para "meter" el array de opciones dentro del spinner (desplegable)
        val adaptadorGrupoProf =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesGrupoProf)
        // Comportamiento del spinner al pulsar el desplegable
        adaptadorGrupoProf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        grupoProf.adapter = adaptadorGrupoProf

        // Configuración del elemento para seleccionar el SALARIO BRUTO ANUAL
        val inputSalarioBruto = findViewById<EditText>(R.id.inputSalarioBruto)

        // Configuración del elemento para seleccionar el NÚMERO DE PAGAS
        val opcionesPagas = arrayOf("12 pagas", "14 pagas")
        val pagas = findViewById<Spinner>(R.id.desplegableNumeroPagas)
        // Adaptador para "meter" el array de opciones dentro del spinner (desplegable)
        val adaptadorPagas = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesPagas)
        // Comportamiento del spinner al pulsar el desplegable
        adaptadorPagas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pagas.adapter = adaptadorPagas

        // Configuración del botón "Calcular"
        val botonCalcular = findViewById<Button>(R.id.botonCalcular)

        // Aplicación de la lógica al clickar el botón
        botonCalcular.setOnClickListener {
            val edadSeleccionada = edad.value
            val estadoCivilSeleccionado = estadoCivil.selectedItem.toString()
            val hijosSeleccionado = hijos.value
            val discapacidadSeleccionado = inputGradoDiscapacidad.text.toString().toIntOrNull() ?: 0
            val grupoProfSeleccionado = grupoProf.selectedItem.toString()
            val salarioBrutoSeleccionado = inputSalarioBruto.text.toString().toDoubleOrNull() ?: 0.0
            val pagasSeleccionado = pagas.selectedItem
            var pagasCalculado = 0

            // Aplicamos la lógica para determinar qué valores tendrán la paga y el irpf en función de la selección del usuario
            if (pagasSeleccionado == "12 pagas") {
                pagasCalculado = 12
            } else {
                pagasCalculado = 14
            }

            var irpf = 0.0
            var deducEdad = 0.0
            var deducCivil = 0.0
            var deducHijos = 0.0
            var deducDiscp = 0.0
            var deducGrupo = 0.0
            var deducAplicadas = StringBuilder()

            if (salarioBrutoSeleccionado <= 25000.0) {
                irpf = 5.0
            } else {
                irpf = 15.0
            }
            // Posibles deducciones aplicables
            if (edadSeleccionada >= 40) {
                deducEdad = 2.0
                deducAplicadas.append("Deducción por edad: $deducEdad%\n")
            }
            if (estadoCivilSeleccionado == "Casado/a") {
                deducCivil = 2.0
                deducAplicadas.append("Deducción por estado civil: $deducCivil%\n")
            }
            if (hijosSeleccionado <= 2) {
                deducHijos = 5.0
                deducAplicadas.append("Deducción por hijos: $deducHijos%\n")
            }
            if (discapacidadSeleccionado >= 33) {
                deducDiscp = 5.0
                deducAplicadas.append("Deducción por discapacidad: $deducDiscp%\n")

            }
            if (grupoProfSeleccionado == "Grupo I") {
                deducGrupo = 7.0
                deducAplicadas.append("Deducción por grupo profesional: $deducGrupo%\n")
            }

            // Variable donde se guarda el resultado de la fórmula aplicada para calcular el salario neto
            val salarioNeto =
                (salarioBrutoSeleccionado + (salarioBrutoSeleccionado / pagasCalculado)) - (salarioBrutoSeleccionado * irpf / 100) -
                        (salarioBrutoSeleccionado * deducCivil / 100) - (salarioBrutoSeleccionado * deducHijos / 100) -
                        (salarioBrutoSeleccionado * deducDiscp / 100) - (salarioBrutoSeleccionado * deducGrupo / 100) -
                        (salarioBrutoSeleccionado * deducEdad / 100)

            // Envío de los valores mediante Intent a la segunda vista
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("Salario bruto", salarioBrutoSeleccionado)
            intent.putExtra("Salario neto", salarioNeto)
            intent.putExtra("IRPF", irpf)
            intent.putExtra("Deducciones aplicadas", deducAplicadas.toString())
            startActivity(intent)
        }
    }
}
