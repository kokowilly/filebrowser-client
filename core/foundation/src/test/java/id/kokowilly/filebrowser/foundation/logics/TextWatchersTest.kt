package id.kokowilly.filebrowser.foundation.logics

import id.kokowilly.test.common.shouldBe
import org.junit.Test

class TextWatchersTest {
  @Test
  fun url() {
    val regex = Regex(TextWatchers.REGEX_URL)

    regex.matches("www.google.com") shouldBe true
    regex.matches("https://www.google.com") shouldBe true
    regex.matches("http://www.google.com") shouldBe true
    regex.matches("http://www.google.com:80") shouldBe true
    regex.matches("http://secret.google.com/filebrowser") shouldBe true
    regex.matches("192.168.1.1") shouldBe true
    regex.matches("192.168.1.1:80") shouldBe true
    regex.matches("http://192.168.1.1:8080") shouldBe true
    regex.matches("http://192.168.1.1:8080") shouldBe true
    regex.matches("http://192.168.1.1:8080/api") shouldBe true

    regex.matches("this is not url") shouldBe false
    regex.matches("http://192.168.1.1:house") shouldBe false
    regex.matches("invalid") shouldBe false
  }
}