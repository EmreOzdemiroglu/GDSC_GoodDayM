import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

data class Task(
    val title: String,
    val body: String,
    val date: LocalDate,
    val linkedTodo: String,
    val aiGenerated: String
)


object UserData {
    var nameSurname by mutableStateOf("")
}

val onDeleteFromDatabase: (Task) -> Unit = { task ->
    val database = FirebaseDatabase.getInstance().reference
    val nameSurname = UserData.nameSurname
    val dateFormatter = DateTimeFormatter.ISO_DATE
    val dateString = task.date.format(dateFormatter)

    database.child("Users").child(nameSurname).child("ToDos")
        .orderByChild("date").equalTo(dateString)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    val title = child.child("title").getValue(String::class.java)
                    val body = child.child("body").getValue(String::class.java)
                    val linkedTodo = child.child("linked_todo").getValue(String::class.java)
                    val aiGenerated = child.child("AIGenerated").getValue(String::class.java)

                    if (title == task.title && body == task.body && linkedTodo == task.linkedTodo && aiGenerated == task.aiGenerated) {
                        child.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
}

@Composable
fun ToDoScreen() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTask by remember { mutableStateOf(Task("", "", LocalDate.now(), "", "")) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    val tasksMap = tasks.groupBy({ it.date }, { it })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                contentColor = MaterialTheme.colors.onPrimary
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Task") },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                onClick = { showDialog = true },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Calendar(selectedDate = selectedDate, tasks = tasksMap, onDateSelected = { date ->
                selectedDate = date
            })
            Spacer(modifier = Modifier.height(16.dp))
            TaskList(
                tasks = tasks.filter { it.date == selectedDate },
                onDelete = { task ->
                    tasks = tasks.filter { it != task }
                    onDeleteFromDatabase(task)
                },
                onTaskClick = { task ->
                    selectedTask = task
                }
            )
        }
    }

    if (showDialog) {
        val nameSurname = UserData.nameSurname
        val database = FirebaseDatabase.getInstance().reference
        NewTaskDialog(
            newTask = newTask,
            onNewTask = { task ->
                tasks = tasks + task
                val dateFormatter = DateTimeFormatter.ISO_DATE
                val dateString = selectedDate.format(dateFormatter)
                val linked = ""
                val generatedAI = "false"
                val taskMap = mapOf(
                    "title" to task.title,
                    "body" to task.body,
                    "date" to dateString,
                    "linked_todo" to linked,
                    "AIGenerated" to generatedAI
                )
                database.child("Users").child(nameSurname).child("ToDos").push().setValue(taskMap)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    selectedTask?.let { task ->
        AlertDialog(
            onDismissRequest = { selectedTask = null },
            title = { Text("Task Details") },
            text = {
                Column {
                    Text(text = "Title: ${task.title}", fontWeight = FontWeight.Bold)
                    Text(text = "Body: ${task.body}")
                    Text(text = "Date: ${task.date}")
                    Text(text = "Linked Todo: ${task.linkedTodo}")
                    Text(text = "AI Generated: ${task.aiGenerated}")
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedTask = null }
                ) {
                    Text("Close")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference
        val nameSurname = UserData.nameSurname
        database.child("Users").child(nameSurname).child("ToDos").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tasks = dataSnapshot.children.mapNotNull { snapshot ->
                    val title = snapshot.child("title").getValue(String::class.java)
                    val body = snapshot.child("body").getValue(String::class.java)
                    val dateString = snapshot.child("date").getValue(String::class.java)
                    val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
                    val linkedTodo = snapshot.child("linked_todo").getValue(String::class.java)
                    val aiGenerated = snapshot.child("AIGenerated").getValue(String::class.java)
                    Task(title ?: "", body ?: "", date, linkedTodo ?: "", aiGenerated ?: "")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onDelete: (Task) -> Unit,
    onTaskClick: (Task) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(tasks) { task ->
            TaskItem(task = task, onDelete = { onDelete(task) }, onTaskClick = { onTaskClick(task) })
        }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: () -> Unit, onTaskClick: () -> Unit) {
    var checked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onTaskClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color.White,
                    checkedColor = Color.Green
                )
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.body1.copy(
                        textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                    )
                )
                Text(
                    text = task.body,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = task.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                    style = MaterialTheme.typography.caption
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete task")
            }
        }
    }
}

@Composable
fun NewTaskDialog(newTask: Task, onNewTask: (Task) -> Unit, onDismiss: () -> Unit) {
    var currentTitle by remember { mutableStateOf(newTask.title) }
    var currentBody by remember { mutableStateOf(newTask.body) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = currentTitle,
                    onValueChange = { currentTitle = it },
                    label = { Text("Title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = currentBody,
                    onValueChange = { currentBody = it },
                    label = { Text("Body") },
                    singleLine = false
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onNewTask(Task(currentTitle, currentBody, LocalDate.now(), "", ""))
                    currentTitle = ""
                    currentBody = ""
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        }
    )
}

@Composable
fun Calendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    tasks: Map<LocalDate, List<Task>>
) {
    val currentMonth = YearMonth.of(selectedDate.year, selectedDate.month)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.ordinal

    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onDateSelected(selectedDate.minusMonths(1)) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                }
                Text(
                    text = "${currentMonth.month.name.lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }} ${currentMonth.year}",
                    style = MaterialTheme.typography.h6
                )
                IconButton(onClick = { onDateSelected(selectedDate.plusMonths(1)) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in DayOfWeek.values()) {
                    Text(
                        text = dayOfWeek.name.take(3).uppercase(),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }

            LazyColumn {
                items((1..6).toList()) { week ->
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        for (dayOfWeek in DayOfWeek.values()) {
                            val dayOfMonth = week * 7 + dayOfWeek.ordinal - firstDayOfWeek + 1
                            if (dayOfMonth in 1..daysInMonth) {
                                val date = LocalDate.of(currentMonth.year, currentMonth.month, dayOfMonth)
                                val isSelected = date == selectedDate
                                val hasTasks = tasks[date]?.isNotEmpty() == true
                                val buttonModifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .background(
                                        color = when {
                                            isSelected -> MaterialTheme.colors.primary.copy(alpha = 0.2f)
                                            hasTasks -> Color.LightGray
                                            else -> MaterialTheme.colors.surface
                                        }
                                    )
                                    .clickable { onDateSelected(date) }

                                Text(
                                    text = dayOfMonth.toString(),
                                    modifier = buttonModifier,
                                    color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body1
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
