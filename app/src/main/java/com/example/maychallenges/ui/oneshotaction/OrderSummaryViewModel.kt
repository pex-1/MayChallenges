package com.example.maychallenges.ui.oneshotaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

enum class OrderState {
    IDLE,
    PROCESSING,
    DONE
}

data class OrderUiState(
    val clicks: Int = 0,
    val requestsStarted: Int = 0,
    val orderState: OrderState = OrderState.IDLE
)

class OrderSummaryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    private val mutex = Mutex()

    fun onPlaceOrderClicked() {
        // Always increment clicks, regardless of processing state
        _uiState.update { it.copy(clicks = it.clicks + 1) }

        // Attempt to start the operation — will be skipped if already locked
        viewModelScope.launch {
            val acquired = mutex.tryLock()
            if (!acquired) {
                // Operation already in progress — do nothing further
                return@launch
            }

            try {
                // Reset counters and start processing
                _uiState.update { current ->
                    current.copy(
                        requestsStarted = 1,
                        orderState = OrderState.PROCESSING
                    )
                }

                delay(2500L)

                _uiState.update { it.copy(orderState = OrderState.DONE) }
            } finally {
                mutex.unlock()
            }
        }
    }

    fun onPlaceOrderClickedFromDone() {
        _uiState.update {
            OrderUiState(
                clicks = 1,
                requestsStarted = 1,
                orderState = OrderState.PROCESSING
            )
        }

        viewModelScope.launch {
            delay(2500L)
            _uiState.update { it.copy(orderState = OrderState.DONE) }
        }
    }
}
