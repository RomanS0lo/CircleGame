package com.dts.circle_game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import timber.log.Timber
import kotlin.math.pow
import kotlin.random.Random


class CircleDrawerView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        private const val CIRCLES_COUNT_MIN = 1
        private const val CIRCLES_COUNT_MAX = 11
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintStroke = Paint(Paint.ANTI_ALIAS_FLAG)
    private val radius = context.resources.getDimensionPixelSize(R.dimen.circle_radius).toFloat()
    private var listOfCircles = mutableListOf<Circle>()

    private var onCompleteLevel: (() -> Unit)? = null

    var isCanDraw = false
    var countCircle = 0

    init {
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL

        paintStroke.style = Paint.Style.STROKE
        paintStroke.color = Color.GRAY
        paintStroke.strokeWidth = 10f
        setupListeners()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        if (!isCanDraw) return

        drawCircles(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val x = event.x
                val y = event.y
                val result = listOfCircles.firstOrNull {
                    (x - it.x).pow(2) + (y - it.y).pow(2) <= radius.pow(2)
                }
                Timber.d("x= $x, y= $y | ${event.rawX} - ${event.rawY}")
                if (result != null) {
                    Timber.d("is inside")
                    listOfCircles.remove(result)
                    if (listOfCircles.isEmpty()) onCompleteLevel?.invoke()
                    postInvalidate()
                    true
                } else {
                    false
                }
            } else {
                true
            }
        }
    }

    private fun drawCircles(canvas: Canvas) {
        val width = canvas.width
        val height = canvas.height

        countCircle = Random.nextInt(CIRCLES_COUNT_MIN, CIRCLES_COUNT_MAX)
        if (listOfCircles.isEmpty()) {
            while (listOfCircles.size < countCircle) {
                val centerX = Random.nextInt(radius.toInt(), width - radius.toInt()).toFloat()
                val centerY = Random.nextInt(radius.toInt(), height - radius.toInt()).toFloat()
                canvas.drawCircle(centerX, centerY, radius, paint)
                canvas.drawCircle(centerX, centerY, radius, paintStroke)
                listOfCircles.add(Circle(centerX, centerY))
            }
        } else {
            listOfCircles.forEach { circle ->
                canvas.drawCircle(circle.x, circle.y, radius, paint)
                canvas.drawCircle(
                    circle.x,
                    circle.y,
                    radius,
                    paintStroke
                )
            }
        }
    }

    fun setOnCompleteLevel(onComplete: () -> Unit) {
        this.onCompleteLevel = onComplete
    }
}
