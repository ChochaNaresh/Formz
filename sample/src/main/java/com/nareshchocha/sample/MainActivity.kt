package com.nareshchocha.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nareshchocha.sample.baseComponents.AppOutlinedTextField
import com.nareshchocha.sample.ui.theme.SampleTheme
import com.nareshchocha.sample.velidate.BooleanInput
import com.nareshchocha.sample.velidate.NonEmptyInput
import com.nareshchocha.sample.velidate.RegexInput
import com.nareshchocha.sample.velidate.RegularExpressions
import com.nareshchocha.sample.velidate.getErrorMessage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AllComponents(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AllComponents(modifier: Modifier = Modifier) {
    var testInputTextField by remember { mutableStateOf(NonEmptyInput()) }
    var passwordInputTextField by remember { mutableStateOf(RegexInput(RegularExpressions.PASSWORD)) }
    var checkBox by remember { mutableStateOf(BooleanInput()) }
    Column(modifier = modifier) {
        AppOutlinedTextField(
            value = testInputTextField.value,
            onValueChange = { text ->
                println("AppOutlinedTextField= $text")
                testInputTextField = testInputTextField.copy(text)
            },
            label = {
                Text(text = "Test")
            },
            isError = testInputTextField.isNotValid,
            errorMassage = testInputTextField.displayError()
                ?.getErrorMessage("Test"),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),

            modifier = Modifier.fillMaxWidth(),
        )

        PasswordTextField(
            value = passwordInputTextField.value,
            isError = passwordInputTextField.isNotValid,
            errorMassage = passwordInputTextField.displayError()
                ?.getErrorMessage("Password"),
            onValueChange = {
                println("PasswordTextField= $it")
                passwordInputTextField = passwordInputTextField.copy(it)
            },
            onClick = {}
        )

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkBox.value, onCheckedChange = {
                    println("Checkbox= ${checkBox.displayError()}")
                    checkBox = checkBox.copy(it)
                })
                Text("Test Checkbox")
            }
            if (!checkBox.isValid()) {
                Text(
                    text = checkBox.displayError()?.getErrorMessage("Test Checkbox") ?: "",
                    modifier = Modifier.padding(start = 20.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

    }
}


@Composable
private fun PasswordTextField(
    value: String,
    isError: Boolean,
    errorMassage: String?,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(value = false) }

    AppOutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = {
            Text(text = "password")
        },
        isError = isError,
        errorMassage = errorMassage,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
        ),
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "hide_password"
                    )
                }
            } else {
                IconButton(
                    onClick = { showPassword = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "hide_password"
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            onClick()
        })
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SampleTheme {
        AllComponents()
    }
}