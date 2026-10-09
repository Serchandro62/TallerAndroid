package com.example.aplicaciontaller.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aplicaciontaller.R

@Composable
fun Title(text:String){
    Text(
        text=text,
        fontSize=28.sp,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.sp
    )
}

@Composable
fun CartButton(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            painter = painterResource(R.drawable.outline_shopping_cart_24),
            contentDescription = "Ver cotización"
        )
    }
}

@Composable
fun MainScaffold(
    selectedItem:Int,
    onItemSelected:(Int) -> Unit,
    topBar:@Composable () -> Unit ={},
    floatingActionButton: @Composable () -> Unit  = {},
    showBottomBar: Boolean=true,
    content: @Composable (PaddingValues) -> Unit
){
    Scaffold(
        topBar = topBar,
        bottomBar = {
            if (showBottomBar) {
                BottomMenu(
                    selectedItem = selectedItem,
                    onItemSelected = onItemSelected
                )
            }
        },
        floatingActionButton = floatingActionButton,
        content = { innerPadding ->
            content(innerPadding)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(){
    var query by remember { mutableStateOf("") }
    var activa by remember { mutableStateOf(false) }
    
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = { activa = false },
                expanded = activa,
                onExpandedChange = { activa = it },
                placeholder = {
                    Text("Buscar vehículos...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (activa) {
                        IconButton(
                            onClick = {
                                if (query.isNotEmpty()) query = "" else activa = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        expanded = activa,
        onExpandedChange = { activa = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        content = {
            Text(
                text = "Buscando: $query",
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp
            )
        }
    )
}