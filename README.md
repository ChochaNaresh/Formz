
# Formz

[![Maven Central](https://img.shields.io/maven-central/v/io.github.chochanaresh/formz.svg)](https://search.maven.org/artifact/io.github.chochanaresh/formz)
![Build](https://github.com/ChochaNaresh/formz/actions/workflows/ci.yml/badge.svg)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
![Language](https://img.shields.io/badge/language-Kotlin-orange.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.1.20-blue)

**Formz** is a lightweight validation framework for Android forms written in Kotlin. It provides a simple, yet powerful way to define, validate, and manage form inputs in your Android applications. The library is designed with immutability and performance in mind, ensuring that expensive validation logic is computed only once per input.


## Features


**Immutable Form Inputs:**

```Define form inputs as immutable objects to ensure consistency and safety.```

**Lazy Validation Caching:**

```Validation results are cached using Kotlin's lazy properties to avoid duplicate computations.```

**Extensible Validation Logic:**

```Extend the FormzInput class and implement custom validation logic for your specific needs.```

**Interface for Form State:**

```Use the FormzInterface to automatically handle the validation status of multiple form inputs.```


## inspire from

inspire from **Very Good Ventures** 🦄
from flutter package
[formz](https://pub.dev/packages/formz)
## Installation

To include Formz in your project, simply add the source files to your Kotlin Android project. If you decide to publish the library later, you can host it on JitPack or another Maven repository for easier integration.

### How to add dependencies

## 📦 Installation

### Gradle (Groovy)
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.chochanaresh:formz:<latest-version>'
}
```

### Gradle (Kotlin DSL)
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.chochanaresh:formz:<latest-version>")
}
```
## Version
`latest-version` = [![libVersion](https://img.shields.io/maven-central/v/io.github.chochanaresh/formz.svg)](https://central.sonatype.com/artifact/io.github.chochanaresh/formz/versions)
---

## Usage


**Defining a Custom Form Input**

Extend FormzInput to create your own input type with custom validation logic. For example, here’s how you might define a simple non-empty string input:

```kotlin
package com.nareshchocha.formz

// Custom input that validates that a string is not empty.
class NonEmptyInput(value: String, isPure: Boolean) : FormzInput<String, String>(value, isPure) {
    override fun validator(value: String): ValidationResult<String> {
        return if (value.trim().isEmpty()) {
            ValidationResult.Failure("Field cannot be empty")
        } else {
            ValidationResult.Success
        }
    }
}
```
**Defining a Form State**

Implement the FormzInterface to bundle your form inputs together. This allows you to check the overall validation state of the form easily.
```kotlin
package com.nareshchocha.formz

class LoginFormState(
    val username: NonEmptyInput,
    val password: NonEmptyInput
) : FormzInterface {
    override val inputs: List<FormzInput<*, *>>
        get() = listOf(username, password)
}
```

**Checking Validation**

You can now use the provided helper functions to validate the entire form:

```kotlin
val loginForm = LoginFormState(
    username = NonEmptyInput("user123", isPure = false),
    password = NonEmptyInput("", isPure = false)
)

if (loginForm.isValid) {
    // Proceed with form submission
} else {
    // Display validation errors
    println("Username error: ${loginForm.username.displayError()?.error}")
    println("Password error: ${loginForm.password.displayError()?.error}")
}
```


## API Reference
## ```FormzInput```

**Properties:**

```value```: The input's current value.

```isPure```: A flag indicating whether the input has been modified.

**Methods:**

```abstract fun validator(value: T): ValidationResult<E>```: Implement your custom validation logic.

```fun isValid()```: Boolean: Returns true if the input is valid.

```fun error(): ValidationResult.Failure<E>?```: Returns the validation error if one exists.

```fun displayError(): ValidationResult.Failure<E>?```: Returns the error to display only if the input is not pure.

## ```ValidationResult```
A sealed class representing the result of a validation:

```ValidationResult.Success```: Indicates successful validation.

```ValidationResult.Failure<E>(val error: E)```: Indicates validation failure with an error message.

## ```Formz```
Utility object for working with collections of FormzInput:

```fun validate(inputs: List<FormzInput<*, *>>): Boolean```

```fun isPure(inputs: List<FormzInput<*, *>>): Boolean```

## ```FormzInterface```
An interface that automatically handles form validation:

```val inputs: List<FormzInput<*, *>>```

```val isValid: Boolean```

```val isNotValid: Boolean```

```val isPure: Boolean```

```val isDirty: Boolean```

## Contributing
Contributions are welcome! If you find any bugs or want to add new features, please fork the repository and submit a pull request. Make sure to follow the existing code style and include tests for your changes.

## Support

For support, email  chochanaresh0@gmail.com or join our Slack channel.

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/chochanaresh)

## License
```text
Copyright 2025 Naresh chocha

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    
http://www.apache.org/licenses/LICENSE-2.0
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
