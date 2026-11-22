package com.example.cartnote
//CHECKLISTA EXAM 1 - G
//[x] 1. Projekt + publikt repo med .gitignore (utan build)
//[x] 2. Skapa datamodell: ShoppingItem + Section
//[x] 3. ShoppingViewModel med varulistan
//[x] 4. Material 3-tema, TopAppBar och LazyColumn
//[x] 5. Lägg till vara: fält "Vara" + rullgardin "Avdelning", tom text sparas inte
//[x] 6. Bocka av och ta bort (bara vald rad)
//[x] 7. Redigera vara i dialog
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
// Pennikonen på redigera-knappen
import androidx.compose.material.icons.filled.Edit
// Kundvagnsikonen i det tomma läget
import androidx.compose.material.icons.filled.ShoppingCart
// För att centrera texten i det tomma läget
import androidx.compose.ui.text.style.TextAlign

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

// det vi ser på skärmen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartNoteScreen(viewModel: ShoppingViewModel = viewModel()) {
    // Vilken vara som redigeras just nu (null = ingen dialog visas)
    var editingItem by remember { mutableStateOf<ShoppingItem?>(null) }

    // Visa dialogen när en vara är vald för redigering
    editingItem?.let { item ->
        EditItemDialog(
            item = item,
            onDismiss = { editingItem = null },
            onSave = { name, section ->
                viewModel.updateItem(item.id, name, section)
                editingItem = null
            }
        )
    }
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

            // Statistikrad: visas alltid, även "0 av 0 köpta"
            StatsRow(
                bought = viewModel.items.count { it.isBought },
                total = viewModel.items.size
            )

            Spacer(Modifier.height(12.dp))

            // Tomt läge: bara när det inte finns några varor
            if (viewModel.items.isEmpty()) {
                EmptyState()
            }

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
                        onEdit = { editingItem = item },
                        onDelete = { viewModel.deleteItem(item.id) }
                    )
                }
            }
        }
    }
}

// Inmatning: fältet "Vara", rullgardinen "Avdelning" och knappen "Lägg till"
@Composable
fun AddItemSection(onAdd: (String, Section) -> Unit) {
    // Det användaren skriver och väljer
    var name by remember { mutableStateOf("") }
    var section by remember { mutableStateOf(Section.FRUIT_VEG) }
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

            // Rullgardinen "Avdelning" (återanvänds i dialogen)
            SectionDropdown(
                selected = section,
                onSelect = { section = it },
                modifier = Modifier.weight(1f)
            )
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
    onEdit: () -> Unit,
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
            // Redigera raden
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Redigera ${item.name}")
            }
            // Ta bort raden
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Ta bort ${item.name}")
            }
        }
    }
}
// Rullgardin för avdelning. Används både i "Lägg till" och i redigeringsdialogen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDropdown(
    selected: Section,
    onSelect: (Section) -> Unit,
    modifier: Modifier = Modifier
) {
    // Om menyn är öppen eller stängd
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.label,
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
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Dialogen "Redigera vara": samma fält och rullgardin som när man lägger till
@Composable
fun EditItemDialog(
    item: ShoppingItem,
    onDismiss: () -> Unit,
    onSave: (String, Section) -> Unit
) {
    // Startar med varans nuvarande namn och avdelning
    var name by remember { mutableStateOf(item.name) }
    var section by remember { mutableStateOf(item.section) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Redigera vara") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Vara") },
                    singleLine = true,
                    isError = name.isBlank(),
                    supportingText = if (name.isBlank()) {
                        { Text("Namnet kan inte vara tomt") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
                // Samma rullgardin som i "Lägg till", återanvänd
                SectionDropdown(
                    selected = section,
                    onSelect = { section = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        // Spara går bara att trycka på när namnet inte är tomt
        confirmButton = {
            TextButton(
                onClick = { onSave(name, section) },
                enabled = name.isNotBlank()
            ) { Text("Spara") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Avbryt") }
        }
    )
}

// Statistikrad: hur många varor som är köpta, med en förloppsindikator
@Composable
fun StatsRow(bought: Int, total: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "$bought av $total köpta",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(8.dp))
            // Andel köpta från 0 till 1. Om listan är tom blir det 0, så vi slipper dela med noll.
            LinearProgressIndicator(
                progress = { if (total == 0) 0f else bought.toFloat() / total },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Visas när inköpslistan är tom
@Composable
fun EmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Icon(
            Icons.Default.ShoppingCart,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Din inköpslista är tom – lägg till en vara ovan",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}