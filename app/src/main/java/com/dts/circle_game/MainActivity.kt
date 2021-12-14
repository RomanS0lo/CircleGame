package com.dts.circle_game

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dts.circle_game.databinding.ActivityMainBinding
import com.dts.circle_game.repository.FireDatabase
import com.dts.circle_game.repository.ScoreRepository
import com.dts.circle_game.scores.ScoresActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TIMER_TIME_DURATION = 10_000L
        private const val TIMER_STEP_DURATION = 1000L
        private const val LEVEL_INCREMENT_DURATION = 3000L
    }

    private lateinit var binding: ActivityMainBinding
    private val scoreRepository by lazy { ScoreRepository(FireDatabase()) }

    private var timerTime = TIMER_TIME_DURATION
    private var timerSum = TIMER_TIME_DURATION

    private var counterSuccess = 0
    private var counterTotal = 0

    private var gameState: GameState = GameState.NONE
        set(value) {
            field = value
            updateUI()
        }

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()

        gameState = GameState.INITIAL
    }

    private fun setupListener() {
        binding.btnScores.setOnClickListener {
            val intent = Intent(this, ScoresActivity::class.java)
            startActivity(intent)
        }

        binding.circleDrawer.setOnCompleteLevel {
            timerTime += LEVEL_INCREMENT_DURATION
            timerSum += LEVEL_INCREMENT_DURATION
            beginTimer()
            counterSuccess++
            binding.tvCount.text = counterSuccess.toString()
        }

        binding.btnStart.setOnClickListener {
            gameState = GameState.STARTED
        }

        binding.etUserName.addTextChangedListener {
            gameState = if (it.toString().isReadyUserName()) GameState.READY
            else GameState.INITIAL
        }
    }

    private fun beginTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timerTime, TIMER_STEP_DURATION) {
            override fun onTick(millisUntilFinished: Long) {
                timerTime -= TIMER_STEP_DURATION
                binding.tvTimer.text = millisUntilFinished.secondsStr
            }

            override fun onFinish() {
                val username = binding.etUserName.text.toString()
                scoreRepository.saveScore(
                    username,
                    counterSuccess,
                    timerSum.millisToSeconds
                )
                Timber.i("${timerSum.millisToSeconds}")
                binding.root.snackbar(
                    resources.getString(
                        R.string.finish_game_format,
                        counterSuccess
                    )
                )
                gameState = GameState.INITIAL
            }
        }
        timer?.start()
    }

    private fun updateUI() {
        when (gameState) {
            GameState.NONE -> Timber.d("Activity is initialized!")
            GameState.INITIAL -> {
                counterSuccess = 0
                counterTotal = 0
                timerTime = TIMER_TIME_DURATION
                binding.tvCount.text = "0"
                binding.etUserName.isEnabled = true
                binding.circleDrawer.isCanDraw = false
                binding.circleDrawer.invalidate()
                binding.btnScores.isEnabled = true
                binding.btnStart.isEnabled = binding.etUserName.text.toString().isReadyUserName()
            }
            GameState.READY -> {
                binding.btnStart.isEnabled = true
            }
            GameState.STARTED -> {
                binding.etUserName.isEnabled = false
                binding.circleDrawer.isCanDraw = true
                binding.circleDrawer.invalidate()
                binding.btnScores.isEnabled = false
                binding.btnStart.isEnabled = false
                beginTimer()
            }
        }
    }
}
