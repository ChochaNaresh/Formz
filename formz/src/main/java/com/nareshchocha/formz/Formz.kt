package com.nareshchocha.formz

import java.util.Objects

/**
 * Represents the result of a validation.
 */
sealed class ValidationResult<out E> {
    data object Success : ValidationResult<Nothing>()
    data class Failure<E>(val error: E) : ValidationResult<E>()
}

/**
 * Abstract class representing a form input that can be validated.
 *
 * @param T The type of the input's value.
 * @param E The type of the validation error.
 */
abstract class FormzInput<T, E>(val value: T, val isPure: Boolean) {

    // Cache the validation result. Since 'value' is immutable, this is safe.
    private val validationResult: ValidationResult<E> by lazy { validator(value) }

    /**
     * Validates the given [value] and returns a [ValidationResult].
     *
     * @param value The value to validate.
     * @return [ValidationResult.Success] if valid; otherwise, [ValidationResult.Failure].
     */
    abstract fun validator(value: T): ValidationResult<E>

    /**
     * Returns `true` if the cached validation result is [ValidationResult.Success],
     * indicating that the input value is valid.
     */
    fun isValid(): Boolean = validationResult == ValidationResult.Success

    /**
     * Returns a [ValidationResult.Failure] if validation fails, or `null` if it succeeds.
     */
    fun error(): ValidationResult.Failure<E>? {
        return when (validationResult) {
            is ValidationResult.Failure -> validationResult as ValidationResult.Failure<E>
            ValidationResult.Success -> null
        }
    }

    /**
     * Returns the error to display.
     *
     * If the input is still pure (unmodified), no error is displayed.
     */
    fun displayError(): ValidationResult.Failure<E>? =
        if (isPure) null else error()

    override fun hashCode(): Int {
        return Objects.hash(value, isPure)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FormzInput<*, *>) return false

        if (value != other.value) return false
        if (isPure != other.isPure) return false

        return true
    }

    override fun toString(): String {
        return "FormzInput(value=$value, isPure=$isPure, isValid=${isValid()}, error=${error()})"
    }
}

/**
 * Provides helper methods to manage and validate [FormzInput] instances.
 */
object Formz {
    /**
     * Returns `true` if all provided inputs are valid.
     */
    fun validate(inputs: List<FormzInput<*, *>>): Boolean {
        return inputs.all { it.isValid() }
    }

    /**
     * Returns `true` if all provided inputs are still pure.
     */
    fun isPure(inputs: List<FormzInput<*, *>>): Boolean {
        return inputs.all { it.isPure }
    }
}

/**
 * Interface to automatically handle validation for all [FormzInput] instances.
 *
 * Example usage:
 * ```kotlin
 * class LoginFormState(
 *     val username = Username(isPure = true),
 *     val password = Password(isPure = true)
 * ) : FormzInterface {
 *
 *     override val inputs: List<FormzInput<*, *>> = listOf(username, password)
 * }
 * ```
 */
interface FormzInterface {
    /**
     * All the [FormzInput] instances that need to be validated.
     */
    val inputs: List<FormzInput<*, *>>

    /**
     * Returns `true` if all the inputs are valid.
     */
    val isValid: Boolean
        get() = Formz.validate(inputs)

    /**
     * Returns `true` if at least one of the inputs is invalid.
     */
    val isNotValid: Boolean
        get() = !isValid

    /**
     * Returns `true` if all the inputs are pure.
     */
    val isPure: Boolean
        get() = Formz.isPure(inputs)

    /**
     * Returns `true` if at least one of the inputs has been modified (is dirty).
     */
    val isDirty: Boolean
        get() = !isPure
}
