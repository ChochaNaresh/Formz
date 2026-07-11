package com.nareshchocha.sample.velidate

class NonEmptyInput(
    value: String = "",
    isPure: Boolean = true
) : FormFieldValidator<String, ValidationError>(value, isPure) {
    override fun validator(value: String): ValidationState<ValidationError> =
        if (value.isNotBlank()) ValidationState.Success else ValidationState.Failure(ValidationError.EMPTY)

    override fun copy(
        value: String,
        isPure: Boolean
    ): NonEmptyInput = NonEmptyInput(value, isPure = isPure)
}
