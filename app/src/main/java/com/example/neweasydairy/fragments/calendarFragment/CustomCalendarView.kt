package com.example.neweasydairy.fragments.calendarFragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.CalendarView

class CustomCalendarView(context: Context, attrs: AttributeSet?) : CalendarView(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED // Dot color
        style = Paint.Style.FILL
    }

    private var markedDates: HashSet<Long> = HashSet()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        for (date in markedDates) {
            val x = width / 2
            val y = height - 10
            canvas.drawCircle(x, y, 10f, paint)
        }
    }
}
