package com.example.minichallenges.challenges.fightforcontrol

import androidx.lifecycle.ViewModel
import com.example.maychallenges.ui.fightforcontrol.Category
import com.example.maychallenges.ui.fightforcontrol.GalleryImage
import com.example.maychallenges.ui.fightforcontrol.allImages
import com.example.maychallenges.ui.fightforcontrol.imagesForCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GalleryUiState(
    val selectedCategory: Category = Category.NATURE,
    val selectedImage: GalleryImage = allImages.first(),
    val visibleImages: List<GalleryImage> = imagesForCategory(Category.NATURE)
)

class GalleryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(selectedCategory = category, visibleImages = imagesForCategory(
            category
        )
        ) }
    }

    fun onImageSelected(image: GalleryImage) {
        _uiState.update { it.copy(selectedImage = image) }
    }
}
