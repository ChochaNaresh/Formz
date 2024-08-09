package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput

class NonEmptyInput(
    value: String = "",
    isPure: Boolean = true
) : FormzInput<String, ValidationError>(value, isPure) {
    override fun validator(value: String): ValidationError? {
        return if (value.isNotBlank()) null else ValidationError.EMPTY
    }

    fun copy(value: String, isPure: Boolean = false): NonEmptyInput {
        return NonEmptyInput(value, isPure = isPure)
    }
}
