package com.example.cartnote
//CHECKLISTA EXAM 1 - G
//[x] 1. Projekt + publikt repo med .gitignore (utan build)
//[x] 2. Skapa datamodell: ShoppingItem + Section
//[] 3. ShoppingViewModel med varulistan
//[] 4. Material 3-tema, TopAppBar och LazyColumn
//[] 5. Lägg till vara: fält "Vara" + rullgardin "Avdelning", tom text sparas inte
//[] 6. Bocka av och ta bort (bara vald rad)
//[] 7. Redigera vara i dialog
//[] 8. Statistikrad "x av y köpta" + text för tom lista
//[] 9. README: felsökning + reflektion kring hjälpmedel

//---VG---
//[] 10. UiState + egna composables, filter och ångra borttagning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cartnote.ui.theme.CartNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartNoteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CartNoteTheme {
        Greeting("Android")
    }
}