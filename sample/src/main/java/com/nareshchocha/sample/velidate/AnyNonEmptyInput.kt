package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput

class AnyNonEmptyInput<T>(
    value: T,
    isPure: Boolean = true
) : FormzInput<T, ValidationError>(value, isPure) {
    override fun validator(value: T): ValidationError? {
        return if (value == null) ValidationError.INVALID else null
    }

    fun copy(value: T, isPure: Boolean = false): AnyNonEmptyInput<T> {
        return AnyNonEmptyInput(value, isPure = isPure)
    }
}
