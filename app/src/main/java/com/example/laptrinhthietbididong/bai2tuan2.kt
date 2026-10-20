package com.example.laptrinhthietbididong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Bai2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GradeScreen()
            }
        }
    }
}

data class GradeResult(
    val grade: String,
    val color: Color
)

fun classifyGrade(score: Double): GradeResult {
    return if (score < 0 || score > 10) {
        GradeResult("Không hợp lệ", Color(0xFF858B98))
    } else if (score >= 8.0) {
        GradeResult("Giỏi", Color(0xFF397A4A))
    } else if (score >= 6.5) {
        GradeResult("Khá", Color(0xFF315FA8))
    } else if (score >= 5.0) {
        GradeResult("Trung bình", Color(0xFF9B681E))
    } else {
        GradeResult("Yếu", Color(0xFFB74740))
    }
}

@Composable
fun GradeScreen() {
    val score = 8.5

    val testScores = listOf(9.0, 7.0, 5.5, 3.0, 11.0)
    val mainResult = classifyGrade(score)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F8))
    ) {
        // Thanh tiêu đề
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF304878))
                .padding(20.dp)
        ) {
            Text(
                text = "Xếp loại học lực",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Thẻ điểm chính
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        RoundedCornerShape(14.dp)
                    )
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Điểm trung bình",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = score.toString(),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202633)
                )

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            mainResult.color.copy(alpha = 0.12f),
                            RoundedCornerShape(9.dp)
                        )
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mainResult.grade,
                        color = mainResult.color,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = "KIỂM TRA THÊM",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            // Danh sách điểm kiểm tra
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 14.dp)
            ) {
                testScores.forEachIndexed { index, testScore ->
                    val result = classifyGrade(testScore)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Điểm ${testScore.toString()}",
                            color = Color(0xFF252A34),
                            fontSize = 14.sp
                        )

                        Box(
                            modifier = Modifier
                                .background(
                                    result.color.copy(alpha = 0.12f),
                                    RoundedCornerShape(5.dp)
                                )
                                .padding(
                                    horizontal = 9.dp,
                                    vertical = 5.dp
                                )
                        ) {
                            Text(
                                text = result.grade,
                                color = result.color,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (index < testScores.lastIndex) {
                        HorizontalDivider(
                            color = Color(0xFFF0F0F0)
                        )
                    }
                }
            }
        }
    }
}
