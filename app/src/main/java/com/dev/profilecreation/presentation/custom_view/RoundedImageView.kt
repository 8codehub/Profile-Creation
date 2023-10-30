package com.dev.profilecreation.presentation.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.dev.profilecreation.R

class RoundedImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    private val path = Path()
    private var cornerRadius = 0f

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)
        cornerRadius = attributes.getDimension(R.styleable.RoundedImageView_cornerRadius, 0f)
        attributes.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}
