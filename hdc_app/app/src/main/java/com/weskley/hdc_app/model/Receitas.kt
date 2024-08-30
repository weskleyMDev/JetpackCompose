package com.weskley.hdc_app.model

data class Receitas(
    val id: Int = 0,
    val titulo: String = "",
    val data: String = "",
    val medico: String = "",
    val paciente: String = "",
    val medicamentos: List<Map<String, String>> = emptyList(),
)
