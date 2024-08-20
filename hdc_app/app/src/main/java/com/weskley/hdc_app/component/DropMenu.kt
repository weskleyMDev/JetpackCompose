package com.weskley.hdc_app.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.state.NotificationEvent
import com.weskley.hdc_app.state.NotificationState
import com.weskley.hdc_app.viewmodel.AlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropMenu(
    state: NotificationState,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val list = listOf("", "Paracetamol", "Dipirona", "Ibuprofeno", "Estomazil")
    var selectedText by remember {
        mutableStateOf(list[0])
    }
    var isExpanded by remember { mutableStateOf(false) }
    val img = remember {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                textStyle = MaterialTheme.typography.bodyLarge,
                value = state.title,
                onValueChange = {
                    viewModel.onEvent(NotificationEvent.SetTitle(it))
                },
                minLines = 1,
                maxLines = 2,
                placeholder = { Text(text = "Selecione ou digite")},
                label = { Text(text = "Medicamento") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                supportingText = {
                    Text(
                        text = "${state.body.length} / 50",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )
            if (state.title == "") {
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    list.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = list[index]
                                isExpanded = false
                                viewModel.onEvent(NotificationEvent.SetTitle(selectedText))
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            img.value = when (state.title) {
                "Paracetamol" -> R.drawable.paracetamol
                "Dipirona" -> R.drawable.dipirona
                "Ibuprofeno" -> R.drawable.ibuprofeno
                "Estomazil" -> R.drawable.estomazil
                else -> R.drawable.logo_circ_branco
            }
            viewModel.onEvent(NotificationEvent.SetImage(img.value))
        }
    }
}