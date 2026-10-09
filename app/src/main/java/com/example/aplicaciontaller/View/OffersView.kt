package com.example.aplicaciontaller.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicaciontaller.Components.MainScaffold
import com.example.aplicaciontaller.Components.ProductCardHome
import com.example.aplicaciontaller.Components.Title
import com.example.aplicaciontaller.Components.spaceH
import com.example.aplicaciontaller.Components.spaceV
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

    val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(productRepository))
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartRepository, productRepository))

    val products by productViewModel.products.collectAsState()
    val offerProducts = products.filter { it.isOffer }

    MainScaffold(
        selectedItem = 1,
        onItemSelected = onItemSelected,
        topBar = { Title(text = "Promociones Automotrices") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (offerProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = "Sin ofertas",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        }
                        spaceV(24)
                        Text(
                            text = "No hay ofertas de vehículos",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        spaceV(8)
                        Text(
                            text = "Vuelve pronto para conocer nuestras promociones",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                spaceV(16)
                Text(
                    text = "Vehículos en Promoción",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.Start)
                )
                spaceV(8)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(offerProducts) { product ->
                        ProductCardHome(
                            product = product,
                            onAddClick = { cartViewModel.addToCart(product.id) },
                            onFavoriteClick = { productViewModel.toggleFavorite(product) }
                        )
                    }
                }
            }
        }
    }
}
