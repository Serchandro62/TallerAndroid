package com.example.aplicaciontaller.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicaciontaller.Components.MainScaffold
import com.example.aplicaciontaller.Components.Title
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicaciontaller.Components.ProductCardHome
import com.example.aplicaciontaller.Data.database.AppDatabase
import com.example.aplicaciontaller.Data.repository.CartRepository
import com.example.aplicaciontaller.Data.repository.ProductRepository
import com.example.aplicaciontaller.ViewModel.CartViewModel
import com.example.aplicaciontaller.ViewModel.CartViewModelFactory
import com.example.aplicaciontaller.ViewModel.ProductViewModel
import com.example.aplicaciontaller.ViewModel.ProductViewModelFactory
import com.example.aplicaciontaller.Components.spaceV

@Composable
fun FavoritesView(onItemSelected: (Int) -> Unit) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val productRepository = ProductRepository(database.productDao())
    val cartRepository = CartRepository(database.cartItemDao())

    val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(productRepository))
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartRepository, productRepository))

    val products by productViewModel.products.collectAsState()
    val favoriteProducts = products.filter { it.isFavorite }

    MainScaffold(
        selectedItem = 2,
        onItemSelected = onItemSelected,
        topBar = { Title(text = "Mis Autos Favoritos") }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (favoriteProducts.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
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
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favoritos Vacíos",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                    spaceV(24)
                    Text(
                        text = "Aún no tienes vehículos favoritos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    spaceV(8)
                    Text(
                        text = "Explora nuestro catálogo y guarda los que más te gusten",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(favoriteProducts) { product ->
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
