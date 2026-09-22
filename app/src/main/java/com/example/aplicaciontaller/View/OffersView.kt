package com.example.aplicaciontaller.View

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicaciontaller.Components.MainScaffold
import com.example.aplicaciontaller.Components.ProductCardHome
import com.example.aplicaciontaller.Components.Title
import com.example.aplicaciontaller.Components.spaceH
import com.example.aplicaciontaller.Data.database.AppDatabase
import com.example.aplicaciontaller.Data.repository.CartRepository
import com.example.aplicaciontaller.Data.repository.ProductRepository
import com.example.aplicaciontaller.ViewModel.CartViewModel
import com.example.aplicaciontaller.ViewModel.CartViewModelFactory
import com.example.aplicaciontaller.ViewModel.ProductViewModel
import com.example.aplicaciontaller.ViewModel.ProductViewModelFactory

@Composable
fun OffersView(onItemSelected: (Int) -> Unit) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val productRepository = ProductRepository(database.productDao())
    val cartRepository = CartRepository(database.cartItemDao())

    val productViewModel: ProductViewModel =
        viewModel(factory = ProductViewModelFactory(productRepository))
    val cartViewModel: CartViewModel =
        viewModel(factory = CartViewModelFactory(cartRepository, productRepository))

    val products by productViewModel.products.collectAsState()
    val offerProducts = products.filter { it.isOffer }

    MainScaffold(
        selectedItem = 1,
        onItemSelected = onItemSelected,
        topBar = { Title(text = "Ofertas Especiales") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (offerProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay ofertas de vehículos disponibles", fontSize = 20.sp, color = Color.Gray)
                }
            } else {
                Text(
                    text = "Vehículos en Promoción",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(offerProducts) { product ->
                        ProductCardHome(
                            product = product,
                            onAddClick = { cartViewModel.addToCart(product.id) }
                        )
                        spaceH(8)
                    }
                }
            }
        }
    }
}
