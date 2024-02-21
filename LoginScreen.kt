import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun LoginScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val database = FirebaseDatabase.getInstance().reference
    var nameSurname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


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
                    singleLine = true,

                    )
                UserData.nameSurname = nameSurname
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    coroutineScope.launch {

                        var checkPassword =
                            database.child("Users").child(nameSurname).child("Password").get().await()
                                .toString()

                        if (password in checkPassword) {
                            navController.navigate("main")
                        } else {
                            println(password)
                            println(checkPassword)
                        }
                    }

                },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nameSurname.isNotEmpty() && password.isNotEmpty()) {
                    Text("Log in")
                }
            }
        }
}


