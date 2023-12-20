package com.makovsky.mvi.presentation.details

import com.makovsky.mvi.base.UiEvent
import com.makovsky.mvi.base.UiState
import javax.annotation.concurrent.Immutable

@Immutable
sealed class DetailsScreenUiEvent : UiEvent {
    object LoadingStarted : DetailsScreenUiEvent()
    data class ShowData(val item: DetailsScreenItem?) : DetailsScreenUiEvent()
}

@Immutable
data class DetailsScreenState(
    val isLoading: Boolean,
    val data: DetailsScreenItem?
) : UiState {

    companion object {
        fun initial() = DetailsScreenState(
            isLoading = true,
            data = null
        )
    }

    override fun toString(): String {
        return "isLoading: $isLoading, data is null: ${data == null}"
    }
}