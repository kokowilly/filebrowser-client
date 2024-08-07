package id.kokowilly.filebrowser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.kokowilly.filebrowser.connection.collection.NetworkConfigActivity

class EntryPointActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startActivity(
      Intent(this, NetworkConfigActivity::class.java)
    )
    finish()
  }
}
