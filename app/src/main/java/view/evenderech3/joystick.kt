package view.evenderech3

import kotlin.math.abs
import kotlin.math.min
import android.content.Context
import android.graphics.PointF
import android.view.View
import android.util.AttributeSet
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint


class joystick @JvmOverloads constructor(
    context: Context, attributes: AttributeSet? = null,
    styleAttributes: Int = 0
) : View(context, attributes, styleAttributes) {
    private val colorKnob = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL_AND_STROKE
    }
    private val colorJoystick = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
    }
    //initialize radius of joystick knob and outer circle
    private var knobSize: Float = 0f
    private var joystickSize: Float = 0f
    //initialize center point of joystick knob and outer circle
    private var knobCenter: PointF = PointF()
    private var joystickCenter: PointF = PointF()

    //size and positioning of joystick knob and outer circle
    override fun onSizeChanged(newWidth: Int, newHeight: Int, prevWidth: Int, prevHeight: Int) {
        val min = min(newWidth, newHeight)
        knobSize = 0.15f * min
        joystickSize = 0.45f * min
        val centerHeight = height / 2.0f;
        val centerWidth = width / 2.0f
        knobCenter = PointF(centerWidth, centerHeight)
        joystickCenter = PointF(centerWidth, centerHeight)
    }

    //draws the joystick--first outer circle and then knob
    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(joystickCenter.x, joystickCenter.y, joystickSize, colorJoystick)
        canvas.drawCircle(knobCenter.x, knobCenter.y, knobSize, colorKnob)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> releaseKnob(event.x, event.y)
            MotionEvent.ACTION_MOVE -> dragKnob(event.x, event.y)
        }
        return true
    }

    //when let go of knob, put it back in original center position
    private fun releaseKnob(x: Float, y: Float) {
        knobCenter.x = joystickCenter.x;
        knobCenter.y = joystickCenter.y;
        invalidate()
    }

    //movement of joystick knob
    private fun dragKnob(x: Float, y: Float) {
        val distX = x - joystickCenter.x
        val distY = y - joystickCenter.y
        val distanceX = abs(distX)
        val distanceY = abs(distY)
        val maxToMove = joystickSize - knobSize
        //checks knob isn't going out of bounds
        if (maxToMove >= distanceX && maxToMove >= distanceY) {
            knobCenter.x = x
            knobCenter.y = y
        }
        invalidate()
    }

}