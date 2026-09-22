package com.example.aplicaciontaller.View

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.aplicaciontaller.ViewModel.ContadorViewModel

@Composable
fun ContadorScreen(
    viewModel: ContadorViewModel,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ){
        Text(text="Contador ${viewModel.contador}")

        Button(
            onClick = {
                viewModel.incrementar()
            }
        ) {
            Text("Incrementar")
        }
    }
}