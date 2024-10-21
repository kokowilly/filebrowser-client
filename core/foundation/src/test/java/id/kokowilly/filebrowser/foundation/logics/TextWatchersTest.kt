package id.kokowilly.filebrowser.foundation.logics

import id.kokowilly.test.common.shouldBe
import org.junit.Test

class TextWatchersTest {
  @Test
  fun url() {
    Regex(TextWatchers.REGEX_URL)
      .accepts(
        "www.google.com",
        "https://www.google.com",
        "http://www.google.com",
        "http://www.google.com:80",
        "http://secret.google.com/filebrowser",
        "192.168.1.1",
        "192.168.1.1:80",
        "http://192.168.1.1:8080",
        "http://192.168.1.1:8080",
        "http://192.168.1.1:8080/api",
      )
      .rejects(
        "this is not url",
        "http://192.168.1.1:house",
        "invalid",
      )
  }

  @Test
  fun filename() {
    Regex(TextWatchers.REGEX_FILENAME)
      .accepts(
        "normal.jpg",
        "number file 3.jpg",
        "copied (1).doc",
        "multi ext.tar.gz",
        "numbered ext.7z",
        "spaced filename.txt",
        "numbered ext.mp3",
      )
      .rejects(
        "/",
        "/folder",
        ".invisible",
        ".",
        "space extension. jpg",
        "space before ext .jpg",
        " start space.jpg",
        "end space.jpg ",
        "no ext.",
      )
  }

  private fun Regex.accepts(vararg texts: String) = apply {
    texts.forEach {
      matches(it).shouldBe(true) { "comparison failed: $it" }
    }
  }

  private fun Regex.rejects(vararg texts: String) = apply {
    texts.forEach {
      matches(it).shouldBe(false) { "comparison failed: $it" }
    }
  }
}
