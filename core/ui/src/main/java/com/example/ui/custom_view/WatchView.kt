package com.example.ui.custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.ui.R
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

private const val PI = Math.PI.toFloat()
private const val NUMBER_SHIFT = 47
private const val POINT_SHIFT = 16

class WatchView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var height = 0f
    private var width = 0f
    private var radius = 0f
    private var angle = 0f
    private var centreX = 0f
    private var centreY = 0f
    private var padding = 0f
    private var isInit = false
    private var paint: Paint? = null
    private var path: Path? = null
    private var rect: Rect? = null
    private var numbers = arrayOf<Int>()
    private var minimum = 0f
    private var hour = 0f
    private var minute = 0f
    private var second = 0f
    private var hourHandSize = 0f
    private var handSize = 0f
    private var fontSize = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInit) {
            init()
        }
        drawCircle(canvas)
        drawHands(canvas)
        drawNumerals(canvas)
        drawPoints(canvas)
        postInvalidateDelayed(500);
    }

    private fun init() {
        height = getHeight().toFloat()
        width = getWidth().toFloat()
        padding = 50f
        centreX = width / 2
        centreY = height / 2
        minimum = height.coerceAtMost(width)
        radius = minimum / 2 - padding
        angle = PI / 30 - PI / 2
        paint = Paint()
        path = Path()
        rect = Rect()
        hourHandSize = radius - radius / 2
        handSize = radius - radius / 4
        numbers = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        fontSize = 50f
        isInit = true
    }

    private fun drawCircle(canvas: Canvas) {
        paint?.apply {
            setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 10f)
            canvas.drawCircle(centreX, centreY, radius, paint!!)
        }
        fillCircle(canvas)
    }

    private fun fillCircle(canvas: Canvas) {
        paint?.apply {
            setPaintAttributes(ContextCompat.getColor(context, R.color.gray), Paint.Style.FILL, 0f)
            canvas.drawCircle(centreX, centreY, radius, paint!!)
        }
    }

    private fun setPaintAttributes(colour: Int, setStyle: Paint.Style, setStrokeWidth: Float) {
        paint?.apply {
            reset()
            color = colour
            style = setStyle
            strokeWidth = setStrokeWidth
            isAntiAlias = true
        }
    }

    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
        hour = if (hour > 12) hour - 12 else hour
        minute = calendar.get(Calendar.MINUTE).toFloat()
        second = calendar.get(Calendar.SECOND).toFloat()

        drawHourHand(canvas, (hour + minute / 60) * 5)
        drawMinuteHand(canvas, minute)
        drawSecondsHand(canvas, second)
    }

    private fun drawHourHand(canvas: Canvas, location: Float) {
        paint?.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 15f)
        angle = PI * location / 30 - PI / 2
        canvas.drawLine(
            centreX,
            centreY,
            centreX + cos(angle) * hourHandSize,
            centreY + sin(angle) * hourHandSize,
            paint!!
        )
    }

    private fun drawMinuteHand(canvas: Canvas, location: Float) {
        paint?.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 8f)

        angle = PI * location / 30 - PI / 2
        canvas.drawLine(centreX, centreY,
            centreX + cos(angle) * handSize,
            centreY + sin(angle) * handSize,
            paint!!
        )
    }

    private fun drawSecondsHand(canvas: Canvas, location: Float) {
        paint?.reset()
        setPaintAttributes(Color.RED, Paint.Style.STROKE, 8f)
        angle = PI * location / 30 - PI / 2
        canvas.drawLine(
            centreX,
            centreY,
            centreX + cos(angle) * handSize,
            centreY + sin(angle) * handSize,
            paint!!
        )
    }

    private fun drawNumerals(canvas: Canvas) {
        paint?.reset()
        paint?.textSize = fontSize
        for (number in numbers) {
            val num = number.toString()
            paint?.getTextBounds(num, 0, num.length, rect)
            val angle = PI / 6 * (number - 3)
            val x = centreX + cos(angle) * (radius - NUMBER_SHIFT) - rect?.width()!! / 2
            val y = centreY + sin(angle) * (radius - NUMBER_SHIFT) + rect?.height()!! / 2
            canvas.drawText(num, x, y, paint!!)
        }
    }

    private fun drawPoints(canvas: Canvas) {
        var pointRadius = 6f
        paint?.reset()
        paint?.color = Color.BLACK
        for (i in numbers) {
            val angle = PI / 6 * i
            val x = centreX + cos(angle) * (radius - POINT_SHIFT) - rect?.width()!! / 2 + 24
            val y = centreY + sin(angle) * (radius - POINT_SHIFT) + rect?.height()!! / 2 - 18
            canvas.drawCircle(x, y , pointRadius, paint!!)
        }

        pointRadius = 3f
        for (i in 1..60) {
            val angle = PI / 30 * i
            val x = centreX + cos(angle) * (radius - POINT_SHIFT) - rect?.width()!! / 2 + 24
            val y = centreY + sin(angle) * (radius - POINT_SHIFT) + rect?.height()!! / 2 - 18
            canvas.drawCircle(x, y , pointRadius, paint!!)
        }
    }
}