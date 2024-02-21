import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase




@Composable
fun SingUpScreen(navController: NavController) {
    val database = FirebaseDatabase.getInstance().reference
    var nameSurname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    val purposes = listOf("Choose", "Self-improvement", "Fitness", "Yoga", "Study", "Casual")
    var selectedPurpose by remember { mutableStateOf(purposes[0]) }
    val genders = listOf("Choose", "Man", "Woman", "I don't want to specify ","Private")
    var selectedGender by remember { mutableStateOf(genders[0]) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp
    ) {
        Column {
            TopAppBar(
                title = { Text("Welcome to GooDaym App", style = MaterialTheme.typography.h5) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )

            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = nameSurname,
                    onValueChange = { nameSurname = it },
                    label = { Text("Name Surname") },
                    singleLine = true
                )
                UserData.nameSurname = nameSurname
                Spacer(modifier = Modifier.height(16.dp))
                CustomExposedDropdownMenu(
                    selectedGender,
                    genders,
                    onValueChange = { selectedGender = it },
                    label = { Text("Gender") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomExposedDropdownMenu(
                    selectedPurpose,
                    purposes,
                    onValueChange = { selectedPurpose = it },
                    label = { Text("Purpose of Use") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        database.child("Users").child(nameSurname).child("Gender")
                            .setValue(selectedGender)
                        database.child("Users").child(nameSurname).child("Age").setValue(age)
                        database.child("Users").child(nameSurname).child("Purpose")
                            .setValue(selectedPurpose)
                        database.child("Users").child(nameSurname).child("Password")
                            .setValue(password)

                        navController.navigate("main")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nameSurname.isNotEmpty() && password.isNotEmpty()
                ) {
                    Text("Sign Up")
                }
                Button(
                    onClick = {
                        navController.navigate("Login")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(1.dp, Color.Red)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Warning: This is an important message!",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.graphicsLayer {
                                translationY = (-8).dp.toPx()
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "-The application is in the demo phase and uses generative artificial intelligence. For this reason, the application may not always give the correct result.\n" +
                                    "\n" +
                                    "-If you have any health problems or special conditions, it is important to consult a doctor instead of using the app.",
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomExposedDropdownMenu(selectedItem: String, items: List<String>, onValueChange: (String) -> Unit, label: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { },
            label = label,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { label ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onValueChange(label)
                }) {
                    Text(text = label)
                }
            }
        }
    }
}
