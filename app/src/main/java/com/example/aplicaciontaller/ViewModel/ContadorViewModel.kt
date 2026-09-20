package com.example.aplicaciontaller.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ContadorViewModel: ViewModel() {
    //Indica que cuando el valor cambia, el compose debe actualizarse
    var contador by mutableStateOf(0)
        //indica que el valor solo puede modificarse en el view model
        private set
    fun incrementar(){
        contador ++
    }
}