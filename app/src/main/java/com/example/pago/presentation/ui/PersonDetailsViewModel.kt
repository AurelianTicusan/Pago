package com.example.pago.presentation.ui

import androidx.lifecycle.ViewModel
import com.example.pago.domain.usecase.GetPostsByUserIdUseCase
import com.example.pago.presentation.mapper.toPostItem
import com.example.pago.presentation.state.PostItem
import com.example.pago.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val getPostsByUserIdUseCase: GetPostsByUserIdUseCase
) : ViewModel() {

    private val _postsState: MutableStateFlow<UiState<List<PostItem>>> =
        MutableStateFlow(value = UiState.Loading())
    val postsState: StateFlow<UiState<List<PostItem>>> get() = _postsState.asStateFlow()

    private var postsDisposable: Disposable? = null

    fun initialize(userId: Int) {
        postsDisposable = getPosts(userId)
    }

    private fun getPosts(userId: Int) = getPostsByUserIdUseCase.execute(userId)
        .subscribeBy(
            onSuccess = { people ->
                _postsState.value = UiState.Loaded(people.map { it.toPostItem() })
            },
            onError = {
                _postsState.value = UiState.Error(it.localizedMessage)
            }
        )

    override fun onCleared() {
        postsDisposable?.apply { if (isDisposed.not()) dispose() }
        super.onCleared()
    }
}