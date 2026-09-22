package com.example.aplicaciontaller.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ContadorViewModel: ViewModel() {
    var contador by mutableStateOf(0)
        private set
    fun incrementar(){
        contador ++
    }
}