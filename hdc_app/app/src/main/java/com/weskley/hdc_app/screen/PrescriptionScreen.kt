package com.weskley.hdc_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.model.Receitas
import com.weskley.hdc_app.viewmodel.ReceitasViewModel

@Composable
fun PrescriptionScreen(
    viewModel: ReceitasViewModel = hiltViewModel()
) {
    val receitas by viewModel.receitas.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(receitas) { receita ->
                ReceitaItem(receita = receita)
            }
        }
    }
}

@Composable
fun ReceitaItem(receita: Receitas) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .background(Color.LightGray)) {
            Text(text = receita.titulo, fontWeight = FontWeight.Bold)
            Text(text = receita.data)
            Text(text = "Médico: ${receita.medico}")
            Text(text = "Paciente: ${receita.paciente}")

            receita.medicamentos.forEachIndexed { index, medicamento ->
                val nome = medicamento["nome"]
                val dose = medicamento["dose"]
                Row {
                    if (nome != null && dose != null) {
                        Text(text = "${index + 1}: $nome - $dose")
                    } else {
                        Text(text = "dados incompletos")
                    }
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Editar")
                    }
                }
            }
        }
    }
}