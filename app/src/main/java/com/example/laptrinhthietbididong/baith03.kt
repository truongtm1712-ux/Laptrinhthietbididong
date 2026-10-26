package com.example.laptrinhthietbididong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Baithuchanh03 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {

    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedOperator by remember { mutableStateOf("+") }
    var errorMessage by remember { mutableStateOf("") }

    fun calculate(operator: String) {
        selectedOperator = operator
        errorMessage = ""

        val a = number1.trim().toDoubleOrNull()
        val b = number2.trim().toDoubleOrNull()

        if (a == null || b == null) {
            result = ""
            errorMessage = "Vui lòng nhập đầy đủ hai số!"
            return
        }

        if (operator == "/" && b == 0.0) {
            result = ""
            errorMessage = "Không thể chia cho 0!"
            return
        }

        val answer = when (operator) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> a / b
            else -> 0.0
        }

        result = if (
            answer.isFinite() &&
            answer >= Long.MIN_VALUE.toDouble() &&
            answer <= Long.MAX_VALUE.toDouble() &&
            answer % 1.0 == 0.0
        ) {
            answer.toLong().toString()
        } else {
            answer.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(110.dp))

        Text(
            text = "Thực hành 03",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Ô nhập số thứ nhất
        OutlinedTextField(
            value = number1,
            onValueChange = {
                number1 = it
                result = ""
                errorMessage = ""
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = null,
            placeholder = { Text("Nhập số thứ nhất") },
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Bốn nút phép tính
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CalculatorButton(
                symbol = "+",
                color = Color(0xFFD94B45),
                selected = selectedOperator == "+",
                modifier = Modifier.weight(1f)
            ) {
                calculate("+")
            }

            CalculatorButton(
                symbol = "-",
                color = Color(0xFFE2B54E),
                selected = selectedOperator == "-",
                modifier = Modifier.weight(1f)
            ) {
                calculate("-")
            }

            CalculatorButton(
                symbol = "*",
                color = Color(0xFF603CE0),
                selected = selectedOperator == "*",
                modifier = Modifier.weight(1f)
            ) {
                calculate("*")
            }

            CalculatorButton(
                symbol = "/",
                color = Color(0xFF202020),
                selected = selectedOperator == "/",
                modifier = Modifier.weight(1f)
            ) {
                calculate("/")
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Ô nhập số thứ hai
        OutlinedTextField(
            value = number2,
            onValueChange = {
                number2 = it
                result = ""
                errorMessage = ""
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = null,
            placeholder = { Text("Nhập số thứ hai") },
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Hiển thị kết quả
        Text(
            text = if (result.isNotEmpty()) {
                "Kết quả: $result"
            } else {
                "Kết quả:"
            },
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            color = Color.Black
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = errorMessage,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                color = Color.Red
            )
        }
    }
}

@Composable
fun CalculatorButton(
    symbol: String,
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(7.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        border = if (selected) {
            BorderStroke(2.dp, Color.Black)
        } else {
            null
        },
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Text(
            text = symbol,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
