package com.example.aplicaciontaller.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.aplicaciontaller.Components.Title
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicaciontaller.Components.CardViewHome
import com.example.aplicaciontaller.Components.CartButton
import com.example.aplicaciontaller.Components.MainScaffold
import com.example.aplicaciontaller.Components.SearchBarComponent
import com.example.aplicaciontaller.Components.cardCategoryHome
import com.example.aplicaciontaller.Components.spaceH
import com.example.aplicaciontaller.Components.spaceV
import com.example.aplicaciontaller.Data.database.AppDatabase
import com.example.aplicaciontaller.Data.repository.CategoryRepository
import com.example.aplicaciontaller.R
import com.example.aplicaciontaller.ViewModel.HomeViewModel
import com.example.aplicaciontaller.ViewModel.HomeViewModelFactory
import com.example.aplicaciontaller.ui.theme.AplicacionTallerTheme
import com.example.aplicaciontaller.ui.theme.ultra_l_blue
import com.example.aplicaciontaller.ui.theme.ultra_l_green
import com.example.aplicaciontaller.ui.theme.ultra_l_red
import com.example.aplicaciontaller.ui.theme.ultra_l_yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit
){
    MainScaffold(
        selectedItem = 0,
        onItemSelected = onItemSelected,
        topBar = {
            Column{
                CenterAlignedTopAppBar(
                    title = {
                        Title(text = "Concesionaria")
                    }
                )
                //SearchBarComponent()
            }
        },
        floatingActionButton = {
            CartButton()
        }
    ) {
        padding -> ContentHomeView(Modifier.padding(padding))
    }
}

@Composable
fun ContentHomeView(
    modifier: Modifier = Modifier
){
    val context= LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val categoryDao = database.categoryDao()
    val categoryRepository = CategoryRepository(categoryDao)
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(categoryRepository)
    )
    val categories by homeViewModel.categories.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardViewHome(
            "Encuentra tu auto ideal",
            "Hasta 24 meses sin intereses en seleccionados",
            stringResource(R.string.texto_boton),
            R.drawable.auto_banner
        )
        spaceV(16)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Categorías de vehículos",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ver todas",
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(categories){ category ->
                cardCategoryHome(
                    categoria = category.name,
                    imagen = category.image,
                    color = category.color
                )
                spaceH(6)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview(){
    AplicacionTallerTheme{
        HomeView(
            modifier = Modifier,
            onItemSelected = {}
        )
    }
}