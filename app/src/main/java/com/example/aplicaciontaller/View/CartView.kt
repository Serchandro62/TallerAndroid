package com.example.aplicaciontaller.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.aplicaciontaller.Components.CartProductCard
import com.example.aplicaciontaller.Components.MainScaffold
import com.example.aplicaciontaller.Components.Title
import com.example.aplicaciontaller.Components.spaceV
import com.example.aplicaciontaller.Data.database.AppDatabase
import com.example.aplicaciontaller.Data.repository.CartRepository
import com.example.aplicaciontaller.Data.repository.ProductRepository
import com.example.aplicaciontaller.ViewModel.CartViewModel
import com.example.aplicaciontaller.ViewModel.CartViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartView(
    onItemSelected: (Int) -> Unit,
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val cartRepository = CartRepository(database.cartItemDao())
    val productRepository = ProductRepository(database.productDao())
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartRepository, productRepository))
    val cartProducts by cartViewModel.cartProducts.collectAsState()
    
    MainScaffold(
        selectedItem = -1,
        onItemSelected = onItemSelected,
        showBottomBar = false,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Title(text = "Mi Cotización") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar al catálogo"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (cartProducts.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no has seleccionado vehículos",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartProducts) { cartProduct ->
                        CartProductCard(
                            cartProduct = cartProduct,
                            onIncrese = { cartViewModel.addToCart(cartProduct.productId.id) },
                            onDelete = { cartViewModel.removeFromCart(cartProduct.productId.id) }
                        )
                    }
                }
                
                spaceV(16)
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(20.dp)
                ) {
                    val total = cartProducts.sumOf { (it.productId.price * it.quantity).toDouble() }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Inversión Estimada:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$${String.format("%,.2f", total)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onCheckoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Agendar cita en concesionaria",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
