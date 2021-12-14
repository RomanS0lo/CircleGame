package com.dts.circle_game.scores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dts.circle_game.model.Score
import com.dts.circle_game.repository.FireDatabase
import com.dts.circle_game.repository.ScoreRepository

class ScoresViewModel : ViewModel() {

    private val scoreRepository by lazy { ScoreRepository(FireDatabase()) }
    private val itemsLiveData = MutableLiveData<List<Score>>()

    fun onScoresUpdate(): LiveData<List<Score>> = itemsLiveData

    fun fetch() {
        scoreRepository.getAll { scores ->
            itemsLiveData.postValue(scores)
        }
    }
}
