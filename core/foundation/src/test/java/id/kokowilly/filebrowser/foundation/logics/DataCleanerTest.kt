package id.kokowilly.filebrowser.foundation.logics

import id.kokowilly.test.common.shouldBe
import org.junit.Test

class DataCleanerTest {
  @Test
  fun cleanPath() {
    listOf(
      "/hello/world" to "/hello/world",
      "//" to "/",
      "hello/world" to "/hello/world",
    ).forEach { (original, expected) ->
      original.cleanPath() shouldBe expected
    }
  }
}
