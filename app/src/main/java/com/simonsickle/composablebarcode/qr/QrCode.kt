package com.simonsickle.composablebarcode.qr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.google.zxing.qrcode.encoder.QRCode

@Composable
fun QrCode(
  contents: String,
  modifier: Modifier = Modifier,
  qrCodeProperties: QrCodeProperties = QrCodeProperties(
    foreground = if (isSystemInDarkTheme()) Color.White else Color.Black,
    background = if (isSystemInDarkTheme()) Color.Black else Color.White
  )
) {
  // QR Spec doesn't allow for empty data
  require(contents.isNotEmpty()) { "QR Code must have non empty contents" }

  // Generate the QR code and remember it unless the encoded contents change to prevent
  // unnecessary work on recomposition. If the data inside the QR code changes, then the
  // matrix that defines the code will be recreated as it is used as the remember key.
  val qrCode = remember(contents) {
    createQrCode(contents = contents)
  }

  // Box contains the QR, making it's surroundings a square
  Box(
    modifier = modifier
      .defaultMinSize(48.dp, 48.dp)
      .aspectRatio(1f)
      .background(qrCodeProperties.background)
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      // Calculate the height and width of each column/row
      val rowHeight = (size.width - QR_MARGIN_PX * 2f) / qrCode.matrix.height
      val columnWidth = (size.width - QR_MARGIN_PX * 2f) / qrCode.matrix.width

      // Draw all of the finder patterns required by the QR spec. Calculate the ratio
      // of the number of rows/columns to the width and height
      drawQrCodeFinders(
        qrCodeProperties = qrCodeProperties,
        sideLength = size.width,
        finderPatternSize = Size(
          width = columnWidth * FINDER_PATTERN_ROW_COUNT,
          height = rowHeight * FINDER_PATTERN_ROW_COUNT
        )
      )

      // Draw data bits (encoded data part)
      drawAllQrCodeDataBits(
        qrCodeProperties = qrCodeProperties,
        bytes = qrCode.matrix,
        size = Size(
          width = columnWidth,
          height = rowHeight
        )
      )
    }
  }
}

/**
 * Generate and return a QRCode with a bit matrix to be drawn on your canvas
 * @param contents the UTF-8, non-empty string representation of your data
 */
private fun createQrCode(contents: String): QRCode {
  // Verify that the contents aren't empty as no code can be generated
  require(contents.isNotEmpty())

  return Encoder.encode(
    contents,
    QR_ERROR_CORRECTION_LEVEL,
    mapOf(
      EncodeHintType.CHARACTER_SET to "UTF-8",
      EncodeHintType.MARGIN to QR_MARGIN_PX,
      EncodeHintType.ERROR_CORRECTION to QR_ERROR_CORRECTION_LEVEL
    )
  )
}

/**
 * Define the way a QR code is drawn onto the canvas
 */
data class QrCodeProperties(
  val foreground: Color,
  val background: Color
)

/**
 * This is a "quiet zone" outside of the QR code to allow successful scanning to occur
 */
const val QR_MARGIN_PX = 16f

/**
 * QR codes have error correction built in - this is what allows a logo to be drawn over the center. As
 * the level increases, the QR code bits get more dense. The level below was chosen as it had a good
 * scannability ratio (with logo) without making the QR code too dense.
 *
 * The levels follow:
 * [Low][ErrorCorrectionLevel.L] allows ~7% restoration
 * [Medium][ErrorCorrectionLevel.M] allows ~15% restoration
 * [Quartile][ErrorCorrectionLevel.Q] allows ~25% restoration
 * [High][ErrorCorrectionLevel.H] allows ~30% restoration
 */
val QR_ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.Q
