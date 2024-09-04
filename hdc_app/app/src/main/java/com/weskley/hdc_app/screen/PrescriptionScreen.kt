package com.weskley.hdc_app.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.twotone.AlarmAdd
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.EditMedicationDialog
import com.weskley.hdc_app.component.ShimmerEffectReceitas
import com.weskley.hdc_app.event.NotificationEvent
import com.weskley.hdc_app.model.Receitas
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import com.weskley.hdc_app.viewmodel.ReceitasViewModel
import kotlinx.coroutines.delay

@Composable
fun PrescriptionScreen(
    viewModel: ReceitasViewModel = hiltViewModel()
) {
    val receitas by viewModel.receitas.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(receitas) { receita ->
            ReceitaItem(receita = receita)
        }
    }

}

@Composable
fun ReceitaItem(receita: Receitas, viewModel: AlarmViewModel = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedMedIndex by remember { mutableStateOf(-1) }
    var medicamentoNome by remember { mutableStateOf("") }
    var medicamentoDose by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000)
        isLoading.value = false
    }
    if (!isLoading.value) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.LightGray),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = receita.titulo,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = receita.data,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .rotate(rotationState),
                        onClick = {
                            expandedState = !expandedState
                        }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop-Down Arrow",
                            tint = Color.Black
                        )
                    }
                }
                if (expandedState) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Médico: ${receita.medico}",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                    Text(
                        text = "Paciente: ${receita.paciente}",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    receita.medicamentos.forEachIndexed { index, medicamento ->
                        val nome = medicamento["nome"]
                        val dose = medicamento["dose"]
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (nome != null && dose != null) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "${index + 1}: $nome - $dose",
                                    fontSize = 19.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Text(text = "dados incompletos")
                            }
                            IconButton(
                                onClick = {
                                    selectedMedIndex = index
                                    medicamentoNome = nome ?: ""
                                    medicamentoDose = dose ?: ""
                                    showDialog = true
                                },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.TwoTone.AlarmAdd,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        ShimmerEffectReceitas()
    }
    EditMedicationDialog(
        showDialog = showDialog,
        medicamentoNome = medicamentoNome,
        medicamentoDose = medicamentoDose,
        onDismiss = { showDialog = false },
        onSave = {
            viewModel.onEvent(NotificationEvent.SaveNotification)
            showDialog = false
        },
        state = state
    )
}