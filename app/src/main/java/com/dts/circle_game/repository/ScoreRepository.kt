package com.dts.circle_game.repository

import com.dts.circle_game.model.Score

class ScoreRepository(private val dataSource: ScoreDataSource) {

    fun getAll(onComplete: ((List<Score>) -> Unit)) {
        dataSource.getAll { scores ->
            onComplete(scores.sortedByDescending { it.score })
        }
    }

    fun saveScore(username: String, score: Int, timeSeconds: Int) {
        dataSource.saveScore(Score(username, score, timeSeconds))
    }
}
