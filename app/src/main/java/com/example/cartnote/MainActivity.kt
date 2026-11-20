package com.example.cartnote
//CHECKLISTA EXAM 1 - G
//[x] 1. Projekt + publikt repo med .gitignore (utan build)
//[x] 2. Skapa datamodell: ShoppingItem + Section
//[x] 3. ShoppingViewModel med varulistan
//[x] 4. Material 3-tema, TopAppBar och LazyColumn
//[x] 5. Lägg till vara: fält "Vara" + rullgardin "Avdelning", tom text sparas inte
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
// Ger oss remember, mutableStateOf m.m. för fältens innehåll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cartnote.ui.theme.CartNoteTheme
// För ikonen på ta bort-knappen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
// För att centrera i raden och stryka över köpta varor
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Fält och knapp för att lägga till en vara
            AddItemSection(onAdd = { name, section -> viewModel.addItem(name, section) })

            Spacer(Modifier.height(16.dp))

            // Den rullande listan med varor (blir snyggare i steg 6)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.items, key = { it.id }) { item ->
                    // Varje vara visas som en egen rad. id:t ser till att bara den raden påverkas.
                    ShoppingRow(
                        item = item,
                        onToggle = { viewModel.toggleBought(item.id) },
                        onDelete = { viewModel.deleteItem(item.id) }
                    )
                }
            }
        }
    }
}

// Inmatning: fältet "Vara", rullgardinen "Avdelning" och knappen "Lägg till"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemSection(onAdd: (String, Section) -> Unit) {
    // Det användaren skriver och väljer
    var name by remember { mutableStateOf("") }
    var section by remember { mutableStateOf(Section.FRUIT_VEG) }
    var expanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        // Två fält bredvid varandra
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            // Fältet "Vara"
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; showError = false },
                label = { Text("Vara") },
                placeholder = { Text("T.ex. mjölk") },
                singleLine = true,
                isError = showError,
                supportingText = if (showError) {
                    { Text("Skriv in en vara först") }
                } else null,
                modifier = Modifier.weight(1f)
            )

            // Rullgardinen "Avdelning"
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = section.label,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text("Avdelning") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // En rad i menyn för varje avdelning
                    Section.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = { section = option; expanded = false }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Knappen "Lägg till"
        Button(
            onClick = {
                if (name.isBlank()) {
                    showError = true   // Tom text sparas inte
                } else {
                    onAdd(name, section)
                    name = ""          // Töm fältet efteråt
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lägg till")
        }
    }
}

// En rad i listan: checkbox, namn + avdelning och ta bort-knapp
@Composable
fun ShoppingRow(
    item: ShoppingItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            // Bocka av eller ångra
            Checkbox(
                checked = item.isBought,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary
                )
            )

            // Namn och avdelning
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    // Stryk över varan när den är köpt
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else null
                )
                Text(
                    text = item.section.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Ta bort raden
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Ta bort ${item.name}")
            }
        }
    }
}