package com.example.cartnote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

//Gör så att listan klarar rotation
class ShoppingViewModel : ViewModel() {

    //mutableStateOf gör att skärmen ritas om direkt vid ändring.
    // private set = bara ViewModel får ändra listan.
    var items by mutableStateOf(listOf<ShoppingItem>())
        private set

    private var nextId = 1

    // Lägger till en ny vara, men bara om namnet inte är tomt
    fun addItem(name: String, section: Section) {
        if (name.isBlank()) return
        items = items + ShoppingItem(id = nextId++, name = name.trim(), section = section)
    }

    // Växlar köpt/inte köpt, bara på raden med rätt id
    fun toggleBought(id: Int) {
        items = items.map { if (it.id == id) it.copy(isBought = !it.isBought) else it }
    }

    // Ändrar namn och avdelning på en vara
    fun updateItem(id: Int, name: String, section: Section) {
        if (name.isBlank()) return
        items = items.map { if (it.id == id) it.copy(name = name.trim(), section = section) else it }
    }

    // Tar bort varan med rätt id
    fun deleteItem(id: Int) {
        items = items.filter { it.id != id }
    }
}