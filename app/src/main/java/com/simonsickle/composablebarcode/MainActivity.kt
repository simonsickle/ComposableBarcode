package com.simonsickle.composablebarcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.simonsickle.composablebarcode.qr.QrCode
import com.simonsickle.composablebarcode.qr.QrCodeProperties
import com.simonsickle.composablebarcode.ui.theme.ComposableBarcodeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ComposableBarcodeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          MyWebsiteQR()
        }
      }
    }
  }
}

@Composable
private fun MyWebsiteQR() {
  QrCode(
    modifier = Modifier.fillMaxSize(),
    contents = "https://github.com/simonsickle",
    qrCodeProperties = QrCodeProperties(
      foreground = MaterialTheme.colors.onSurface,
      background = MaterialTheme.colors.surface
    )
  )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
  ComposableBarcodeTheme {
    MyWebsiteQR()
  }
}