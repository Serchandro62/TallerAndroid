package com.example.aplicaciontaller.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicaciontaller.Components.spaceV
import com.example.aplicaciontaller.R
import com.example.aplicaciontaller.ViewModel.AuthViewModel
import com.example.aplicaciontaller.ui.theme.AplicacionTallerTheme
import com.example.aplicaciontaller.ui.theme.Green1
import com.example.aplicaciontaller.ui.theme.ultra_l_blue

@Composable
fun RegisterView(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val authVM: AuthViewModel = viewModel()

    RegisterViewContent(
        email = authVM.email,
        password = authVM.password,
        errorMessage = authVM.errorMessage,
        isLoading = authVM.isLoading,
        onEmailChange = { authVM.email = it },
        onPasswordChange = { authVM.password = it },
        onRegister = {
            authVM.register(onRegisterSuccess)
        },
        onClearError = {
            authVM.clearError()
        },
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterViewContent(
    email: String,
    password: String,
    errorMessage: String?,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onClearError: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Emblem/Icono de Registro
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(ultra_l_blue),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.deportivo),
                contentDescription = "Registro Concesionaria",
                modifier = Modifier.size(54.dp)
            )
        }

        spaceV(16)

        Text(
            text = "REGISTRO DE CLIENTE",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Green1,
            letterSpacing = 1.sp
        )

        Text(
            text = "Crea tu cuenta para explorar el catálogo exclusivo",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        spaceV(24)

        // Tarjeta con formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Correo Electrónico") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = Green1
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                spaceV(16)

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Lock Icon",
                            tint = Green1
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                errorMessage?.let { error ->
                    spaceV(8)
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                spaceV(24)

                Button(
                    onClick = onRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "REGISTRARME Y ESTRENAR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        spaceV(16)

        TextButton(
            onClick = {
                onClearError()
                onNavigateToLogin()
            }
        ) {
            Text(
                text = "¿Ya tienes cuenta? Inicia sesión aquí",
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    AplicacionTallerTheme {
        RegisterViewContent(
            email = "cliente@concesionaria.com",
            password = "123456",
            errorMessage = null,
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onRegister = {},
            onClearError = {},
            onNavigateToLogin = {}
        )
    }
}
