package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput
import com.nareshchocha.formz.ValidationResult

class RegexInput(
    private val regex: String,
    private val skipEmpty: Boolean = false,
    value: String = "",
    isPure: Boolean = true
) : FormzInput<String, ValidationError>(value, isPure) {
    override fun validator(value: String): ValidationResult<ValidationError> =
        if (value.isEmpty()) {
            if (skipEmpty) ValidationResult.Success else ValidationResult.Failure(ValidationError.EMPTY)
        } else if (!Regex(regex).matches(value)) {
            ValidationResult.Failure(ValidationError.INVALID)
        } else {
            ValidationResult.Success
        }

    fun copy(
        value: String,
        isPure: Boolean = false
    ): RegexInput = RegexInput(regex, skipEmpty, value, isPure = isPure)
}

object RegularExpressions {
    const val PASSWORD =
        "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"
}
