package com.example.maychallenges.ui.tabswitchguard

import com.example.maychallenges.R
import kotlinx.coroutines.delay

enum class Tab(val label: String, val delayMs: Long) {
    ALL("All", 1200L),
    POPULAR("Popular", 700L),
    NEW("New", 300L),
    SALE("Sale", 500L)
}

data class Product(
    val id: Int,
    val name: String,
    val subtitle: String,
    val price: String,
    val image: Int
)

object ProductRepository {

    private val allProducts = listOf(
        Product(1, "Wireless Earbuds Pro",    "Noise cancelling · BT 5.3", "$49.99", R.drawable.earbuds),
        Product(2, "Ergonomic Laptop Stand",  "Aluminum · Adjustable",     "$34.99", R.drawable.laptop_stand),
        Product(3, "USB-C Hub 7-in-1",        "4K HDMI · 100W PD",         "$29.99", R.drawable.usbc),
        Product(4, "Mechanical Keyboard TKL", "RGB · Hot-swap switches",   "$89.99", R.drawable.keyboard),
        Product(5, "Portable Charger 20K",    "USB-C · Fast charge 65W",   "$39.99", R.drawable.charger),
        Product(6, "Webcam HD 1080p",         "Auto-focus · Ring light",   "$54.99", R.drawable.webcam),
        Product(7, "LED Desk Lamp",           "Dimmable · USB-A port",     "$19.99", R.drawable.lamp)
    )

    private fun byIds(vararg ids: Int) = ids.map { id -> allProducts.first { it.id == id } }

    suspend fun getProductsForTab(tab: Tab): List<Product> {
        delay(tab.delayMs)
        return when (tab) {
            Tab.ALL     -> allProducts
            Tab.POPULAR -> byIds(1, 3, 6, 7)
            Tab.NEW     -> byIds(4, 5, 6)
            Tab.SALE    -> byIds(2, 3, 7)
        }
    }
}
