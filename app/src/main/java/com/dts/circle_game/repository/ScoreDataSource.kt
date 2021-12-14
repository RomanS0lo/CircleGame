package com.dts.circle_game.repository

import com.dts.circle_game.model.Score

interface ScoreDataSource {

    fun getAll(onComplete: (List<Score>) -> Unit)

    fun saveScore(score: Score)
}
