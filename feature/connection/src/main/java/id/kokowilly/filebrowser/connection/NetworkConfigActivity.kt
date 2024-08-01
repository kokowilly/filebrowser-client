package id.kokowilly.filebrowser.connection

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import id.kokowilly.filebrowser.style.ImmersiveActivity

class NetworkConfigActivity : ImmersiveActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_network_config)
  }
}
