import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "Sing Up") {
        composable("Sing Up") { SingUpScreen(navController) }
        composable("main") { MainScreen() }
        composable("Login") { LoginScreen(navController) }
    }
}

@Composable
fun MainScreen() {
    val items = listOf("To-Do", "Protocols", "Notes")
    var selectedItem by remember { mutableStateOf(items[0]) }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 16.dp
            ) {
                items.forEach { item ->
                    BottomNavigationItem(
                        label = { Text(item) },
                        icon = {
                            when (item) {
                                "To-Do" -> Icon(Icons.Filled.Home, contentDescription = null)
                                "Protocols" -> Icon(Icons.Filled.Info, contentDescription = null)
                                "Notes" -> Icon(Icons.Filled.Create, contentDescription = null)
                            }
                        },
                        selected = selectedItem == item,
                        onClick = { selectedItem = item },
                        alwaysShowLabel = false,
                        selectedContentColor = MaterialTheme.colors.primary,
                        unselectedContentColor = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Crossfade(targetState = selectedItem, label = "") { screen ->
                when (screen) {
                    "To-Do" -> ToDoScreen()
                    "Protocols" -> ProtocolsScreen()
                    "Notes" -> NotesScreen()
                }
            }
        }
    }
}







