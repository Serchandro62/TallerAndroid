package com.example.aplicaciontaller.View

import android.app.Activity
import android.content.pm.PackageManager
import android.view.Surface
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicaciontaller.Components.MainScaffold
import com.example.aplicaciontaller.Components.Title
import com.example.aplicaciontaller.Data.database.AppDatabase
import com.example.aplicaciontaller.Data.repository.CartRepository
import com.example.aplicaciontaller.Data.repository.ProductRepository
import com.example.aplicaciontaller.ViewModel.CheckOutViewModel
import com.example.aplicaciontaller.ViewModel.CheckOutViewModelFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.util.jar.Manifest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutView(
    onItemSelected: (Int) -> Unit,
    onBackClick:() -> Unit = {}
){
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val cartRepository = CartRepository(database.cartItemDao())
    val productRepository = ProductRepository(database.productDao())
    val checkOutViewModel: CheckOutViewModel = viewModel(
        factory = CheckOutViewModelFactory(cartRepository, productRepository)
    )

    val uiState by checkOutViewModel.uiState.collectAsState()

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val defaultLocation = LatLng(19.432608, -99.133209)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    val fetchCurrentLocation = {
        val hasFine = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if(hasFine || hasCoarse){
            checkOutViewModel.updateLocationPermission(true)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if(location!=null){
                        val userLatLng = LatLng(location.latitude, location.longitude)
                        checkOutViewModel.updateUserLocation(userLatLng)
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(userLatLng, 16f)
                    } else {
                        Toast.makeText(context, "Asegúrate de tener el GPS activado.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: SecurityException){
                e.printStackTrace()
            }
        } else {
            checkOutViewModel.updateLocationPermission(false)
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        checkOutViewModel.updateLocationPermission(granted)
        if(granted){
            fetchCurrentLocation()
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if(hasFine || hasCoarse){
            checkOutViewModel.updateLocationPermission(true)
            fetchCurrentLocation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    MainScaffold(
        selectedItem = -1,
        onItemSelected = onItemSelected,
        showBottomBar = false,
        topBar = {
            CenterAlignedTopAppBar(
                title = {Title(text = "Agendar cita / Cotizar")},
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
    ) {
        padding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Encuentra tu concesionaria",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Card(
                modifier = Modifier.fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isMyLocationEnabled = uiState.hasLocationPermission
                        ),
                        uiSettings = MapUiSettings(
                            myLocationButtonEnabled = false,
                            zoomControlsEnabled = true
                        )
                    ) {
                        uiState.userLocation?.let{ latLng ->
                            Marker(
                                state = rememberMarkerState(position = latLng),
                                title = "Dirígete a tu concesionaria más cercana aquí: ",
                                snippet = "Ubicación actual seleccionada"
                            )
                        }
                    }
                    FloatingActionButton(
                        onClick = {
                            if(uiState.hasLocationPermission){
                                fetchCurrentLocation()
                            } else {
                                permissionLauncher.launch(
                                    arrayOf(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                            .padding(12.dp),
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ){
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Obtener ubicación actual"
                        )
                    }

                    if(!uiState.hasLocationPermission){
                        Surface(
                            modifier = Modifier.fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)
                        ){
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "Permiso de ubicación requerido para encontrar tu concesionaria más cercana",
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ){
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column{
                    Text(
                        text = "Dirección detectada",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = uiState.locationAddress,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}





























