package com.nareshchocha.formz

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FormzInputTest {
    private class NonEmptyStringInput(
        value: String,
        isPure: Boolean = true
    ) : FormzInput<String, String>(value, isPure) {
        override fun validator(value: String): ValidationResult<String> =
            if (value.isNotEmpty()) ValidationResult.Success else ValidationResult.Failure("Empty")
    }

    @Test
    fun isValid_returnsTrue_whenInputIsPure() {
        val input = NonEmptyStringInput("", isPure = true)
        assertTrue(input.isValid())
    }

    @Test
    fun isValid_returnsTrue_whenInputIsValidAndDirty() {
        val input = NonEmptyStringInput("abc", isPure = false)
        assertTrue(input.isValid())
    }

    @Test
    fun isValid_returnsFalse_whenInputIsInvalidAndDirty() {
        val input = NonEmptyStringInput("", isPure = false)
        assertFalse(input.isValid())
    }

    @Test
    fun error_returnsNull_whenInputIsValid() {
        val input = NonEmptyStringInput("abc", isPure = false)
        assertNull(input.error())
    }

    @Test
    fun error_returnsFailure_whenInputIsInvalid() {
        val input = NonEmptyStringInput("", isPure = false)
        val error = input.error()
        assertNotNull(error)
        assertEquals("Empty", error?.error)
    }

    @Test
    fun displayError_returnsNull_whenInputIsPure() {
        val input = NonEmptyStringInput("", isPure = true)
        assertNull(input.displayError())
    }

    @Test
    fun displayError_returnsError_whenInputIsInvalidAndDirty() {
        val input = NonEmptyStringInput("", isPure = false)
        assertEquals("Empty", input.displayError())
    }

    @Test
    fun displayError_returnsNull_whenInputIsValidAndDirty() {
        val input = NonEmptyStringInput("abc", isPure = false)
        assertNull(input.displayError())
    }

    @Test
    fun equals_and_hashCode_workCorrectly() {
        val input1 = NonEmptyStringInput("abc", isPure = false)
        val input2 = NonEmptyStringInput("abc", isPure = false)
        val input3 = NonEmptyStringInput("def", isPure = false)
        assertEquals(input1, input2)
        assertEquals(input1.hashCode(), input2.hashCode())
        assertNotEquals(input1, input3)
    }

    @Test
    fun isValid_returnsFalse_whenInputIsNull() {
        val input =
            object : FormzInput<String?, String>(null, isPure = false) {
                override fun validator(value: String?): ValidationResult<String> =
                    if (value.isNullOrEmpty()) ValidationResult.Failure("Null or Empty") else ValidationResult.Success
            }
        assertFalse(input.isValid())
    }

    @Test
    fun error_returnsFailure_whenInputIsNull() {
        val input =
            object : FormzInput<String?, String>(null, isPure = false) {
                override fun validator(value: String?): ValidationResult<String> =
                    if (value.isNullOrEmpty()) ValidationResult.Failure("Null or Empty") else ValidationResult.Success
            }
        val error = input.error()
        assertNotNull(error)
        assertEquals("Null or Empty", error?.error)
    }

    @Test
    fun displayError_returnsError_whenInputIsNullAndDirty() {
        val input =
            object : FormzInput<String?, String>(null, isPure = false) {
                override fun validator(value: String?): ValidationResult<String> =
                    if (value.isNullOrEmpty()) ValidationResult.Failure("Null or Empty") else ValidationResult.Success
            }
        assertEquals("Null or Empty", input.displayError())
    }

    @Test
    fun equals_and_hashCode_workCorrectly_withNullValues() {
        val input1 =
            object : FormzInput<String?, String>(null, isPure = false) {
                override fun validator(value: String?): ValidationResult<String> =
                    if (value.isNullOrEmpty()) ValidationResult.Failure("Null or Empty") else ValidationResult.Success
            }
        val input2 =
            object : FormzInput<String?, String>(null, isPure = false) {
                override fun validator(value: String?): ValidationResult<String> =
                    if (value.isNullOrEmpty()) ValidationResult.Failure("Null or Empty") else ValidationResult.Success
            }
        assertEquals(input1, input2)
        assertEquals(input1.hashCode(), input2.hashCode())
    }

    @Test
    fun isValid_returnsTrue_whenInputIsWhitespaceAndConsideredValid() {
        val input =
            object : FormzInput<String, String>("   ", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.isBlank()) ValidationResult.Success else ValidationResult.Failure("Not Blank")
            }
        assertTrue(input.isValid())
    }

    @Test
    fun error_returnsFailure_whenInputIsWhitespaceAndInvalid() {
        val input =
            object : FormzInput<String, String>("   ", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.isBlank()) ValidationResult.Failure("Blank Input") else ValidationResult.Success
            }
        val error = input.error()
        assertNotNull(error)
        assertEquals("Blank Input", error?.error)
    }

    @Test
    fun displayError_returnsError_whenInputIsWhitespaceAndDirty() {
        val input =
            object : FormzInput<String, String>("   ", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.isBlank()) ValidationResult.Failure("Blank Input") else ValidationResult.Success
            }
        assertEquals("Blank Input", input.displayError())
    }

    @Test
    fun isValid_returnsFalse_whenInputIsEmptyString() {
        val input =
            object : FormzInput<String, String>("", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.isEmpty()) ValidationResult.Failure("Empty String") else ValidationResult.Success
            }
        assertFalse(input.isValid())
    }

    @Test
    fun equals_and_hashCode_workCorrectly_withWhitespaceValues() {
        val input1 =
            object : FormzInput<String, String>("   ", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.isBlank()) ValidationResult.Success else ValidationResult.Failure("Not Blank")
            }
        val input2 =
            object : FormzInput<String, String>("   ", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.isBlank()) ValidationResult.Success else ValidationResult.Failure("Not Blank")
            }
        assertEquals(input1, input2)
        assertEquals(input1.hashCode(), input2.hashCode())
    }

    @Test
    fun isValid_returnsFalse_whenInputExceedsMaxLength() {
        val input =
            object : FormzInput<String, String>("a".repeat(101), isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.length > 100) ValidationResult.Failure("Exceeds Max Length") else ValidationResult.Success
            }
        assertFalse(input.isValid())
    }

    @Test
    fun error_returnsFailure_whenInputExceedsMaxLength() {
        val input =
            object : FormzInput<String, String>("a".repeat(101), isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.length > 100) ValidationResult.Failure("Exceeds Max Length") else ValidationResult.Success
            }
        val error = input.error()
        assertNotNull(error)
        assertEquals("Exceeds Max Length", error?.error)
    }

    @Test
    fun isValid_returnsFalse_whenInputIsNegativeNumber() {
        val input =
            object : FormzInput<Int, String>(-1, isPure = false) {
                override fun validator(value: Int): ValidationResult<String> =
                    if (value < 0) ValidationResult.Failure("Negative Number") else ValidationResult.Success
            }
        assertFalse(input.isValid())
    }

    @Test
    fun error_returnsFailure_whenInputIsNegativeNumber() {
        val input =
            object : FormzInput<Int, String>(-1, isPure = false) {
                override fun validator(value: Int): ValidationResult<String> =
                    if (value < 0) ValidationResult.Failure("Negative Number") else ValidationResult.Success
            }
        val error = input.error()
        assertNotNull(error)
        assertEquals("Negative Number", error?.error)
    }

    @Test
    fun isValid_returnsTrue_whenInputIsBoundaryValue() {
        val input =
            object : FormzInput<Int, String>(0, isPure = false) {
                override fun validator(value: Int): ValidationResult<String> =
                    if (value >= 0) ValidationResult.Success else ValidationResult.Failure("Invalid Boundary")
            }
        assertTrue(input.isValid())
    }

    @Test
    fun isValid_returnsFalse_whenInputIsSpecialCharacters() {
        val input =
            object : FormzInput<String, String>("@#$%", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.any { it.isLetterOrDigit() }) {
                        ValidationResult.Success
                    } else {
                        ValidationResult.Failure(
                            "Special Characters"
                        )
                    }
            }
        assertFalse(input.isValid())
    }

    @Test
    fun error_returnsFailure_whenInputIsSpecialCharacters() {
        val input =
            object : FormzInput<String, String>("@#$%", isPure = false) {
                override fun validator(value: String): ValidationResult<String> =
                    if (value.any { it.isLetterOrDigit() }) {
                        ValidationResult.Success
                    } else {
                        ValidationResult.Failure(
                            "Special Characters"
                        )
                    }
            }
        val error = input.error()
        assertNotNull(error)
        assertEquals("Special Characters", error?.error)
    }
}
