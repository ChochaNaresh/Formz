package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput
import com.nareshchocha.formz.ValidationResult

class AnyNonEmptyInput<T>(
    value: T,
    isPure: Boolean = true
) : FormzInput<T, ValidationError>(value, isPure) {
    override fun validator(value: T): ValidationResult<ValidationError> =
        if (value == null) ValidationResult.Failure(ValidationError.INVALID) else ValidationResult.Success

    fun copy(
        value: T,
        isPure: Boolean = false
    ): AnyNonEmptyInput<T> = AnyNonEmptyInput(value, isPure = isPure)
}
