package com.simonsickle.composablebarcode.ui.canvas

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType

/**
 * Syntactic sugar to create a new path to be used in Compose drawing with a default fill type
 * of [PathFillType.EvenOdd] to match the native [Android Path][android.graphics.Path]
 */
fun newPath(withPath: Path.() -> Unit) = Path().apply {
  fillType = PathFillType.EvenOdd
  withPath(this)
}