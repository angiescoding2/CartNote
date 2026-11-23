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

    // Det valda filtret
    private var filter = Filter.ALL

    // Skärmens tillstånd. _uiState ändras här inne, uiState kan bara läsas utifrån.
    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    // Räknar om allt skärmen visar, varje gång listan eller filtret ändras
    private fun refresh() {
        // Bara de varor som passar filtret visas
        val visible = when (filter) {
            Filter.ALL -> allItems
            Filter.REMAINING -> allItems.filter { !it.isBought }
            Filter.BOUGHT -> allItems.filter { it.isBought }
        }
        _uiState.value = ShoppingUiState(
            items = visible,
            boughtCount = allItems.count { it.isBought },
            totalCount = allItems.size,
            filter = filter
        )
    }

    // Ändrar bara varan med rätt id, och uppdaterar sedan skärmen
    private fun update(id: Int, change: (ShoppingItem) -> ShoppingItem) {
        allItems = allItems.map { if (it.id == id) change(it) else it }
        refresh()
    }

    // Byter filter
    fun setFilter(newFilter: Filter) {
        filter = newFilter
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

    // Den senast borttagna varan och var i listan den låg, så att den kan ångras
    private var lastDeleted: Pair<Int, ShoppingItem>? = null

    // Tar bort varan med rätt id, men kommer ihåg den
    fun deleteItem(id: Int) {
        val index = allItems.indexOfFirst { it.id == id }
        if (index == -1) return
        lastDeleted = index to allItems[index]
        allItems = allItems.filter { it.id != id }
        refresh()
    }

    // Lägger tillbaka den senast borttagna varan på samma plats
    fun undoDelete() {
        val (index, item) = lastDeleted ?: return
        allItems = allItems.toMutableList().apply { add(index.coerceAtMost(size), item) }
        lastDeleted = null
        refresh()
    }
}