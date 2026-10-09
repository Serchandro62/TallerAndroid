package com.example.aplicaciontaller.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicaciontaller.Components.CardViewHome
import com.example.aplicaciontaller.Components.CartButton
import com.example.aplicaciontaller.Components.MainScaffold
import com.example.aplicaciontaller.Components.ProductCardHome
import com.example.aplicaciontaller.Components.SearchBarComponent
import com.example.aplicaciontaller.Components.Title
import com.example.aplicaciontaller.Components.cardCategoryHome
import com.example.aplicaciontaller.Components.spaceV
import com.example.aplicaciontaller.Data.database.AppDatabase
import com.example.aplicaciontaller.Data.repository.CartRepository
import com.example.aplicaciontaller.Data.repository.CategoryRepository
import com.example.aplicaciontaller.Data.repository.ProductRepository
import com.example.aplicaciontaller.R
import com.example.aplicaciontaller.ViewModel.CartViewModel
import com.example.aplicaciontaller.ViewModel.CartViewModelFactory
import com.example.aplicaciontaller.ViewModel.HomeViewModel
import com.example.aplicaciontaller.ViewModel.HomeViewModelFactory
import com.example.aplicaciontaller.ViewModel.ProductViewModel
import com.example.aplicaciontaller.ViewModel.ProductViewModelFactory
import com.example.aplicaciontaller.ui.theme.AplicacionTallerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit,
    onCartClick: () -> Unit = {}
) {
    MainScaffold(
        selectedItem = 0,
        onItemSelected = onItemSelected,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Title(text = "AGENCIA AUTO")
                    }
                )
                SearchBarComponent()
            }
        },
        floatingActionButton = {
            CartButton(onClick = onCartClick)
        }
    ) { padding ->
        ContentHomeView(Modifier.padding(padding))
    }
}

@Composable
fun ContentHomeView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val categoryDao = database.categoryDao()
    val categoryRepository = CategoryRepository(categoryDao)
    val productRepository = ProductRepository(database.productDao())
    val cartRepository = CartRepository(database.cartItemDao())

    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(categoryRepository))
    val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(productRepository))
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartRepository, productRepository))

    val categories by homeViewModel.categories.collectAsState()
    val products by productViewModel.products.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        spaceV(8)

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            CardViewHome(
                title = "Estrena sin límites",
                description = "Plan a 36 meses sin intereses\nSeguro incluido",
                buttonText = "Cotizar Ahora",
                image = R.drawable.auto_banner
            )
        }

        spaceV(24)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Segmentos",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        spaceV(12)

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                cardCategoryHome(
                    categoria = category.name,
                    imagen = category.image,
                    color = category.color
                )
            }
        }

        spaceV(24)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Modelos Destacados",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Ver todos",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        
        spaceV(12)

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCardHome(
                    product = product,
                    onAddClick = {
                        cartViewModel.addToCart(product.id)
                    },
                    onFavoriteClick = {
                        productViewModel.toggleFavorite(product)
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp)) // Espacio para que el FloatingActionButton no tape contenido
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    AplicacionTallerTheme {
        HomeView(
            modifier = Modifier,
            onItemSelected = {}
        )
    }
}
