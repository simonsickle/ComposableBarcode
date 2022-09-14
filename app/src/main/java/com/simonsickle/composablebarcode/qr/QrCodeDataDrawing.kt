package com.simonsickle.composablebarcode.qr

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.zxing.qrcode.encoder.ByteMatrix
import com.simonsickle.composablebarcode.ui.canvas.newPath

private typealias Coordinate = Pair<Int, Int>

/**
 * Responsible for rendering all data bits in a QR code data matrix region. The data matrix is the area
 * of the QR code that contains the encoded information, redundancy and error correction information.
 *
 * @param bytes generated matrix representing the QR code\
 * @param size canvas size to draw data bits
 * @param qrCodeProperties defining information for the QR code
 */
fun DrawScope.drawAllQrCodeDataBits(
  qrCodeProperties: QrCodeProperties,
  bytes: ByteMatrix,
  size: Size
) {
  setOf(
    // data bits between top left finder pattern and top right finder pattern.
    Pair(
      first = Coordinate(first = FINDER_PATTERN_ROW_COUNT, second = 0),
      second = Coordinate(
        first = (bytes.width - FINDER_PATTERN_ROW_COUNT),
        second = FINDER_PATTERN_ROW_COUNT
      )
    ),
    // data bits below top left finder pattern and above bottom left finder pattern.
    Pair(
      first = Coordinate(first = 0, second = FINDER_PATTERN_ROW_COUNT),
      second = Coordinate(
        first = bytes.width,
        second = bytes.height - FINDER_PATTERN_ROW_COUNT
      )
    ),
    // data bits to the right of the bottom left finder pattern.
    Pair(
      first = Coordinate(
        first = FINDER_PATTERN_ROW_COUNT,
        second = (bytes.height - FINDER_PATTERN_ROW_COUNT)
      ),
      second = Coordinate(
        first = bytes.width,
        second = bytes.height
      )
    )
  ).forEach { section ->
    for (y in section.first.second until section.second.second) {
      for (x in section.first.first until section.second.first) {
        if (bytes[x, y] == 1.toByte()) {
          drawPath(
            color = qrCodeProperties.foreground,
            path = newPath {
              addRect(
                rect = Rect(
                  offset = Offset(
                    x = QR_MARGIN_PX + x * size.width,
                    y = QR_MARGIN_PX + y * size.height
                  ),
                  size = size
                )
              )
            }
          )
        }
      }
    }
  }
}