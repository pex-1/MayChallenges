package com.example.maychallenges.ui.tabswitchguard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maychallenges.R
import com.example.maychallenges.ui.theme.MayChallengesTheme

@Composable
fun TabSwitchScreen(
    onBack: () -> Unit = {},
    viewModel: TabSwitchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    TabSwitchContent(uiState = uiState, onTabSelected = viewModel::onTabSelected, onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabSwitchContent(
    uiState: TabSwitchUiState,
    onTabSelected: (Tab) -> Unit,
    onBack: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            TabBar(selectedTab = uiState.selectedTab, onTabSelected = onTabSelected)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            when (val state = uiState.contentState) {
                is TabContentState.Loading -> LoadingContent(uiState.selectedTab.label)
                is TabContentState.Loaded -> ProductList(state.products)
            }
        }
    }
}

@Composable
private fun TabBar(selectedTab: Tab, onTabSelected: (Tab) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Tab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(tab) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = tab.label,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(1.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(tabName: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(32.dp)
            )
            Text(
                "Loading \"$tabName\"...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductList(products: List<Product>) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
        item {
            Text(
                "${products.size} results",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        items(products, key = { it.id }) { product ->
            ProductCard(product)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(product.image),
                contentDescription = null
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(
                product.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(product.price, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

// previews

@Preview(name = "All Tab – Loading", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewAllLoading() {
    MayChallengesTheme {
        TabSwitchContent(
            uiState = TabSwitchUiState(
                selectedTab = Tab.ALL,
                contentState = TabContentState.Loading
            ),
            onTabSelected = {}, onBack = {}
        )
    }
}

@Preview(name = "Popular Tab – Loaded", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPopularLoaded() {
    MayChallengesTheme {
        TabSwitchContent(
            uiState = TabSwitchUiState(
                selectedTab = Tab.POPULAR,
                contentState = TabContentState.Loaded(
                    listOf(
                        Product(
                            1,
                            "Wireless Earbuds Pro",
                            "Noise cancelling · BT 5.3",
                            "$49.99",
                            R.drawable.earbuds
                        ),
                        Product(3, "USB-C Hub 7-in-1", "4K HDMI · 100W PD", "$29.99", R.drawable.usbc),
                        Product(6, "Webcam HD 1080p", "Auto-focus · Ring light", "$54.99", R.drawable.webcam),
                        Product(7, "LED Desk Lamp", "Dimmable · USB-A port", "$19.99", R.drawable.lamp),
                    )
                )
            ),
            onTabSelected = {}, onBack = {}
        )
    }
}

@Preview(name = "Sale Tab – Loading", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSaleLoading() {
    MayChallengesTheme {
        TabSwitchContent(
            uiState = TabSwitchUiState(
                selectedTab = Tab.SALE,
                contentState = TabContentState.Loading
            ),
            onTabSelected = {}, onBack = {}
        )
    }
}

@Preview(name = "Product Card", showBackground = true, widthDp = 360)
@Composable
private fun PreviewProductCard() {
    MayChallengesTheme {
        Surface {
            ProductCard(
                Product(
                    1,
                    "Wireless Earbuds Pro",
                    "Noise cancelling · BT 5.3",
                    "$49.99",
                    R.drawable.earbuds
                )
            )
        }
    }
}
