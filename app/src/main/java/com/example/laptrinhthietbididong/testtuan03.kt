
package com.example.laptrinhthietbididong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class Baith03 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "A"
    ) {
        composable("A") {
            ScreenA {
                navController.navigate("B")
            }
        }

        composable("B") {
            ScreenB {
                navController.navigate("C")
            }
        }

        composable("C") {
            ScreenC {
                navController.popBackStack(
                    route = "A",
                    inclusive = false
                )
            }
        }
    }
}

@Composable
fun ScreenA(onPush: () -> Unit) {
    ScreenLayout(
        title = "Màn hình A",
        color = Color(0xFF2196F3),
        buttonText = "Push sang màn hình B",
        onClick = onPush
    )
}

@Composable
fun ScreenB(onPush: () -> Unit) {
    ScreenLayout(
        title = "Màn hình B",
        color = Color(0xFF4CAF50),
        buttonText = "Push sang màn hình C",
        onClick = onPush
    )
}

@Composable
fun ScreenC(onPop: () -> Unit) {
    ScreenLayout(
        title = "Màn hình C",
        color = Color(0xFFFF9800),
        buttonText = "Pop về màn hình A",
        onClick = onPop
    )
}

@Composable
fun ScreenLayout(
    title: String,
    color: Color,
    buttonText: String,
    onClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = color
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp
                )
            }
        }
    }
}
