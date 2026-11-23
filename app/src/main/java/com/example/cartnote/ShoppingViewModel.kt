package com.example.cartnote

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Håller listan och all logik. Räknar ut UiState som skärmen visar.
class ShoppingViewModel : ViewModel() {

    // Den kompletta listan. Bara ViewModel ser och ändrar den.
    private var allItems = listOf<ShoppingItem>()

    // Nästa lediga id
    private var nextId = 1

    // Skärmens tillstånd. _uiState ändras här inne, uiState kan bara läsas utifrån.
    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    // Räknar om allt skärmen visar, varje gång listan har ändrats
    private fun refresh() {
        _uiState.value = ShoppingUiState(
            items = allItems,
            boughtCount = allItems.count { it.isBought },
            totalCount = allItems.size
        )
    }

    // Ändrar bara varan med rätt id, och uppdaterar sedan skärmen
    private fun update(id: Int, change: (ShoppingItem) -> ShoppingItem) {
        allItems = allItems.map { if (it.id == id) change(it) else it }
        refresh()
    }

    // Lägger till en ny vara, men bara om namnet inte är tomt
    fun addItem(name: String, section: Section) {
        if (name.isBlank()) return
        allItems = allItems + ShoppingItem(id = nextId++, name = name.trim(), section = section)
        refresh()
    }

    // Växlar köpt/inte köpt
    fun toggleBought(id: Int) = update(id) { it.copy(isBought = !it.isBought) }

    // Ändrar namn och avdelning
    fun updateItem(id: Int, name: String, section: Section) {
        if (name.isBlank()) return
        update(id) { it.copy(name = name.trim(), section = section) }
    }

    // Tar bort varan med rätt id
    fun deleteItem(id: Int) {
        allItems = allItems.filter { it.id != id }
        refresh()
    }
}