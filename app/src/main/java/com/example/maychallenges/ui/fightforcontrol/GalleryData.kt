package com.example.maychallenges.ui.fightforcontrol

enum class Category(val label: String) {
    NATURE("Nature"), TRAVEL("Travel"), CITY("City"),
    FOOD("Food"), ANIMALS("Animals"), PEOPLE("People")
}

data class GalleryImage(val id: Int, val category: Category, val assetPath: String)

val allImages = listOf(
    GalleryImage(1,  Category.NATURE,  "images/nature1.webp" ),
    GalleryImage(2,  Category.NATURE,  "images/nature2.webp" ),
    GalleryImage(3,  Category.NATURE,  "images/nature3.webp" ),
    GalleryImage(4,  Category.NATURE,  "images/nature4.webp" ),
    GalleryImage(5,  Category.NATURE,  "images/nature5.webp" ),
    GalleryImage(6,  Category.NATURE,  "images/nature6.webp" ),
    GalleryImage(7,  Category.TRAVEL,  "images/travel1.webp" ),
    GalleryImage(8,  Category.TRAVEL,  "images/travel2.webp" ),
    GalleryImage(9,  Category.TRAVEL,  "images/travel3.webp" ),
    GalleryImage(10, Category.TRAVEL,  "images/travel4.webp" ),
    GalleryImage(11, Category.TRAVEL,  "images/travel5.webp" ),
    GalleryImage(12, Category.CITY,    "images/city1.webp" ),
    GalleryImage(13, Category.CITY,    "images/city2.webp" ),
    GalleryImage(14, Category.CITY,    "images/city3.webp" ),
    GalleryImage(15, Category.CITY,    "images/city4.webp" ),
    GalleryImage(16, Category.FOOD,    "images/food1.webp" ),
    GalleryImage(17, Category.FOOD,    "images/food2.webp" ),
    GalleryImage(18, Category.FOOD,    "images/food3.webp" ),
    GalleryImage(19, Category.FOOD,    "images/food4.webp" ),
    GalleryImage(20, Category.ANIMALS, "images/animals1.webp" ),
    GalleryImage(21, Category.ANIMALS, "images/animals2.webp" ),
    GalleryImage(22, Category.ANIMALS, "images/animals3.webp" ),
    GalleryImage(23, Category.ANIMALS, "images/animals4.webp" ),
    GalleryImage(24, Category.PEOPLE,  "images/people1.webp" ),
    GalleryImage(25, Category.PEOPLE,  "images/people2.webp" ),
    GalleryImage(26, Category.PEOPLE,  "images/people3.webp" ),
)

fun imagesForCategory(category: Category): List<GalleryImage> =
    allImages.filter { it.category == category }
