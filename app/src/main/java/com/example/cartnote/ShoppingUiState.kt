package com.example.cartnote

// Vilka varor som visas i listan
enum class Filter(val label: String) {
    ALL("Alla"),
    REMAINING("Kvar"),
    BOUGHT("Köpta")
}

// Allt skärmen behöver visa, samlat på ett ställe
data class ShoppingUiState(
    val items: List<ShoppingItem> = emptyList(),  // varorna som visas, efter filtret
    val boughtCount: Int = 0,                      // hur många som är köpta totalt
    val totalCount: Int = 0,                       // hur många varor det finns totalt
    val filter: Filter = Filter.ALL                // det valda filtret
) {
    // Tomt läge visas när det inte finns några varor alls
    val isEmpty: Boolean get() = totalCount == 0

    // Det finns varor, men inga som passar filtret
    val noMatches: Boolean get() = !isEmpty && items.isEmpty()
}