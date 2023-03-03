package com.example.watch.view

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
    private var minuteHandSize = 0f
    private var secondsHandSize = 0f
    private var fontSize = 0f
    private var hourHandWidth = 0f
    private var minuteHandWidth = 0f
    private var secondsHandWidth = 0f
    private var circleStrokeWidth = 0f

    private val attributes = context.obtainStyledAttributes(attrs, com.example.watch.R.styleable.WatchView)

    private val strokeColor = attributes.getColor(com.example.watch.R.styleable.WatchView_strokeColor, Color.DKGRAY)
    private val background = attributes.getColor(com.example.watch.R.styleable.WatchView_circleBackground, Color.BLACK)
    private val pointsColor = attributes.getColor(com.example.watch.R.styleable.WatchView_pointsColor, Color.WHITE)
    private val numbersColor = attributes.getColor(com.example.watch.R.styleable.WatchView_numbersColor, ContextCompat.getColor(context, R.color.light_blue))
    private val hourHandColor = attributes.getColor(com.example.watch.R.styleable.WatchView_hourHandColor, Color.WHITE)
    private val minuteHandColor = attributes.getColor(com.example.watch.R.styleable.WatchView_minuteHandColor, Color.WHITE)
    private val secondsHandColor = attributes.getColor(com.example.watch.R.styleable.WatchView_secondsHandColor, Color.WHITE)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInit) {
            init()
        }
        drawCircle(canvas)
        drawHands(canvas)
        drawNumerals(canvas)
        drawPoints(canvas)
        drawCentrePoint(canvas)
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
        minuteHandSize = radius - radius / 4
        secondsHandSize = radius - radius / 8
        numbers = arrayOf(3, 6, 9, 12)
        fontSize = radius / 6
        hourHandWidth = radius / 24
        minuteHandWidth = radius / 31
        secondsHandWidth = radius / 48
        circleStrokeWidth = radius / 8
        isInit = true
    }

    private fun drawCircle(canvas: Canvas) {
        paint?.apply {
            setPaintAttributes(strokeColor, Paint.Style.STROKE, circleStrokeWidth)
            canvas.drawCircle(centreX, centreY, radius, paint!!)
        }
        fillCircle(canvas)
    }

    private fun fillCircle(canvas: Canvas) {
        paint?.apply {
            setPaintAttributes(background, Paint.Style.FILL, 0f)
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
        val handShift = radius / 12.5f
        paint?.reset()
        setPaintAttributes(hourHandColor, Paint.Style.STROKE, hourHandWidth)
        angle = PI * location / 30 - PI / 2
        canvas.drawLine(
            centreX + cos(angle) * handShift,
            centreY + sin(angle) * handShift,
            centreX + cos(angle) * hourHandSize,
            centreY + sin(angle) * hourHandSize,
            paint!!
        )
    }

    private fun drawMinuteHand(canvas: Canvas, location: Float) {
        val handShift = radius / 12.5f
        paint?.reset()
        setPaintAttributes(minuteHandColor, Paint.Style.STROKE, minuteHandWidth)
        angle = PI * location / 30 - PI / 2
        canvas.drawLine(
            centreX + cos(angle) * handShift,
            centreY + sin(angle) * handShift,
            centreX + cos(angle) * minuteHandSize,
            centreY + sin(angle) * minuteHandSize,
            paint!!
        )
    }

    private fun drawSecondsHand(canvas: Canvas, location: Float) {
        val handShift = radius / 12.5f
        paint?.reset()
        setPaintAttributes(secondsHandColor, Paint.Style.STROKE, secondsHandWidth)
        angle = PI * location / 30 - PI / 2
        canvas.drawLine(
            centreX + cos(angle) * handShift,
            centreY + sin(angle) * handShift,
            centreX + cos(angle) * secondsHandSize,
            centreY + sin(angle) * secondsHandSize,
            paint!!
        )
    }

    private fun drawNumerals(canvas: Canvas) {
        paint?.reset()
        paint?.textSize = fontSize
        paint?.color = numbersColor
        val numberShift = radius / 4
        var angle = 0f
        for (number in numbers) {
            val num = number.toString()
            paint?.getTextBounds(num, 0, num.length, rect)
            val x = centreX + cos(angle) * (radius - numberShift) - rect?.width()!! / 2
            val y = centreY + sin(angle) * (radius - numberShift) + rect?.height()!! / 2
            angle += PI / 2
            canvas.drawText(num, x, y, paint!!)
        }
    }

    private fun drawPoints(canvas: Canvas) {
        var pointShift = radius / 8
        setPaintAttributes(pointsColor, Paint.Style.STROKE, radius / 120)
        for (i in 1..12) {
            val angle = PI / 6 * i
            val x = centreX + cos(angle) * (radius - pointShift)
            val y = centreY + sin(angle) * (radius - pointShift)
            canvas.drawLine(
                x,
                y,
                x + cos(angle) * fontSize / 3,
                y + sin(angle) * fontSize / 3,
                paint!!)
        }

        pointShift = radius / 12
        for (i in 1..59) {
            val angle = PI / 30 * i
            val x = centreX + cos(angle) * (radius - pointShift)
            val y = centreY + sin(angle) * (radius - pointShift)
            canvas.drawLine(
                x,
                y,
                x + cos(angle) * fontSize / 6,
                y + sin(angle) * fontSize / 6,
                paint!!)
        }
    }

    private fun drawCentrePoint(canvas: Canvas) {
        val pointRadius = radius / 32
        paint?.reset()
        setPaintAttributes(pointsColor, Paint.Style.STROKE, radius / 30)
        canvas.drawCircle(centreX, centreY, pointRadius, paint!!)
    }
}