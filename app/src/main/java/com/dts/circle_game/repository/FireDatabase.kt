package com.dts.circle_game.repository

import com.dts.circle_game.model.Score
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class FireDatabase : ScoreDataSource {

    private val database = Firebase.database.reference

    private val reference = database.child("scores")

    override fun getAll(onComplete: (List<Score>) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val resultList = mutableListOf<Score>()
                snapshot.children.forEach { child ->
                    child.getValue<Score>()?.let { score ->
                        resultList.add(score)
                    } ?: Timber.w("Received null score item $child")
                }
                onComplete(resultList)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.d(error.message)
            }
        })
    }

    override fun saveScore(score: Score) {
        val childScore = reference.push()
        childScore.setValue(score)
    }
}
