package com.example.pago.presentation.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.pago.domain.usecase.GetPersonsUseCase
import com.example.pago.presentation.mapper.toPersonItem
import com.example.pago.presentation.state.PersonItem
import com.example.pago.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPersonsUseCase: GetPersonsUseCase
) : ViewModel() {

    private val _personsState: MutableStateFlow<UiState<List<PersonItem>>> =
        MutableStateFlow(value = UiState.Loading())
    val personsState: StateFlow<UiState<List<PersonItem>>> get() = _personsState.asStateFlow()

    private var personsDisposable: Disposable? = null

    fun initialize() {
        Log.d("ababa", "initialize: ")
        val list = mutableStateListOf<PersonItem>().apply {
            repeat(12) {
                add(PersonItem(it, "Person Number $it", "", "", "active"))
            }
        }
        personsDisposable = Single.just(list)
            .subscribeBy(
                onSuccess = { people ->
                    Log.d("ababa", "getPersons: " + people)
                    _personsState.value = UiState.Loaded(people)
                },
                onError = {
                    Log.d("ababa", "getPersons: " + it)
                    _personsState.value = UiState.Error(it.localizedMessage)
                })
    }

    private fun getPersons() = getPersonsUseCase.execute()
        .subscribeBy(
            onSuccess = { people ->
                Log.d("ababa", "getPersons: " + people)
                _personsState.value = UiState.Loaded(people.map { it.toPersonItem() })
            },
            onError = {
                Log.d("ababa", "getPersons: " + it)
                _personsState.value = UiState.Error(it.localizedMessage)
            }
        )

    override fun onCleared() {
        personsDisposable?.apply { if (isDisposed.not()) dispose() }
        super.onCleared()
    }
}