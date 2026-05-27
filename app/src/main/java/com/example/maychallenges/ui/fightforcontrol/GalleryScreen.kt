package com.example.maychallenges.ui.fightforcontrol

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maychallenges.ui.theme.MayChallengesTheme
import com.example.minichallenges.challenges.fightforcontrol.GalleryUiState
import com.example.minichallenges.challenges.fightforcontrol.GalleryViewModel

enum class SheetState { COLLAPSED, HALF_EXPANDED, FULLY_EXPANDED }

private val PeekHeightDp = 80.dp
private val HalfExpandedFraction = 0.52f

// ── Stateful entry point ──────────────────────────────────────────────────────

@Composable
fun GalleryScreen(
    onBack: () -> Unit = {},
    viewModel: GalleryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MayChallengesTheme {
        GalleryContent(
            uiState = uiState,
            onBack = onBack,
            onCategorySelected = viewModel::onCategorySelected,
            onImageSelected = viewModel::onImageSelected
        )
    }
}

// ── Stateless content ─────────────────────────────────────────────────────────

@Composable
fun GalleryContent(
    uiState: GalleryUiState,
    onBack: () -> Unit,
    onCategorySelected: (Category) -> Unit,
    onImageSelected: (GalleryImage) -> Unit
) {
    val density = LocalDensity.current
    var sheetState by remember { mutableStateOf(SheetState.HALF_EXPANDED) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeightPx = with(density) { maxHeight.toPx() }
        val peekHeightPx = with(density) { PeekHeightDp.toPx() }
        val halfHeightPx = screenHeightPx * HalfExpandedFraction

        val sheetHeightDp = with(density) {
            when (sheetState) {
                SheetState.COLLAPSED -> peekHeightPx
                SheetState.HALF_EXPANDED -> halfHeightPx
                SheetState.FULLY_EXPANDED -> screenHeightPx
            }.toDp()
        }

        // Background preview
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(uiState.selectedImage.color)),
            contentAlignment = Alignment.Center
        ) {
            Text(uiState.selectedImage.emoji, fontSize = 96.sp, textAlign = TextAlign.Center)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f)
                            )
                        )
                    )
            )
            // Back button overlay
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        val gridScrollState = rememberLazyGridState()

        val nestedScrollConnection = remember(sheetState) {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (available.y > 0 && !gridScrollState.canScrollBackward) {
                        sheetState = when (sheetState) {
                            SheetState.FULLY_EXPANDED -> SheetState.HALF_EXPANDED
                            SheetState.HALF_EXPANDED -> SheetState.COLLAPSED
                            else -> return Offset.Zero
                        }
                        return available
                    }
                    return Offset.Zero
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    if (available.y < 0 && !gridScrollState.canScrollForward) {
                        sheetState = when (sheetState) {
                            SheetState.HALF_EXPANDED -> SheetState.FULLY_EXPANDED
                            SheetState.COLLAPSED -> SheetState.HALF_EXPANDED
                            else -> return Offset.Zero
                        }
                        return available
                    }
                    return Offset.Zero
                }
            }
        }

        // BottomSheet
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeightDp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Drag handle
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState {},
                            onDragStopped = { velocity ->
                                when (sheetState) {
                                    SheetState.COLLAPSED -> if (velocity < -200f) sheetState =
                                        SheetState.HALF_EXPANDED

                                    SheetState.HALF_EXPANDED -> {
                                        if (velocity < -200f) sheetState = SheetState.FULLY_EXPANDED
                                        else if (velocity > 200f) sheetState = SheetState.COLLAPSED
                                    }

                                    SheetState.FULLY_EXPANDED -> if (velocity > 200f) sheetState =
                                        SheetState.HALF_EXPANDED
                                }
                            }
                        )
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                    )
                    if (sheetState == SheetState.COLLAPSED) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Swipe up to explore",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (sheetState != SheetState.COLLAPSED) {
                    // Category tabs
                    val tabScrollState = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(tabScrollState)
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Category.entries.forEach { cat ->
                            val isSelected = cat == uiState.selectedCategory
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { onCategorySelected(cat) }
                                    .padding(horizontal = 16.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    cat.label, fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Image grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridScrollState,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedScrollConnection),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.visibleImages, key = { it.id }) { image ->
                            ImageTile(
                                image = image,
                                isSelected = image.id == uiState.selectedImage.id,
                                onClick = { onImageSelected(image) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageTile(image: GalleryImage, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(image.color))
            .then(
                if (isSelected) Modifier.border(
                    3.dp,
                    Color.White,
                    RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(image.emoji, fontSize = 40.sp)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 12.sp, color = Color(image.color))
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Gallery – Half Expanded (Nature)", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewHalfExpanded() {
    MayChallengesTheme {
        GalleryContent(
            uiState = GalleryUiState(),
            onBack = {}, onCategorySelected = {}, onImageSelected = {}
        )
    }
}

@Preview(name = "Gallery – Food Category", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewFoodCategory() {
    MayChallengesTheme {
        GalleryContent(
            uiState = GalleryUiState(
                selectedCategory = Category.FOOD,
                selectedImage = allImages.first { it.category == Category.FOOD },
                visibleImages = imagesForCategory(Category.FOOD)
            ),
            onBack = {}, onCategorySelected = {}, onImageSelected = {}
        )
    }
}

@Preview(name = "Image Tile – Default", showBackground = true, widthDp = 180)
@Composable
private fun PreviewImageTileDefault() {
    MayChallengesTheme {
        Surface(modifier = Modifier.padding(8.dp)) {
            ImageTile(image = allImages[0], isSelected = false, onClick = {})
        }
    }
}

@Preview(name = "Image Tile – Selected", showBackground = true, widthDp = 180)
@Composable
private fun PreviewImageTileSelected() {
    MayChallengesTheme {
        Surface(modifier = Modifier.padding(8.dp)) {
            ImageTile(image = allImages[0], isSelected = true, onClick = {})
        }
    }
}
