package com.example.aplicaciontaller.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicaciontaller.Data.model.Category
import com.example.aplicaciontaller.Data.repository.CategoryRepository
import com.example.aplicaciontaller.R
import com.example.aplicaciontaller.ui.theme.ultra_l_blue
import com.example.aplicaciontaller.ui.theme.ultra_l_green
import com.example.aplicaciontaller.ui.theme.ultra_l_purple
import com.example.aplicaciontaller.ui.theme.ultra_l_red
import com.example.aplicaciontaller.ui.theme.ultra_l_yellow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val categoryRepository: CategoryRepository): ViewModel() {
    private val _categories= MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories
    init {
        viewModelScope.launch {
            insertInitialCategories()
            loadCategories()
        }
    }
    private suspend fun loadCategories(){
        _categories.value = categoryRepository.getCategories()
    }
    private suspend fun insertInitialCategories(){
        if(categoryRepository.countCategories()==0){
            val categories = listOf(
                Category(
                    name = "Camionetas",
                    image = R.drawable.camioneta,
                    color = ultra_l_green
                ),
                Category(
                    name = "Compactos",
                    image = R.drawable.compacto,
                    color = ultra_l_yellow
                ),
                Category(
                    name = "4x4",
                    image = R.drawable.cuatro_x_cuatro,
                    color = ultra_l_blue
                ),
                Category(
                    name = "PickUps",
                    image = R.drawable.pickup,
                    color = ultra_l_red
                ),
                Category(
                    name = "Deportivos",
                    image = R.drawable.deportivo,
                    color = ultra_l_purple
                )
            )
            categoryRepository.insertCategories(categories)
        }
    }
}