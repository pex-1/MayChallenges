package com.example.maychallenges.ui.tabswitchguard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface TabContentState {
    data object Loading : TabContentState
    data class Loaded(val products: List<Product>) : TabContentState
}

data class TabSwitchUiState(
    val selectedTab: Tab = Tab.ALL,
    val contentState: TabContentState = TabContentState.Loading
)

class TabSwitchViewModel(
    private val repository: ProductRepository = ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TabSwitchUiState())
    val uiState: StateFlow<TabSwitchUiState> = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init { loadTab(Tab.ALL) }

    fun onTabSelected(tab: Tab) {
        if (_uiState.value.selectedTab == tab && _uiState.value.contentState is TabContentState.Loaded) return
        loadingJob?.cancel()
        _uiState.update { it.copy(selectedTab = tab, contentState = TabContentState.Loading) }
        loadTab(tab)
    }

    private fun loadTab(tab: Tab) {
        loadingJob = viewModelScope.launch {
            val products = repository.getProductsForTab(tab)
            if (_uiState.value.selectedTab == tab) {
                _uiState.update { it.copy(contentState = TabContentState.Loaded(products)) }
            }
        }
    }
}
