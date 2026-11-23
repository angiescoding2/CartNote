package com.example.cartnote

// Allt skärmen behöver visa, samlat på ett ställe
data class ShoppingUiState(
    val items: List<ShoppingItem> = emptyList(),  // varorna som visas i listan
    val boughtCount: Int = 0,                      // hur många som är köpta
    val totalCount: Int = 0                        // hur många varor det finns
) {
    // Tomt läge visas när det inte finns några varor
    val isEmpty: Boolean get() = totalCount == 0
}