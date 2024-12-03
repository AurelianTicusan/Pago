package com.example.pago.presentation.ui

import androidx.lifecycle.ViewModel
import com.example.pago.domain.usecase.GetActivePersonsUseCase
import com.example.pago.presentation.mapper.toPersonItem
import com.example.pago.presentation.state.PersonItem
import com.example.pago.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getActivePersonsUseCase: GetActivePersonsUseCase
) : ViewModel() {

    private val _personsState: MutableStateFlow<UiState<List<PersonItem>>> =
        MutableStateFlow(value = UiState.Loading())
    val personsState: StateFlow<UiState<List<PersonItem>>> get() = _personsState.asStateFlow()

    private var personsDisposable: Disposable? = null

    init {
        personsDisposable = getPersons()
    }

    private fun getPersons() = getActivePersonsUseCase.execute()
        .subscribeBy(
            onSuccess = { people ->
                _personsState.value = UiState.Loaded(people.map { it.toPersonItem() })
            },
            onError = {
                _personsState.value = UiState.Error(it.localizedMessage)
            }
        )

    override fun onCleared() {
        personsDisposable?.apply { if (isDisposed.not()) dispose() }
        super.onCleared()
    }
}