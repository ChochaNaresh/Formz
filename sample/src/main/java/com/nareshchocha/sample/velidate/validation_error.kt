package com.nareshchocha.sample.velidate

enum class ValidationError {
    EMPTY,
    INVALID,
    NOT_SELECTED,
}

fun List<ValidationError?>.combineErrors(): ValidationError? {
    return this.filterNotNull().firstOrNull()
}

fun ValidationError.getErrorMessage(
    field: String,
    customize: ((field: String) -> String)? = null
): String {
    val customError = customize?.invoke(field)
    if (customError != null) return customError

    return when (this) {
        ValidationError.EMPTY -> "$field cannot be empty."
        ValidationError.INVALID -> "$field has invalid value."
        ValidationError.NOT_SELECTED -> "You must select $field"
        else -> throw IllegalArgumentException("Unsupported ValidationError type")
    }
}