package com.example.aplicaciontaller.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicaciontaller.Data.model.Product
import com.example.aplicaciontaller.Data.repository.ProductRepository
import com.example.aplicaciontaller.R
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository): ViewModel() {
    //Obtener todos los productos
    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),emptyList())
    init {
        checkAndInsertInitialData()
    }

    private fun checkAndInsertInitialData() {
        viewModelScope.launch {
            // verificar los datos iniciales, para que en caso de no tener productos, se inserten los vehículos
            if(repository.getProductCount()==0){
                val initialProducts = listOf(
                    Product(name = "Camioneta SUV Premium", image = R.drawable.camioneta, price = 485000.0f, measure = "unidad", isOffer = true, isFavorite = false),
                    Product(name = "Auto Compacto Urbano", image = R.drawable.compacto, price = 275000.0f, measure = "unidad", isOffer = false, isFavorite = false),
                    Product(name = "Todoterreno 4x4 Offroad", image = R.drawable.cuatro_x_cuatro, price = 560000.0f, measure = "unidad", isOffer = true, isFavorite = false),
                    Product(name = "Deportivo Turbo GT", image = R.drawable.deportivo, price = 720000.0f, measure = "unidad", isOffer = true, isFavorite = false),
                    Product(name = "PickUp Carga Heavy Duty", image = R.drawable.pickup, price = 510000.0f, measure = "unidad", isOffer = false, isFavorite = false)
                )
                initialProducts.forEach { repository.insertProduct(it) }
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(product.id, !product.isFavorite)
        }
    }
}