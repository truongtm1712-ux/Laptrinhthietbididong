package com.example.laptrinhthietbididong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

// Model dữ liệu sinh viên
data class ScoreStudent(
    val name: String,
    val score: Double?
)

// Kiểm tra điểm hợp lệ
fun isValidScore(score: Double?): Boolean {
    return score != null &&
            score.isFinite() &&
            score in 0.0..10.0
}

// Xếp loại sinh viên
fun getClassification(score: Double?): String {
    if (score == null) return "Chưa có điểm"
    if (!isValidScore(score)) return "Không hợp lệ"

    return when {
        score >= 8.0 -> "Giỏi"
        score >= 6.5 -> "Khá"
        score >= 5.0 -> "Trung bình"
        else -> "Yếu"
    }
}

// Màu sắc theo xếp loại
fun getClassificationColor(score: Double?): Color {
    if (score == null) return Color.Gray
    if (!isValidScore(score)) return Color(0xFF858B98)

    return when {
        score >= 8.0 -> Color(0xFF2E7D32)
        score >= 6.5 -> Color(0xFF1976D2)
        score >= 5.0 -> Color(0xFFE65100)
        else -> Color(0xFFC62828)
    }
}

// Activity của màn hình bảng điểm
class StudentScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ScoreBoardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreBoardScreen() {
    val studentList = listOf(
        ScoreStudent("Nguyễn Văn An", 9.0),
        ScoreStudent("Trần Thị Bình", 7.2),
        ScoreStudent("Lê Minh Chi", null),
        ScoreStudent("Phạm Quốc Dũng", 4.5),
        ScoreStudent("Võ Thanh Em", 5.8)
    )

    var totalScore = 0.0
    var countWithScore = 0
    var gioiCount = 0
    var nullCount = 0
    var invalidCount = 0
    var maxScore = 0.0

    // Thống kê điểm
    for (student in studentList) {
        val score = student.score

        when {
            score == null -> {
                nullCount++
            }

            !isValidScore(score) -> {
                invalidCount++
            }

            else -> {
                totalScore += score
                countWithScore++

                if (score >= 8.0) {
                    gioiCount++
                }

                if (countWithScore == 1 || score > maxScore) {
                    maxScore = score
                }
            }
        }
    }

    val averageScore =
        if (countWithScore > 0) totalScore / countWithScore
        else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bảng điểm lớp",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A6E)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF4F5F7))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Thẻ thống kê tổng quan
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E3A6E)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Điểm trung bình lớp",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )

                    Text(
                        text = String.format(
                            Locale.US,
                            "%.2f",
                            averageScore
                        ),
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(
                            title = "Giỏi",
                            value = gioiCount.toString()
                        )

                        StatItem(
                            title = "Chưa có điểm",
                            value = nullCount.toString()
                        )

                        StatItem(
                            title = "Cao nhất",
                            value = if (countWithScore > 0) {
                                String.format(
                                    Locale.US,
                                    "%.1f",
                                    maxScore
                                )
                            } else {
                                "N/A"
                            }
                        )
                    }

                    if (invalidCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Điểm không hợp lệ: $invalidCount",
                            color = Color(0xFFFFCCBC),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "DANH SÁCH • ${studentList.size} SINH VIÊN",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Hiển thị danh sách sinh viên
            for (student in studentList) {
                StuCard(student = student)

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Thành phần thống kê
@Composable
fun StatItem(title: String, value: String) {
    Column {
        Text(
            text = title,
            color = Color.LightGray,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Thẻ thông tin sinh viên
@Composable
fun StuCard(student: ScoreStudent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = getClassification(student.score),
                    color = getClassificationColor(student.score),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = student.score?.let {
                    if (it.isFinite()) {
                        String.format(Locale.US, "%.1f", it)
                    } else {
                        "—"
                    }
                } ?: "—",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}