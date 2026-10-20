package com.example.laptrinhthietbididong

import android.os.Bundle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// 1. Khai báo class Student với email và phone có thể null
data class Student(
    val name: String,
    val email: String?,
    val phone: String?
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                StudentListScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen() {

    // 2. Tạo danh sách 5 sinh viên
    val students = listOf(
        Student("Nguyễn Văn An", "an@sv.edu.vn", "0901 234 567"),
        Student("Trần Thị Bình", null, "0912 888 999"),
        Student("Lê Minh Chi", "chi@sv.edu.vn", null),
        Student("Phạm Quốc Dũng", null, null),
        Student("Võ Thanh Em", "em@sv.edu.vn", "0933 111 222")
    )

    // 5. Dùng vòng lặp for đếm sinh viên thiếu email
    var missingEmailCount = 0

    for (student in students) {
        if (student.email == null) {
            missingEmailCount++
        }
    }

    val blue = Color(0xFF23477D)
    val background = Color(0xFFF4F5F9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Danh sách sinh viên",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = blue
                )
            )
        },
        containerColor = background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {

            // 3. Dùng for tạo các thẻ sinh viên
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(students) { student ->
                    StudentCard(student = student)
                }
            }

            // Thống kê cuối danh sách
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(
                        color = Color(0xFFE1EAF8),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng: ${students.size} sinh viên",
                    color = blue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    Text(
                        text = "Thiếu email: ",
                        color = blue,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "$missingEmailCount",
                        color = Color.Red,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StudentCard(student: Student) {

    // 4. Toán tử ?: thay thế giá trị null
    val email = student.email ?: "Chưa cập nhật"
    val phone = student.phone ?: "Chưa cập nhật"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = student.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF252A34)
            )

            Spacer(modifier = Modifier.height(5.dp))

            InfoRow(
                label = "Email",
                value = email,
                isMissing = student.email == null
            )

            Spacer(modifier = Modifier.height(3.dp))

            InfoRow(
                label = "SĐT",
                value = phone,
                isMissing = student.phone == null
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    isMissing: Boolean
) {
    Row {
        Text(
            text = "$label: ",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Text(
            text = value,
            fontSize = 13.sp,
            color = if (isMissing) {
                Color(0xFFB45C50)
            } else {
                Color.DarkGray
            },
            fontStyle = if (isMissing) {
                FontStyle.Italic
            } else {
                FontStyle.Normal
            }
        )
    }
}
