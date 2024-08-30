package com.weskley.hdc_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.weskley.hdc_app.model.Receitas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReceitasViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _receitas = MutableStateFlow<List<Receitas>>(emptyList())
    val receitas = _receitas.asStateFlow()

    init {
        fetchReceitas()
    }

    private fun fetchReceitas() {
        firestore.collection("receitas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ReceitasViewModel", "Error fetching receitas", error)
                    return@addSnapshotListener
                }
                val receitasList = snapshot?.documents?.mapNotNull { document ->
                    val id = document.getLong("id")?.toInt() ?: 0
                    val titulo = document.getString("titulo") ?: ""
                    val data = document.getString("data") ?: ""
                    val medico = document.getString("medico") ?: ""
                    val paciente = document.getString("paciente") ?: ""

                    val medicamentosRaw = document.get("medicamentos") as? List<*> ?: emptyList<Any>()
                    val medicamentos = medicamentosRaw.mapNotNull { item ->
                        (item as? Map<*, *>)?.let { map ->
                            val nome = map["nome"] as? String ?: ""
                            val dose = map["dose"] as? String ?: ""
                            mapOf("nome" to nome, "dose" to dose)
                        }
                    }

                    Receitas(id, titulo, data, medico, paciente, medicamentos)
                } ?: emptyList()

                _receitas.value = receitasList
            }
    }
}