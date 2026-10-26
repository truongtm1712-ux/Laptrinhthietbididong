package com.example.laptrinhthietbididong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF1D4ED8)
private val LightBlue = Color(0xFFEFF6FF)
private val BorderBlue = Color(0xFFDBEAFE)

class Bai03 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Bai03App()
                }
            }
        }
    }
}

@Composable
fun Bai03App() {
    var showWelcome by remember { mutableStateOf(true) }
    var selectedScreen by remember { mutableStateOf("") }

    BackHandler(
        enabled = selectedScreen.isNotEmpty() || !showWelcome
    ) {
        when {
            selectedScreen.isNotEmpty() -> selectedScreen = ""
            !showWelcome -> showWelcome = true
        }
    }

    when {
        showWelcome -> {
            WelcomeScreen {
                showWelcome = false
            }
        }

        selectedScreen.isEmpty() -> {
            ComponentListScreen {
                selectedScreen = it
            }
        }

        else -> {
            ComponentDetailScreen(
                selectedScreen = selectedScreen,
                onBack = { selectedScreen = "" }
            )
        }
    }
}

@Composable
fun WelcomeScreen(onReady: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(LightBlue),
            contentAlignment = Alignment.Center
        ) {
            Text("▦", fontSize = 64.sp, color = Blue)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Jetpack Compose",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Jetpack Compose is a modern UI toolkit for building native applications using a declarative programming approach.",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onReady,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        ) {
            Text(
                "I'm ready",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ComponentListScreen(onSelect: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBarSimple("UI Components List")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                SectionTitle("Display")

                ComponentCard("Text", "Displays text") {
                    onSelect("Text")
                }

                ComponentCard("Image", "Displays an image") {
                    onSelect("Image")
                }

                SectionTitle("Input")

                ComponentCard("TextField", "Input field for text") {
                    onSelect("TextField")
                }

                ComponentCard("PasswordField", "Input field for passwords") {
                    onSelect("PasswordField")
                }

                SectionTitle("Layout")

                ComponentCard("Column", "Arranges elements vertically") {
                    onSelect("Column")
                }

                ComponentCard("Row", "Arranges elements horizontally") {
                    onSelect("Row")
                }

                SectionTitle("Practice")

                ComponentCard(
                    "Calculator",
                    "Máy tính cộng, trừ, nhân, chia"
                ) {
                    onSelect("Calculator")
                }
            }
        }
    }
}

@Composable
fun ComponentDetailScreen(
    selectedScreen: String,
    onBack: () -> Unit
) {
    val title = when (selectedScreen) {
        "Text" -> "Text Detail"
        "Image" -> "Images"
        "TextField" -> "TextField"
        "PasswordField" -> "PasswordField"
        "Column" -> "Column Layout"
        "Row" -> "Row Layout"
        "Calculator" -> "Thực hành 03"
        else -> "Chi tiết"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("‹", fontSize = 32.sp, color = Blue)
                }

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Blue
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedScreen) {
                "Text" -> TextDetailScreen()
                "Image" -> ImageDetailScreen()
                "TextField" -> TextFieldDetailScreen()
                "PasswordField" -> TextFieldDetailScreen(true)
                "Column" -> ColumnLayoutScreen()
                "Row" -> RowLayoutScreen()
                "Calculator" -> CalculatorScreenTuan03()
            }
        }
    }
}

@Composable
fun TopAppBarSimple(title: String) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
    )
}

@Composable
fun ComponentCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(LightBlue)
            .border(1.dp, BorderBlue, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun TextDetailScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildAnnotatedString {
                append("The ")
                withStyle(
                    SpanStyle(textDecoration = TextDecoration.LineThrough)
                ) {
                    append("quick ")
                }
                withStyle(
                    SpanStyle(
                        color = Color(0xFF795548),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                ) {
                    append("Brown ")
                }
                append("fox j u m p s ")
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                ) {
                    append("over ")
                }
                append("the ")
                withStyle(
                    SpanStyle(textDecoration = TextDecoration.Underline)
                ) {
                    append("lazy ")
                }
                append("dog.")
            },
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageDetailScreen() {
    val context = LocalContext.current

    val image1 = remember {
        context.resources.getIdentifier(
            "image1", "drawable", context.packageName
        )
    }

    val image2 = remember {
        context.resources.getIdentifier(
            "image2", "drawable", context.packageName
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ImageCard("Image 1", image1)
        Spacer(modifier = Modifier.height(20.dp))
        ImageCard("Image 2 - In app", image2)
    }
}

@Composable
fun ImageCard(label: String, imageId: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageId != 0) {
            Image(
                painter = painterResource(imageId),
                contentDescription = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa tìm thấy ảnh: $label",
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, color = Blue)
    }
}

@Composable
fun TextFieldDetailScreen(passwordMode: Boolean = false) {
    var enteredText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = enteredText,
            onValueChange = { enteredText = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text(
                    if (passwordMode) "Nhập mật khẩu"
                    else "Thông tin nhập"
                )
            },
            placeholder = { Text("Thông tin nhập") },
            shape = RoundedCornerShape(30.dp),
            visualTransformation = if (passwordMode) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = when {
                enteredText.isEmpty() ->
                    "Tự động cập nhật dữ liệu theo textfield"
                passwordMode -> "Đã nhập mật khẩu"
                else -> "Nội dung: $enteredText"
            },
            color = if (enteredText.isEmpty()) Color.Red
            else Color.DarkGray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ColumnLayoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(6) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (index % 3 == 1) Blue
                        else Color(0xFF93C5FD)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Item ${index + 1}", color = Color.White)
            }
        }
    }
}

@Composable
fun RowLayoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(4) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(3) { column ->
                    val index = row * 3 + column

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(65.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (index % 3 == 1) Blue
                                else Color(0xFF93C5FD)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${index + 1}", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorScreenTuan03() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập số thứ nhất") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OperationButton("+", Color.Red) {
                result = calculate(num1, num2, "+")
            }

            OperationButton("-", Color(0xFFFFC107)) {
                result = calculate(num1, num2, "-")
            }

            OperationButton("*", Color(0xFF673AB7)) {
                result = calculate(num1, num2, "*")
            }

            OperationButton("/", Color.DarkGray) {
                result = calculate(num1, num2, "/")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập số thứ hai") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Kết quả: $result",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OperationButton(
    operator: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(60.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Text(
            text = operator,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

fun calculate(
    first: String,
    second: String,
    operator: String
): String {
    val a = first.trim().replace(',', '.').toDoubleOrNull()
    val b = second.trim().replace(',', '.').toDoubleOrNull()

    if (a == null || b == null) {
        return "Vui lòng nhập số hợp lệ!"
    }

    val answer = when (operator) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> {
            if (b == 0.0) return "Không thể chia cho 0"
            a / b
        }
        else -> return "Phép tính không hợp lệ"
    }

    return if (answer.isFinite()) {
        answer.toString()
    } else {
        "Kết quả không hợp lệ"
    }
}
