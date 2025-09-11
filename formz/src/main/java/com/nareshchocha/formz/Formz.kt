package com.nareshchocha.formz

import java.util.Objects

/**
 * A sealed class representing the outcome of a validation process.
 * It can either be a [Success] or a [Failure].
 *
 * @param E The type of the error content in case of a failure.
 */
sealed class ValidationResult<out E> {
    /**
     * Represents a successful validation with no errors.
     */
    data object Success : ValidationResult<Nothing>()

    /**
     * Represents a failed validation with a specific [error].
     *
     * @param E The type of the error.
     * @property error The validation error details.
     */
    data class Failure<E>(
        val error: E
    ) : ValidationResult<E>()
}

/**
 * An abstract representation of a single form input field with validation capabilities.
 * It is designed to be immutable. State changes are represented by creating new instances.
 *
 * @param T The type of the input's value (e.g., `String`, `Int`).
 * @param E The type of the validation error (e.g., `String`, an enum).
 * @property value The current value of the input.
 * @property isPure `true` if the input has not been modified by the user; `false` otherwise.
 */
abstract class FormzInput<T, E>(
    val value: T,
    val isPure: Boolean
) {
    // The validation result is computed lazily and cached, as the input is immutable.
    private val validationResult: ValidationResult<E> by lazy { validator(value) }

    /**
     * An abstract method that subclasses must implement to define validation logic.
     *
     * @param value The value to be validated.
     * @return [ValidationResult.Success] if the value is valid, or [ValidationResult.Failure]
     *         containing an error if it is invalid.
     */
    abstract fun validator(value: T): ValidationResult<E>

    /**
     * Checks if the input is valid.
     * An input is considered valid if it is `pure` or if its value passes the [validator].
     *
     * @return `true` if the input is valid, `false` otherwise.
     */
    fun isValid(): Boolean = if (isPure) true else validationResult == ValidationResult.Success

    /**
     * Retrieves the validation error if the input is invalid.
     *
     * @return A [ValidationResult.Failure] containing the error if validation fails;
     *         otherwise, `null`.
     */
    fun error(): ValidationResult.Failure<E>? =
        when (validationResult) {
            is ValidationResult.Failure -> validationResult as ValidationResult.Failure<E>
            ValidationResult.Success -> null
        }

    /**
     * Returns the error content suitable for display in the UI.
     * No error is returned for `pure` inputs to avoid showing errors before user interaction.
     *
     * @return The error of type [E] if the input is dirty and invalid; otherwise, `null`.
     */
    fun displayError(): E? = if (isPure) null else error()?.error

    override fun hashCode(): Int = Objects.hash(value, isPure)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FormzInput<*, *>) return false

        if (value != other.value) return false
        if (isPure != other.isPure) return false

        return true
    }

    override fun toString(): String = "FormzInput(value=$value, isPure=$isPure, isValid=${isValid()}, error=${error()})"
}

/**
 * A utility object providing helper functions for managing collections of [FormzInput] instances.
 */
object Formz {
    /**
     * Validates a list of [FormzInput]s.
     * The validation short-circuits, returning `false` as soon as the first invalid input is found.
     *
     * @param inputs The list of [FormzInput] instances to validate.
     * @return `true` if all inputs are valid, `false` otherwise.
     */
    fun validate(inputs: List<FormzInput<*, *>>): Boolean {
        for (input in inputs) {
            if (!input.isValid()) return false
        }
        return true
    }

    /**
     * Checks if all inputs in a list are `pure`.
     * The check short-circuits, returning `false` as soon as the first dirty input is found.
     *
     * @param inputs The list of [FormzInput] instances to check.
     * @return `true` if all inputs are pure, `false` otherwise.
     */
    fun isPure(inputs: List<FormzInput<*, *>>): Boolean {
        for (input in inputs) {
            if (!input.isPure) return false
        }
        return true
    }
}

/**
 * An interface for classes that group multiple [FormzInput]s, such as a form's state.
 * Implementing this interface provides convenient access to the collective validation status.
 *
 * Example usage:
 * ```kotlin
 * data class LoginFormState(
 *     val username: Username = Username(""),
 *     val password: Password = Password("")
 * ) : FormzInterface {
 *     override val inputs: List<FormzInput<*, *>> = listOf(username, password)
 * }
 * ```
 */
interface FormzInterface {
    /**
     * A list containing all the [FormzInput] instances managed by this container.
     */
    val inputs: List<FormzInput<*, *>>

    /**
     * A computed property that indicates whether all [inputs] are valid.
     *
     * @return `true` if all inputs are valid, `false` otherwise.
     */
    val isValid: Boolean
        get() = Formz.validate(inputs)

    /**
     * A computed property that is the inverse of [isValid].
     *
     * @return `true` if at least one input is invalid, `false` otherwise.
     */
    val isNotValid: Boolean
        get() = !isValid

    /**
     * A computed property that indicates whether all [inputs] are pure.
     *
     * @return `true` if all inputs are pure, `false` otherwise.
     */
    val isPure: Boolean
        get() = Formz.isPure(inputs)

    /**
     * A computed property that indicates whether any of the [inputs] is dirty (not pure).
     *
     * @return `true` if at least one input is dirty, `false` otherwise.
     */
    val isDirty: Boolean
        get() = !isPure
}
