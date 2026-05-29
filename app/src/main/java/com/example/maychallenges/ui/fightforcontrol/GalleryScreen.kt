package com.example.maychallenges.ui.fightforcontrol

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maychallenges.ui.theme.MayChallengesTheme
import com.example.minichallenges.challenges.fightforcontrol.GalleryUiState
import com.example.minichallenges.challenges.fightforcontrol.GalleryViewModel

enum class SheetAnchor { COLLAPSED, HALF, FULL }

private val PeekHeight = 100.dp
private val HalfFraction = 0.52f

// ── Entry point ───────────────────────────────────────────────────────────────

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

// ── Main content ──────────────────────────────────────────────────────────────

@Composable
fun GalleryContent(
    uiState: GalleryUiState,
    onBack: () -> Unit,
    onCategorySelected: (Category) -> Unit,
    onImageSelected: (GalleryImage) -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    var anchor by remember { mutableStateOf(SheetAnchor.COLLAPSED) }
    var dragDeltaPx by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val totalHeight = maxHeight
        val peekDp = PeekHeight
        val halfDp = totalHeight * HalfFraction
        val maxDp = totalHeight - statusBarPadding

        fun anchorHeight(a: SheetAnchor) = when (a) {
            SheetAnchor.COLLAPSED -> peekDp
            SheetAnchor.HALF -> halfDp
            SheetAnchor.FULL -> maxDp
        }

        // Live height while dragging
        val rawDp = anchorHeight(anchor) - with(density) { dragDeltaPx.toDp() }
        val sheetHeight = rawDp.coerceIn(peekDp, maxDp)

        // Snap on finger lift. velocity < 0 = moving up = sheet growing.
        // The key fix: if currently HALF and moving up at all, go FULL.
        // If currently HALF and moving down at all, go COLLAPSED.
        fun snap(velocityPx: Float) {
            anchor = when (anchor) {
                SheetAnchor.COLLAPSED -> {
                    // any upward movement → go to HALF; downward stays COLLAPSED
                    if (dragDeltaPx < -with(density) { 30.dp.toPx() } || velocityPx < -200f)
                        SheetAnchor.HALF else SheetAnchor.COLLAPSED
                }

                SheetAnchor.HALF -> {
                    when {
                        // Dragged or flung up enough → FULL
                        dragDeltaPx < -with(density) { 40.dp.toPx() } || velocityPx < -300f -> SheetAnchor.FULL
                        // Dragged or flung down enough → COLLAPSED
                        dragDeltaPx > with(density) { 40.dp.toPx() } || velocityPx > 300f -> SheetAnchor.COLLAPSED
                        else -> SheetAnchor.HALF
                    }
                }

                SheetAnchor.FULL -> {
                    // any downward movement → go to HALF
                    if (dragDeltaPx > with(density) { 30.dp.toPx() } || velocityPx > 200f)
                        SheetAnchor.HALF else SheetAnchor.FULL
                }
            }
            dragDeltaPx = 0f
        }

        // ── Background image ──────────────────────────────────────────────────
        val previewBitmap = remember(uiState.selectedImage.assetPath) {
            try {
                context.assets.open(uiState.selectedImage.assetPath)
                    .use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
            } catch (_: Exception) {
                null
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (previewBitmap != null) {
                Image(
                    bitmap = previewBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF263238)))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(0.45f)
                            )
                        )
                    )
            )
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(start = 4.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // ── Bottom sheet ──────────────────────────────────────────────────────
        val gridState = rememberLazyGridState()
        var gridMovingSheet by remember { mutableStateOf(false) }

        val gridNestedScroll = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (source != NestedScrollSource.UserInput) return Offset.Zero
                    // Swiping DOWN + grid at very top → move sheet down
                    if (available.y > 0f && !gridState.canScrollBackward) {
                        val max = with(density) { (sheetHeight - peekDp).toPx() }
                        val take = available.y.coerceAtMost(max)
                        dragDeltaPx += take
                        gridMovingSheet = true
                        return Offset(0f, take)
                    }
                    return Offset.Zero
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    if (source != NestedScrollSource.UserInput) return Offset.Zero
                    // Swiping UP + grid hit the end → move sheet up
                    if (available.y < 0f) {
                        val max = with(density) { (maxDp - sheetHeight).toPx() }
                        val take = available.y.coerceAtLeast(-max)
                        dragDeltaPx += take
                        gridMovingSheet = true
                        return Offset(0f, take)
                    }
                    return Offset.Zero
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    if (gridMovingSheet) {
                        gridMovingSheet = false
                        snap(available.y)
                        return available
                    }
                    return Velocity.Zero
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    if (gridMovingSheet) {
                        gridMovingSheet = false
                        snap(0f)
                    }
                    return Velocity.Zero
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Drag handle — always controls sheet directly ──────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                val minD = with(density) { -(maxDp - sheetHeight).toPx() }
                                val maxD = with(density) { (sheetHeight - peekDp).toPx() }
                                dragDeltaPx = (dragDeltaPx + delta).coerceIn(minD, maxD)
                            },
                            onDragStopped = { velocity -> snap(velocity) }
                        )
                        .padding(top = 12.dp, bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCCCCCC))
                        )
                        // "Swipe up to explore" always visible (not just when collapsed)
                        Text(
                            text = "Swipe up to explore",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1A6EFC)
                        )
                    }
                }

                // ── Tabs + grid — only when not collapsed ─────────────────────
                if (sheetHeight > peekDp + 8.dp) {

                    // Tabs: text buttons with underline indicator (like mockup)
                    val tabScroll = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(tabScroll)
                            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Category.entries.forEach { cat ->
                            val selected = cat == uiState.selectedCategory
                            Column(
                                modifier = Modifier
                                    .clickable { onCategorySelected(cat) }
                                    .padding(end = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = cat.label,
                                    fontSize = 14.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (selected) Color(0xFF1A6EFC) else Color(0xFF888888),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                // Underline indicator
                                Box(
                                    modifier = Modifier
                                        .height(2.dp)
                                        .width(if (selected) 28.dp else 0.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1A6EFC))
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    // Image grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(gridNestedScroll),
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            end = 8.dp,
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(uiState.visibleImages, key = { it.id }) { image ->
                            val bitmap: ImageBitmap? = remember(image.assetPath) {
                                try {
                                    context.assets.open(image.assetPath)
                                        .use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
                                } catch (_: Exception) {
                                    null
                                }
                            }
                            ImageTile(
                                image = image,
                                bitmap = bitmap,
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

// ── Image tile ────────────────────────────────────────────────────────────────

@Composable
fun ImageTile(
    image: GalleryImage,
    bitmap: ImageBitmap?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isSelected) Modifier.border(3.dp, Color(0xFF1A6EFC), RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable(onClick = onClick)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDDDDDD)))
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp, end = 8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A6EFC)),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(name = "Gallery", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewGallery() {
    MayChallengesTheme {
        GalleryContent(
            uiState = GalleryUiState(),
            onBack = {}, onCategorySelected = {}, onImageSelected = {}
        )
    }
}