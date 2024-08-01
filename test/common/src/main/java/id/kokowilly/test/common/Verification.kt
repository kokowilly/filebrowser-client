package id.kokowilly.test.common

import org.junit.Assert

infix fun <T : Any?> T.shouldBe(expected: T) {
    Assert.assertEquals(expected, this)
}

infix fun <T : Any?> T.shouldNot(anticipated: T) {
    Assert.assertNotEquals(anticipated, this)
}
