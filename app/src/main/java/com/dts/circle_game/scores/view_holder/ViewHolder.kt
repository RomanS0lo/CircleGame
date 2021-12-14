package com.dts.circle_game.scores.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dts.circle_game.databinding.CellItemBinding
import com.dts.circle_game.model.Score

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: CellItemBinding = CellItemBinding.bind(view)

    fun bind(data: Score) {
        binding.tvPlace.text = adapterPosition.plus(1).toString()
        binding.tvScore.text = data.score.toString()
        binding.tvUserName.text = data.userName
    }
}
