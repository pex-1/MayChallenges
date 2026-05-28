package com.example.maychallenges.ui.fightforcontrol

enum class Category(val label: String) {
    NATURE("Nature"), TRAVEL("Travel"), CITY("City"),
    FOOD("Food"), ANIMALS("Animals"), PEOPLE("People")
}

data class GalleryImage(val id: Int, val category: Category, val assetPath: String)

val allImages = listOf(
    GalleryImage(1,  Category.NATURE,  "images/nature/nature_1.webp" ),
    GalleryImage(2,  Category.NATURE,  "images/nature/nature_2.webp" ),
    GalleryImage(3,  Category.NATURE,  "images/nature/nature_3.webp" ),
    GalleryImage(4,  Category.NATURE,  "images/nature/nature_4.webp" ),
    GalleryImage(5,  Category.NATURE,  "images/nature/nature_5.webp" ),
    GalleryImage(6,  Category.NATURE,  "images/nature/nature_6.webp" ),
    GalleryImage(7,  Category.TRAVEL,  "images/travel/travel_1.webp" ),
    GalleryImage(8,  Category.TRAVEL,  "images/travel/travel_2.webp" ),
    GalleryImage(9,  Category.TRAVEL,  "images/travel/travel_3.webp" ),
    GalleryImage(10, Category.TRAVEL,  "images/travel/travel_4.webp" ),
    GalleryImage(11, Category.TRAVEL,  "images/travel/travel_5.webp" ),
    GalleryImage(12, Category.CITY,    "images/city/city_1.webp" ),
    GalleryImage(13, Category.CITY,    "images/city/city_2.webp" ),
    GalleryImage(14, Category.CITY,    "images/city/city_3.webp" ),
    GalleryImage(15, Category.CITY,    "images/city/city_4.webp" ),
    GalleryImage(16, Category.FOOD,    "images/food/food_1.webp" ),
    GalleryImage(17, Category.FOOD,    "images/food/food_2.webp" ),
    GalleryImage(18, Category.FOOD,    "images/food/food_3.webp" ),
    GalleryImage(19, Category.FOOD,    "images/food/food_4.webp" ),
    GalleryImage(20, Category.ANIMALS, "images/animals/animals_1.webp" ),
    GalleryImage(21, Category.ANIMALS, "images/animals/animals_2.webp" ),
    GalleryImage(22, Category.ANIMALS, "images/animals/animals_3.webp" ),
    GalleryImage(23, Category.ANIMALS, "images/animals/animals_4.webp" ),
    GalleryImage(24, Category.PEOPLE,  "images/people/people_1.webp" ),
    GalleryImage(25, Category.PEOPLE,  "images/people/people_2.webp" ),
    GalleryImage(26, Category.PEOPLE,  "images/people/people_3.webp" ),
    GalleryImage(27, Category.PEOPLE,  "images/people/people_4.webp" ),
    GalleryImage(28, Category.PEOPLE,  "images/people/people_5.webp" ),
    GalleryImage(29, Category.PEOPLE,  "images/people/people_6.webp" ),
    GalleryImage(30, Category.PEOPLE,  "images/people/people_7.webp" ),
    GalleryImage(31, Category.PEOPLE,  "images/people/people_8.webp" ),
)

fun imagesForCategory(category: Category): List<GalleryImage> =
    allImages.filter { it.category == category }
