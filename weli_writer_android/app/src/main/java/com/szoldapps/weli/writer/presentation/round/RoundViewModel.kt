package com.szoldapps.weli.writer.presentation.round

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.szoldapps.weli.writer.domain.RoundValue
import com.szoldapps.weli.writer.domain.WeliRepository
import com.szoldapps.weli.writer.presentation.round.RoundViewState.Content
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import kotlin.random.Random

class RoundViewModel @ViewModelInject constructor(
    private val weliRepository: WeliRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val roundId: Long =
        savedStateHandle.get<Long>("roundId") ?: throw kotlin.IllegalStateException("Mandatory roundId is missing!")

    val viewState: LiveData<RoundViewState> =
        Transformations.map(weliRepository.roundValuesByRoundId(roundId)) { roundValues ->
            Content(roundValues)
        }

    fun addRandomRound() = viewModelScope.launch {
        weliRepository.addRoundValue(
            RoundValue(
                date = OffsetDateTime.now(),
                number = Random.nextInt(),
                value = Random.nextInt(),
            ),
            roundId,
        )
    }
}

sealed class RoundViewState {
    object Loading : RoundViewState()
    object Error : RoundViewState()
    data class Content(val rounds: List<RoundValue>) : RoundViewState()
}
