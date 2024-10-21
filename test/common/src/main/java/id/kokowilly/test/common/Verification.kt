package id.kokowilly.test.common

import org.junit.Assert

infix fun <T : Any?> T.shouldBe(expected: T) {
  Assert.assertEquals(expected, this)
}

fun <T : Any?> T.shouldBe(expected: T, message: () -> String) {
  Assert.assertEquals(message.invoke(), expected, this)
}

infix fun <T : Any?> T.shouldNot(anticipated: T) {
  Assert.assertNotEquals(anticipated, this)
}
