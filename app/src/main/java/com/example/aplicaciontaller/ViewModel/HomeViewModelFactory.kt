package com.example.aplicaciontaller.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicaciontaller.Data.repository.CategoryRepository

class HomeViewModelFactory(
    private val categoryRepository: CategoryRepository
): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(
        modelClass: Class<T>
    ):T{
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(
                categoryRepository
            )as T
        }
        throw IllegalArgumentException(
            "No existe el view model"
        )
    }
}