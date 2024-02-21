import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProtocolsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informations") },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                contentColor = Color.White
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProtocolCategory("Health", "By increasing the consumption of fruits, vegetables and legumes at a balanced level during the day, the risk of diseases such as cardiovascular diseases and diabetes can be reduced.")
            ProtocolCategory("Addiction Quitting", "Quit the addiction or unwanted habit for at least 30 days. Be aware of the fact that withdrawal effects will be uncomfortable, especially the first 10 to 15 days. After a month all withdrawal effects will be gone or reduced drastically.")
            ProtocolCategory("Productivity and Work", "Work for 25 minutes and rest for 5 minutes. In your resting time stay away from the work to reset your mind.")
            ProtocolCategory("Sun Light", "In cases of excessive exposure to sunlight, protective clothing should be worn and high factor sunscreen should be applied to reduce the risk of skin diseases.")
            ProtocolCategory("Coffee and Caffeine Stimulation", "Do not consume more than five cups of coffee or an equivalent drink or food in caffeine. Record your daily caffeine intake and test if symptoms are gone or have eased. If symptoms continue consult a doctor.")
            ProtocolCategory("Digital Literacy", "Engage in online courses or workshops to learn about internet safety, privacy, digital tools, and platforms. Practice regular use of technology for various tasks.")
            ProtocolCategory("Personal Development", "Set clear personal and professional goals, seek out learning opportunities, practice self-reflection, and embrace challenges as opportunities for growth.")
            ProtocolCategory("Stress Management","Identify stressors and develop coping strategies such as mindfulness, exercise, time management, and seeking social support. Practice relaxation techniques like deep breathing, meditation, or yoga.")
        }
    }
}

@Composable
fun ProtocolCategory(categoryTitle: String, categoryInfo: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = if (expanded) 16.dp else 8.dp,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = categoryTitle,
                style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onPrimary),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            if (expanded) {
                Text(
                    text = categoryInfo,
                    style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}




