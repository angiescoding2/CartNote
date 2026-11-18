package com.example.cartnote

// Avdelningarna i butiken. Användaren väljer en av dessa i rullgardinen.
// label är den svenska texten som visas i appen.
enum class Section(val label: String) {
    FRUIT_VEG("Frukt & grönt"),
    DAIRY("Mejeri"),
    BREAD("Bröd"),
    MEAT_FISH("Kött & fisk"),
    PANTRY("Skafferi"),
    OTHER("Övrigt")
}

// En vara i inköpslistan.
data class ShoppingItem(
    val id: Int,                  // Unikt nummer, så vi vet exakt vilken rad som ska ändras
    val name: String,             // Varans namn, t.ex. "Mjölk"
    val section: Section,         // Vilken avdelning varan hör till
    val isBought: Boolean = false // Köpt eller inte. Nya varor börjar som inte köpta.
)