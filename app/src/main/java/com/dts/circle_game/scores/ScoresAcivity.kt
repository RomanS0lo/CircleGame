package com.dts.circle_game.scores

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.dts.circle_game.databinding.ActivityScoresBinding
import com.dts.circle_game.scores.adapter.ScoresAdapter

class ScoresActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[ScoresViewModel::class.java]
    }

    private val adapter = ScoresAdapter()
    private lateinit var binding: ActivityScoresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupObservers()
        viewModel.fetch()
        setupRecyclerViewDecoration()
    }

    private fun setupAdapter() {
        binding.rvScores.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.onScoresUpdate().observe(this) { scores ->
            adapter.items = scores
        }
    }

    private fun setupRecyclerViewDecoration(){
        binding.rvScores.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }
}
