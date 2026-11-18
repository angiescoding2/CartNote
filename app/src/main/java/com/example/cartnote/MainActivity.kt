package com.example.cartnote
//CHECKLISTA EXAM 1 - G
//[x] 1. Projekt + publikt repo med .gitignore (utan build)
//[x] 2. Skapa datamodell: ShoppingItem + Section
//[x] 3. ShoppingViewModel med varulistan
//[x] 4. Material 3-tema, TopAppBar och LazyColumn
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cartnote.ui.theme.CartNoteTheme

// Startpunkten för appen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartNoteTheme {
                CartNoteScreen()
            }
        }
    }
}

// Appens huvudskärm
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartNoteScreen(viewModel: ShoppingViewModel = viewModel()) {
    Scaffold(
        // Topplisten med appens namn
        topBar = {
            TopAppBar(
                title = { Text("CartNote") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        // Den rullande listan med varor
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.items, key = { it.id }) { item ->
                Text(item.name)
            }
        }
    }
}