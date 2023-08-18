package co.fanavari.androidfanavari40205.ui.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    init {
        // Initialize score to 0
        _score.value = 0
    }

    fun incrementScore() {
        _score.value = _score.value?.plus(1)

    }

    override fun onCleared() {
        // Release resources
        super.onCleared()
    }
}