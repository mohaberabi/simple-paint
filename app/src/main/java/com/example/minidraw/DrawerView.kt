package com.example.minidraw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.io.path.Path
import kotlin.math.abs


private const val STROKE_WIDTH = 12f

class DrawerView(context: Context) : View(context) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private lateinit var frame: Rect

    // returns the distance in pixels before system thinks users is scrolling
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private val drawColor = ResourcesCompat.getColor(
        resources,
        R.color.colorPaint,
        null
    )

    private val paint = Paint(

    ).apply {

        // color which is used to draw with
        color = drawColor
        // wre to apply edge smoothing
        isAntiAlias = true
        // how color is down simpeld , reducing color of the device

        isDither = true
        // using the stroke not filling only outside
        style = Paint.Style.STROKE
        // how lines joins the path , as sharp or rounded
        strokeJoin = Paint.Join.ROUND

        strokeCap = Paint.Cap.ROUND
        // width of the stroke in pixels
        strokeWidth = STROKE_WIDTH
    }
    private val backgroundColor = ResourcesCompat.getColor(
        resources,
        R.color.colorBackground,
        null
    )

    private val path = Path()
    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::extraBitmap.isInitialized) {
            extraBitmap.recycle()
        }
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        val inset = 40
        frame = Rect(
            inset,
            inset,
            w - inset,
            h - inset
        )
    }


    // called anytime  users press or touches the screen
    //which has some callbacks
    //1-touchStart()->when user starts touching
    //2-touchMove()->when user starts touching and not releasing the touches
    //3-touchUp()->when user releases current touch event

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event == null) {
            return true
        }
        motionTouchEventX = event.x
        motionTouchEventY = event.y
        when (event.action) {

            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()

        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(
            extraBitmap,
            0f,
            0f,
            null
        )
        canvas.drawRect(frame, paint)
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY

    }

    //user moves touching
    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }


    // user releases touches
    private fun touchUp() {
        path.reset()
    }
}