package com.nareshchocha.sample.velidate

import java.util.Objects

abstract class FormFieldValidator<T, E>(
    val value: T,
    val isPure: Boolean = true
) {
    /**
     * Lazily computed validation state to avoid re-computation.
     */
    val validationState: ValidationState<E> by lazy { validator(value) }

    /**
     * An abstract method that subclasses must implement to define validation logic.
     *
     * @param value The value to be validated.
     * @return [ValidationState.Success] if the value is valid, or [ValidationState.Failure]
     *         containing an error if it is invalid.
     */
    protected abstract fun validator(value: T): ValidationState<E>

    /**
     * Creates a new instance of the validator with updated values.
     * Subclasses must implement this to return a new instance of their own type.
     */
    abstract fun copy(
        value: T,
        isPure: Boolean = false
    ): FormFieldValidator<T, E>

    fun isValid(): Boolean = if (isPure) true else validationState is ValidationState.Success

    fun displayError(): E? =
        if (isPure) {
            null
        } else {
            (validationState as? ValidationState.Failure)?.error
        }

    override fun hashCode(): Int = Objects.hash(value, isPure, validationState)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FormFieldValidator<*, *>) return false

        if (value != other.value) return false
        if (isPure != other.isPure) return false
        if (validationState != other.validationState) return false

        return true
    }
}

sealed class ValidationState<out E> {
    /**
     * Represents a successful validation with no errors.
     */
    data object Success : ValidationState<Nothing>()

    /**
     * Represents a failed validation with a specific [error].
     *
     * @param E The type of the error.
     * @property error The validation error details.
     */
    data class Failure<E>(
        val error: E
    ) : ValidationState<E>()
}
