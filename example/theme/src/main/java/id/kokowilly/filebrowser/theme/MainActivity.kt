package id.kokowilly.filebrowser.theme

import android.os.Bundle
import id.kokowilly.filebrowser.foundation.style.ImmersiveActivity

class MainActivity : ImmersiveActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
