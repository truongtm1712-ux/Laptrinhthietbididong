package com.example.laptrinhthietbididong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF315CF5)
private val PaleBlue = Color(0xFFEAF0FF)
private val Page = Color(0xFFF7F8FC)
private val Ink = Color(0xFF20243A)
private val Muted = Color(0xFF858BA0)
private val Green = Color(0xFF20B982)
private val Orange = Color(0xFFFFB547)

data class TodoTask(
    val id: Int,
    val title: String,
    val category: String,
    val time: String,
    val completed: Boolean = false
)

private val initialTasks = listOf(
    TodoTask(1, "Hoàn thành bài tập Android", "Học tập", "08:00"),
    TodoTask(2, "Đọc tài liệu Jetpack Compose", "Học tập", "09:30"),
    TodoTask(3, "Làm báo cáo dự án", "Công việc", "13:00"),
    TodoTask(4, "Ôn tập bài học hôm nay", "Cá nhân", "19:00")
)

class Bai1tuan4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UthTodoApp()
        }
    }
}

@Composable
fun UthTodoApp() {
    var screen by remember { mutableStateOf("welcome") }
    var previousScreen by remember { mutableStateOf("tasks") }
    var onboardingStep by remember { mutableIntStateOf(0) }
    var darkMode by remember { mutableStateOf(false) }
    var showAddTask by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskCategory by remember { mutableStateOf("Học tập") }
    var nextId by remember { mutableIntStateOf(5) }
    val tasks = remember {
        mutableStateListOf<TodoTask>().apply {
            addAll(initialTasks)
        }
    }

    val isOnboarding = screen == "welcome" || screen == "guide" ||
            screen == "reminder"

    fun goBack() {
        when (screen) {
            "guide" -> screen = "welcome"
            "reminder" -> screen = "guide"
            "tasks", "stats", "settings" -> {
                if (isOnboarding) screen = "welcome"
                else screen = "tasks"
            }
            "welcome" -> screen = "welcome"
            else -> screen = "tasks"
        }
    }

    BackHandler(enabled = screen != "welcome" || showAddTask) {
        if (showAddTask) {
            showAddTask = false
        } else {
            goBack()
        }
    }

    MaterialTheme(
        colorScheme = if (darkMode) {
            darkColorScheme(
                primary = Blue,
                background = Color(0xFF10121B),
                surface = Color(0xFF1C2030)
            )
        } else {
            lightColorScheme(
                primary = Blue,
                background = Page,
                surface = Color.White
            )
        }
    ) {
        val bg = if (darkMode) Color(0xFF10121B) else Page
        val surface = if (darkMode) Color(0xFF1C2030) else Color.White
        val textColor = if (darkMode) Color.White else Ink

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
        ) {
            when (screen) {
                "welcome" -> UthTodoWelcomeScreen(
                    onStart = {
                        onboardingStep = 0
                        screen = "guide"
                    }
                )

                "guide" -> GuideScreen(
                    step = onboardingStep,
                    onBack = { goBack() },
                    onSkip = { screen = "reminder" },
                    onNext = {
                        if (onboardingStep < 1) onboardingStep++
                        else screen = "reminder"
                    }
                )

                "reminder" -> ReminderScreen(
                    onBack = { goBack() },
                    onFinish = { screen = "tasks" }
                )

                "tasks" -> {
                    val visibleTasks = tasks.filter {
                        it.title.contains(query, ignoreCase = true) &&
                                when (selectedFilter) {
                                    "Đang làm" -> !it.completed
                                    "Hoàn thành" -> it.completed
                                    else -> true
                                }
                    }

                    TaskHomeScreen(
                        tasks = visibleTasks,
                        allTasks = tasks,
                        query = query,
                        selectedFilter = selectedFilter,
                        surface = surface,
                        textColor = textColor,
                        onQueryChange = { query = it },
                        onFilterChange = { selectedFilter = it },
                        onToggle = { task ->
                            val index = tasks.indexOfFirst { it.id == task.id }
                            if (index >= 0) {
                                tasks[index] = tasks[index].copy(
                                    completed = !tasks[index].completed
                                )
                            }
                        },
                        onDelete = { task -> tasks.remove(task) },
                        onAdd = { showAddTask = true },
                        onStats = { previousScreen = "tasks"; screen = "stats" },
                        onSettings = { previousScreen = "tasks"; screen = "settings" }
                    )
                }

                "stats" -> StatsScreen(
                    tasks = tasks,
                    surface = surface,
                    textColor = textColor,
                    onBack = { screen = "tasks" },
                    onSettings = { screen = "settings" },
                    onTasks = { screen = "tasks" }
                )

                "settings" -> SettingsScreen(
                    darkMode = darkMode,
                    onDarkModeChange = { darkMode = it },
                    surface = surface,
                    textColor = textColor,
                    onBack = { screen = previousScreen },
                    onTasks = { screen = "tasks" },
                    onStats = { screen = "stats" }
                )
            }

            if (showAddTask) {
                AddTaskDialog(
                    title = newTaskTitle,
                    category = newTaskCategory,
                    onTitleChange = { newTaskTitle = it },
                    onCategoryChange = { newTaskCategory = it },
                    onDismiss = {
                        showAddTask = false
                        newTaskTitle = ""
                    },
                    onSave = {
                        if (newTaskTitle.isNotBlank()) {
                            tasks.add(
                                0,
                                TodoTask(
                                    id = nextId++,
                                    title = newTaskTitle.trim(),
                                    category = newTaskCategory,
                                    time = "Hôm nay"
                                )
                            )
                            selectedFilter = "Tất cả"
                            query = ""
                            newTaskTitle = ""
                            showAddTask = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun UthTodoWelcomeScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
            .padding(horizontal = 30.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(30.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(138.dp)
                    .clip(RoundedCornerShape(34.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    LogoTaskRow(true)
                    LogoTaskRow(false)
                    LogoTaskRow(false)
                }
            }

            Spacer(Modifier.height(32.dp))
            Text(
                "UTH Todo",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "Tổ chức công việc, làm chủ thời gian",
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 15.sp
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Mỗi ngày một bước tiến nhỏ",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(22.dp))
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Blue
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Bắt đầu ngay", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text("→", fontSize = 20.sp)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "UTH Todo • Phiên bản sinh viên",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun LogoTaskRow(checked: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (checked) Blue else PaleBlue),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Text("✓", color = Color.White, fontSize = 13.sp)
            }
        }
        Box(
            Modifier
                .width(42.dp)
                .height(5.dp)
                .clip(CircleShape)
                .background(if (checked) Blue else Color(0xFFD9E1FF))
        )
    }
}

@Composable
private fun GuideScreen(
    step: Int,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    val titles = listOf(
        "Quản lý công việc dễ dàng",
        "Không bỏ lỡ việc quan trọng"
    )
    val descriptions = listOf(
        "Tạo danh sách việc cần làm, phân loại theo nhóm và theo dõi tiến độ mỗi ngày.",
        "Sắp xếp lịch trình hợp lý, đặt nhắc nhở và hoàn thành mục tiêu đúng hạn."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackArrow(onBack)
            TextButton(onClick = onSkip) {
                Text("Bỏ qua", color = Muted)
            }
        }

        Spacer(Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(28.dp))
                .background(PaleBlue),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Blue),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        LogoTaskRow(true)
                        LogoTaskRow(false)
                        LogoTaskRow(false)
                    }
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    "UTH Todo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
                Text(
                    "Lên kế hoạch • Hoàn thành mục tiêu",
                    fontSize = 12.sp,
                    color = Muted
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(3) { index ->
                Box(
                    Modifier
                        .width(if (index == step) 26.dp else 8.dp)
                        .height(7.dp)
                        .clip(CircleShape)
                        .background(if (index == step) Blue else PaleBlue)
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        Text(
            titles[step],
            fontSize = 25.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
        Spacer(Modifier.height(10.dp))
        Text(
            descriptions[step],
            fontSize = 15.sp,
            lineHeight = 24.sp,
            color = Muted
        )
        Spacer(Modifier.height(26.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                if (step < 1) "Tiếp tục" else "Thiết lập nhắc nhở",
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(8.dp))
            Text("→")
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ReminderScreen(
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    var reminderEnabled by remember { mutableStateOf(true) }
    var selectedTime by remember { mutableStateOf("08:00") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(22.dp)
    ) {
        BackArrow(onBack)
        Spacer(Modifier.height(26.dp))

        Box(
            Modifier
                .size(74.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(PaleBlue),
            contentAlignment = Alignment.Center
        ) {
            Text("🔔", fontSize = 34.sp)
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Nhắc nhở đúng lúc",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Chọn thời gian để UTH Todo giúp bạn nhớ những công việc quan trọng.",
            fontSize = 15.sp,
            lineHeight = 23.sp,
            color = Muted
        )

        Spacer(Modifier.height(28.dp))
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Page)
        ) {
            Column(Modifier.padding(17.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Nhắc nhở hằng ngày",
                            fontWeight = FontWeight.SemiBold,
                            color = Ink
                        )
                        Spacer(Modifier.height(4.dp))
                        Text("Nhắc bạn kiểm tra kế hoạch", color = Muted, fontSize = 12.sp)
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it }
                    )
                }

                Spacer(Modifier.height(16.dp))
                Text("Thời gian nhắc nhở", color = Muted, fontSize = 13.sp)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("07:00", "08:00", "20:00").forEach { time ->
                        FilterChip(
                            selected = selectedTime == time,
                            onClick = { selectedTime = time },
                            label = { Text(time) }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Hoàn tất thiết lập", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Text("→")
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Bạn có thể thay đổi cài đặt sau",
            modifier = Modifier.fillMaxWidth(),
            color = Muted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun TaskHomeScreen(
    tasks: List<TodoTask>,
    allTasks: SnapshotStateList<TodoTask>,
    query: String,
    selectedFilter: String,
    surface: Color,
    textColor: Color,
    onQueryChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onToggle: (TodoTask) -> Unit,
    onDelete: (TodoTask) -> Unit,
    onAdd: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit
) {
    val completed = allTasks.count { it.completed }
    val progress = if (allTasks.isEmpty()) 0f
    else completed.toFloat() / allTasks.size

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("▣", fontSize = 20.sp) },
                    label = { Text("Công việc") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onStats,
                    icon = { Text("▥", fontSize = 20.sp) },
                    label = { Text("Thống kê") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onSettings,
                    icon = { Text("⚙", fontSize = 20.sp) },
                    label = { Text("Cài đặt") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = Blue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text("+", fontSize = 30.sp)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp)
        ) {
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("UTH Todo", fontSize = 13.sp, color = Blue, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(5.dp))
                    Text(
                        "Danh sách công việc",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PaleBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 22.sp)
                }
            }

            Spacer(Modifier.height(18.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Blue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("Chào bạn! 👋", color = Color.White, fontSize = 15.sp)
                    Spacer(Modifier.height(5.dp))
                    Text(
                        "Sẵn sàng hoàn thành mục tiêu hôm nay?",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(18.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "$completed/${allTasks.size} công việc hoàn thành",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                            Spacer(Modifier.height(9.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(CircleShape),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.28f)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Text(
                            "${(progress * 100).toInt()}%",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Công việc của bạn",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )
                Text("${tasks.size} việc", color = Muted, fontSize = 12.sp)
            }

            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(surface)
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⌕", fontSize = 23.sp, color = Muted)
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = textColor,
                        fontSize = 14.sp
                    ),
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text("Tìm kiếm công việc...", color = Muted, fontSize = 14.sp)
                        }
                        inner()
                    }
                )
            }

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Tất cả", "Đang làm", "Hoàn thành").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { onFilterChange(filter) },
                        label = { Text(filter, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            if (tasks.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("☑", fontSize = 45.sp, color = Blue)
                        Spacer(Modifier.height(10.dp))
                        Text("Chưa có công việc phù hợp", color = textColor, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(5.dp))
                        Text("Thêm công việc mới bằng nút +", color = Muted, fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            surface = surface,
                            textColor = textColor,
                            onToggle = { onToggle(task) },
                            onDelete = { onDelete(task) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: TodoTask,
    surface: Color,
    textColor: Color,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Green)
            )
            Spacer(Modifier.width(6.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    task.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.completed) Muted else textColor,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(task.category, color = Blue, fontSize = 11.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("• ${task.time}", color = Muted, fontSize = 11.sp)
                }
            }
            Text(
                "×",
                color = Muted,
                fontSize = 23.sp,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onDelete() }
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun StatsScreen(
    tasks: List<TodoTask>,
    surface: Color,
    textColor: Color,
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onTasks: () -> Unit
) {
    val done = tasks.count { it.completed }
    val total = tasks.size
    val rate = if (total == 0) 0 else done * 100 / total

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = onTasks,
                    icon = { Text("▣") },
                    label = { Text("Công việc") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("▥") },
                    label = { Text("Thống kê") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onSettings,
                    icon = { Text("⚙") },
                    label = { Text("Cài đặt") }
                )
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp)
        ) {
            BackArrow(onBack)
            Spacer(Modifier.height(20.dp))
            Text("Thống kê", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = textColor)
            Spacer(Modifier.height(6.dp))
            Text("Theo dõi tiến độ của bạn", color = Muted, fontSize = 14.sp)
            Spacer(Modifier.height(22.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("Tổng công việc", "$total", Blue, surface, Modifier.weight(1f))
                StatCard("Đã hoàn thành", "$done", Green, surface, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            StatCard("Tiến độ hoàn thành", "$rate%", Orange, surface, Modifier.fillMaxWidth())

            Spacer(Modifier.height(24.dp))
            Text("Tổng quan tiến độ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
            Spacer(Modifier.height(15.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = surface)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Hoàn thành", color = textColor)
                        Text("$done / $total", color = Blue, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { if (total == 0) 0f else done.toFloat() / total },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        if (total == 0) "Hãy thêm công việc đầu tiên của bạn."
                        else if (done == total) "Tuyệt vời! Bạn đã hoàn thành tất cả công việc."
                        else "Cố gắng thêm một chút để hoàn thành mục tiêu nhé!",
                        color = Muted,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    color: Color,
    surface: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(
                Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text("▣", color = color, fontSize = 19.sp)
            }
            Spacer(Modifier.height(13.dp))
            Text(value, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, color = Muted)
        }
    }
}

@Composable
private fun SettingsScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    surface: Color,
    textColor: Color,
    onBack: () -> Unit,
    onTasks: () -> Unit,
    onStats: () -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = onTasks,
                    icon = { Text("▣") },
                    label = { Text("Công việc") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onStats,
                    icon = { Text("▥") },
                    label = { Text("Thống kê") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("⚙") },
                    label = { Text("Cài đặt") }
                )
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp)
        ) {
            BackArrow(onBack)
            Spacer(Modifier.height(20.dp))
            Text("Cài đặt", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = textColor)
            Spacer(Modifier.height(6.dp))
            Text("Tùy chỉnh trải nghiệm UTH Todo", color = Muted, fontSize = 14.sp)
            Spacer(Modifier.height(22.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = surface)
            ) {
                Column(Modifier.padding(17.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(PaleBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎨", fontSize = 23.sp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Giao diện tối", fontWeight = FontWeight.SemiBold, color = textColor)
                            Spacer(Modifier.height(4.dp))
                            Text("Thay đổi giao diện ứng dụng", color = Muted, fontSize = 12.sp)
                        }
                        Switch(
                            checked = darkMode,
                            onCheckedChange = onDarkModeChange
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Page
                    )

                    SettingRow("🔔", "Thông báo", "Thiết lập nhắc nhở", textColor)
                    HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Page)
                    SettingRow("👤", "Tài khoản", "Thông tin cá nhân", textColor)
                    HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Page)
                    SettingRow("ℹ", "Về ứng dụng", "UTH Todo phiên bản 1.0", textColor)
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                "UTH Todo",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Blue
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "Quản lý công việc • Học tập • Phát triển bản thân",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 11.sp,
                color = Muted
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: String,
    title: String,
    description: String,
    textColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 23.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = textColor, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(description, color = Muted, fontSize = 12.sp)
        }
        Text("›", color = Muted, fontSize = 25.sp)
    }
}

@Composable
private fun AddTaskDialog(
    title: String,
    category: String,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackArrow(onClick = onDismiss)
                Spacer(Modifier.width(10.dp))
                Text("Thêm công việc", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tên công việc") },
                    placeholder = { Text("Nhập công việc cần làm") },
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                Text("Danh mục", color = Muted, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Học tập", "Công việc", "Cá nhân").forEach { item ->
                        FilterChip(
                            selected = category == item,
                            onClick = { onCategoryChange(item) },
                            label = { Text(item, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = title.isNotBlank()
            ) {
                Text("Lưu công việc")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        shape = RoundedCornerShape(22.dp)
    )
}

@Composable
private fun BackArrow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("←", fontSize = 25.sp, color = Blue)
        Spacer(Modifier.width(3.dp))
        Text("Quay lại", fontSize = 13.sp, color = Blue)
    }
}
