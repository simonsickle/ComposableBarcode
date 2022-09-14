package com.simonsickle.composablebarcode.qr

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.simonsickle.composablebarcode.ui.canvas.newPath

const val FINDER_PATTERN_ROW_COUNT = 7
private const val INTERIOR_EXTERIOR_SHAPE_RATIO = 3f / FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_EXTERIOR_OFFSET_RATIO = 2f / FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_EXTERIOR_SHAPE_CORNER_RADIUS = 0.12f
private const val INTERIOR_BACKGROUND_EXTERIOR_SHAPE_RATIO = 5f / FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_BACKGROUND_EXTERIOR_OFFSET_RATIO = 1f / FINDER_PATTERN_ROW_COUNT
private const val INTERIOR_BACKGROUND_EXTERIOR_SHAPE_CORNER_RADIUS = 0.5f

/**
 *  A valid QR code has three finder patterns (top left, top right, bottom left).
 *
 *  @param qrCodeProperties how the QR code is drawn
 *  @param sideLength length, in pixels, of each side of the QR code
 *  @param finderPatternSize [Size] of each finder patten, based on the QR code spec
 */
internal fun DrawScope.drawQrCodeFinders(
  qrCodeProperties: QrCodeProperties,
  sideLength: Float,
  finderPatternSize: Size,
) {

  setOf(
    // Draw top left finder pattern.
    Offset(x = QR_MARGIN_PX, y = QR_MARGIN_PX),
    // Draw top right finder pattern.
    Offset(x = sideLength - (QR_MARGIN_PX + finderPatternSize.width), y = QR_MARGIN_PX),
    // Draw bottom finder pattern.
    Offset(x = QR_MARGIN_PX, y = sideLength - (QR_MARGIN_PX + finderPatternSize.height))
  ).forEach { offset ->
    drawQrCodeFinder(
      qrCodeProperties = qrCodeProperties,
      topLeft = offset,
      finderPatternSize = finderPatternSize,
      cornerRadius = CornerRadius.Zero,
    )
  }
}

/**
 * This func is responsible for drawing a single finder pattern, for a QR code
 */
private fun DrawScope.drawQrCodeFinder(
  qrCodeProperties: QrCodeProperties,
  topLeft: Offset,
  finderPatternSize: Size,
  cornerRadius: CornerRadius,

  ) {
  drawPath(
    color = qrCodeProperties.foreground,
    path = newPath {
      // Draw the outer rectangle for the finder pattern.
      addRoundRect(
        roundRect = RoundRect(
          rect = Rect(
            offset = topLeft,
            size = finderPatternSize
          ),
          cornerRadius = cornerRadius
        )
      )

      // Draw background for the finder pattern interior (this keeps the arc ratio consistent).
      val innerBackgroundOffset = Offset(
        x = finderPatternSize.width * INTERIOR_BACKGROUND_EXTERIOR_OFFSET_RATIO,
        y = finderPatternSize.height * INTERIOR_BACKGROUND_EXTERIOR_OFFSET_RATIO
      )
      addRoundRect(
        roundRect = RoundRect(
          rect = Rect(
            offset = topLeft + innerBackgroundOffset,
            size = finderPatternSize * INTERIOR_BACKGROUND_EXTERIOR_SHAPE_RATIO
          ),
          cornerRadius = cornerRadius * INTERIOR_BACKGROUND_EXTERIOR_SHAPE_CORNER_RADIUS
        )
      )

      // Draw the inner rectangle for the finder pattern.
      val innerRectOffset = Offset(
        x = finderPatternSize.width * INTERIOR_EXTERIOR_OFFSET_RATIO,
        y = finderPatternSize.height * INTERIOR_EXTERIOR_OFFSET_RATIO
      )
      addRoundRect(
        roundRect = RoundRect(
          rect = Rect(
            offset = topLeft + innerRectOffset,
            size = finderPatternSize * INTERIOR_EXTERIOR_SHAPE_RATIO
          ),
          cornerRadius = cornerRadius * INTERIOR_EXTERIOR_SHAPE_CORNER_RADIUS
        )
      )
    }
  )
}