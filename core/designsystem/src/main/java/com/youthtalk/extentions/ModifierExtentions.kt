package com.youthtalk.extentions

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur.NORMAL
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.gray30

fun Modifier.shadow(color: Color = gray30, offsetX: Dp = 0.dp, offsetY: Dp = 0.dp, blurRadius: Dp = 0.dp) = then(
    this.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = -offsetX.toPx()
            val topPixel = -offsetY.toPx()
            val rightPixel = size.width + offsetX.toPx()
            val bottomPixel = size.height + offsetY.toPx()

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    },
)
