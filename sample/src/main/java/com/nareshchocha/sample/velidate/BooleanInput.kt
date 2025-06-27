package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput
import com.nareshchocha.formz.ValidationResult

class BooleanInput(
    value: Boolean = false,
    isPure: Boolean = true
) : FormzInput<Boolean, ValidationError>(value, isPure) {
    override fun validator(value: Boolean): ValidationResult<ValidationError> =
        if (value) ValidationResult.Success else ValidationResult.Failure(ValidationError.NOT_SELECTED)

    fun copy(
        value: Boolean,
        isPure: Boolean = false
    ): BooleanInput = BooleanInput(value, isPure = isPure)
}
