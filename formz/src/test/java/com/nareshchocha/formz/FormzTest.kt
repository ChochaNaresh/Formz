package com.nareshchocha.formz

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FormzTest {
    private class AlwaysValidInput : FormzInput<Int, String>(0, false) {
        override fun validator(value: Int) = ValidationResult.Success
    }

    private class AlwaysInvalidInput : FormzInput<Int, String>(0, false) {
        override fun validator(value: Int) = ValidationResult.Failure("Invalid")
    }

    @Test
    fun validate_returnsTrue_whenAllInputsAreValid() {
        val inputs = listOf(AlwaysValidInput(), AlwaysValidInput())
        assertTrue(Formz.validate(inputs))
    }

    @Test
    fun validate_returnsFalse_whenAnyInputIsInvalid() {
        val inputs = listOf(AlwaysValidInput(), AlwaysInvalidInput())
        assertFalse(Formz.validate(inputs))
    }

    @Test
    fun isPure_returnsTrue_whenAllInputsArePure() {
        val pureInputs =
            listOf(
                object : FormzInput<Int, String>(0, true) {
                    override fun validator(value: Int) = ValidationResult.Success
                },
                object : FormzInput<Int, String>(1, true) {
                    override fun validator(value: Int) = ValidationResult.Success
                }
            )
        assertTrue(Formz.isPure(pureInputs))
    }

    @Test
    fun isPure_returnsFalse_whenAnyInputIsDirty() {
        val pureInput =
            object : FormzInput<Int, String>(0, true) {
                override fun validator(value: Int) = ValidationResult.Success
            }
        val dirtyInput =
            object : FormzInput<Int, String>(1, false) {
                override fun validator(value: Int) = ValidationResult.Success
            }
        assertFalse(Formz.isPure(listOf(pureInput, dirtyInput)))
    }

    @Test
    fun validate_returnsTrue_whenInputsListIsEmpty() {
        val inputs = emptyList<FormzInput<*, *>>()
        assertTrue(Formz.validate(inputs))
    }

    @Test
    fun validate_returnsFalse_whenAllInputsAreInvalid() {
        val inputs =
            listOf(
                object : FormzInput<Int, String>(0, false) {
                    override fun validator(value: Int) = ValidationResult.Failure("Invalid")
                },
                object : FormzInput<Int, String>(1, false) {
                    override fun validator(value: Int) = ValidationResult.Failure("Invalid")
                }
            )
        assertFalse(Formz.validate(inputs))
    }

    @Test
    fun isPure_returnsTrue_whenInputsListIsEmpty() {
        val inputs = emptyList<FormzInput<*, *>>()
        assertTrue(Formz.isPure(inputs))
    }

    @Test
    fun isPure_returnsFalse_whenAllInputsAreDirty() {
        val inputs =
            listOf(
                object : FormzInput<Int, String>(0, false) {
                    override fun validator(value: Int) = ValidationResult.Success
                },
                object : FormzInput<Int, String>(1, false) {
                    override fun validator(value: Int) = ValidationResult.Success
                }
            )
        assertFalse(Formz.isPure(inputs))
    }

    @Test
    fun validate_handlesMixedValidAndInvalidInputs() {
        val inputs =
            listOf(
                object : FormzInput<Int, String>(0, false) {
                    override fun validator(value: Int) = ValidationResult.Success
                },
                object : FormzInput<Int, String>(1, false) {
                    override fun validator(value: Int) = ValidationResult.Failure("Invalid")
                }
            )
        assertFalse(Formz.validate(inputs))
    }

    @Test
    fun isPure_handlesMixedPureAndDirtyInputs() {
        val inputs =
            listOf(
                object : FormzInput<Int, String>(0, true) {
                    override fun validator(value: Int) = ValidationResult.Success
                },
                object : FormzInput<Int, String>(1, false) {
                    override fun validator(value: Int) = ValidationResult.Success
                }
            )
        assertFalse(Formz.isPure(inputs))
    }
}
