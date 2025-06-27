package com.nareshchocha.formz

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FormzInterfaceTest {
    private class DummyInput(
        value: String,
        isPure: Boolean
    ) : FormzInput<String, String>(value, isPure) {
        override fun validator(value: String): ValidationResult<String> =
            if (value == "ok") ValidationResult.Success else ValidationResult.Failure("fail")
    }

    private class DummyForm(
        val input1: DummyInput,
        val input2: DummyInput
    ) : FormzInterface {
        override val inputs = listOf(input1, input2)
    }

    @Test
    fun isValid_returnsTrue_whenAllInputsAreValid() {
        val form = DummyForm(DummyInput("ok", false), DummyInput("ok", false))
        assertTrue(form.isValid)
        assertFalse(form.isNotValid)
    }

    @Test
    fun isValid_returnsFalse_whenAnyInputIsInvalid() {
        val form = DummyForm(DummyInput("ok", false), DummyInput("bad", false))
        assertFalse(form.isValid)
        assertTrue(form.isNotValid)
    }

    @Test
    fun isPure_returnsTrue_whenAllInputsArePure() {
        val form = DummyForm(DummyInput("ok", true), DummyInput("ok", true))
        assertTrue(form.isPure)
        assertFalse(form.isDirty)
    }

    @Test
    fun isPure_returnsFalse_whenAnyInputIsDirty() {
        val form = DummyForm(DummyInput("ok", true), DummyInput("ok", false))
        assertFalse(form.isPure)
        assertTrue(form.isDirty)
    }

    @Test
    fun isValid_returnsTrue_whenInputsListIsEmpty() {
        val form =
            object : FormzInterface {
                override val inputs = emptyList<FormzInput<*, *>>()
            }
        assertTrue(form.isValid)
        assertFalse(form.isNotValid)
    }

    @Test
    fun isPure_returnsTrue_whenInputsListIsEmpty() {
        val form =
            object : FormzInterface {
                override val inputs = emptyList<FormzInput<*, *>>()
            }
        assertTrue(form.isPure)
        assertFalse(form.isDirty)
    }

    @Test
    fun isValid_and_isPure_workCorrectly_withMixedInputs() {
        val validPure =
            object : FormzInput<Int, String>(1, true) {
                override fun validator(value: Int) = ValidationResult.Success
            }
        val invalidDirty =
            object : FormzInput<Int, String>(0, false) {
                override fun validator(value: Int) = ValidationResult.Failure("fail")
            }
        val form =
            object : FormzInterface {
                override val inputs = listOf(validPure, invalidDirty)
            }
        assertFalse(form.isValid)
        assertTrue(form.isNotValid)
        assertFalse(form.isPure)
        assertTrue(form.isDirty)
    }

    @Test
    fun isValid_and_isPure_workCorrectly_withDuplicateInputs() {
        val input =
            object : FormzInput<Int, String>(1, true) {
                override fun validator(value: Int) = ValidationResult.Success
            }
        val form =
            object : FormzInterface {
                override val inputs = listOf(input, input)
            }
        assertTrue(form.isValid)
        assertTrue(form.isPure)
    }
}
