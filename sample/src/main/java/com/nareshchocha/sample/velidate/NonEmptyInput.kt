package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput
import com.nareshchocha.formz.ValidationResult

class NonEmptyInput(
    value: String = "",
    isPure: Boolean = true
) : FormzInput<String, ValidationError>(value, isPure) {
    override fun validator(value: String): ValidationResult<ValidationError> =
        if (value.isNotBlank()) ValidationResult.Success else ValidationResult.Failure(ValidationError.EMPTY)

    fun copy(
        value: String,
        isPure: Boolean = false
    ): NonEmptyInput = NonEmptyInput(value, isPure = isPure)
}
