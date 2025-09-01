package com.nikelroid.artist_pedia


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegScreen(navController: NavHostController,userData: UserObject= viewModel() ) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var emailError by remember { mutableStateOf<String?>(null) }
    var emailChanged = false
    var passwordChanged = false
    var nameChanged = false
    var passwordError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }


    val checkEmailFormat: () -> Boolean = {
        when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty() -> {
                emailError = "Invalid email format"
                false
            }

            else -> {
                emailError = null
                true
            }
        }
    }

    val validateEmail: () -> Boolean = {

        when {
            email.isEmpty() -> {
                emailError = "Email cannot be empty"
                false
            }

            else -> {
                emailError = null
                true
            }
        }
    }


    val validatePassword: () -> Boolean = {
        when {
            password.isEmpty() -> {
                passwordError = "Password cannot be empty"
                false
            }

            else -> {
                passwordError = null
                true
            }
        }
    }

    val validateName: () -> Boolean = {
        when {
            name.isEmpty() -> {
                nameError = "Full name cannot be empty"
                false
            }

            else -> {
                nameError = null
                true
            }
        }
    }

    val validateForm: () -> Boolean = {
        val emailValid = validateEmail()
        val nameValid = validateName()
        val emailFormat = checkEmailFormat()
        val passwordValid = validatePassword()
        emailValid && passwordValid && emailFormat && nameValid
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(top = 60.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { navController.popBackStack() }
                )

                Text(
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .weight(1f),
                    text = "Register",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                ) {
                    Spacer(modifier = Modifier.height(240.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                nameChanged = true
                                validateName()
                            },
                            label = { Text("Enter full name") },
                            singleLine = true,
                            isError = nameError != null,
                            modifier = Modifier.fillMaxWidth().onFocusChanged({
                                if (nameChanged || it.isFocused) validateName()
                            }),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface,
                                unfocusedLabelColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.secondary,
                                focusedLabelColor =  MaterialTheme.colorScheme.secondary,
                                focusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else  MaterialTheme.colorScheme.secondary,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                errorLabelColor = MaterialTheme.colorScheme.error,
                                errorCursorColor = MaterialTheme.colorScheme.error
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        nameError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top=2.dp,start = 4.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailChanged = true
                                validateEmail()

                            },
                            label = { Text("Enter email") },
                            singleLine = true,
                            isError = emailError != null,
                            modifier = Modifier.fillMaxWidth().onFocusChanged({
                                if (emailChanged && !it.isFocused) {
                                    validateEmail()
                                    checkEmailFormat()
                                }
                                if (it.isFocused) validateEmail()
                            }),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface,
                                unfocusedLabelColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.secondary,
                                focusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else  MaterialTheme.colorScheme.secondary,
                                focusedLabelColor =  MaterialTheme.colorScheme.secondary,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                errorLabelColor = MaterialTheme.colorScheme.error,
                                errorCursorColor = MaterialTheme.colorScheme.error
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        emailError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top=2.dp,start = 4.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                passwordChanged = true
                            },
                            label = { Text("Password") },
                            singleLine = true,
                            isError = passwordError != null,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().onFocusChanged({
                                if (passwordChanged || it.isFocused) validatePassword()
                            }),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface,
                                unfocusedLabelColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.secondary,
                                focusedBorderColor = if (emailError != null) MaterialTheme.colorScheme.error
                                else  MaterialTheme.colorScheme.secondary,
                                focusedLabelColor =  MaterialTheme.colorScheme.secondary,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                errorLabelColor = MaterialTheme.colorScheme.error,
                                errorCursorColor = MaterialTheme.colorScheme.error
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        passwordError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top=2.dp,start = 4.dp)
                            )
                        }
                    }


                    fun sha(input: String,type: String): String {
                        val bytes = input.toByteArray(Charsets.UTF_8)
                        val md = MessageDigest.getInstance(type)
                        val digest = md.digest(bytes)
                        return digest.fold("") { str, it -> str + "%02x".format(it) }
                    }

                    var isLoading by remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            if (!isLoading && validateForm()) {
                                scope.launch {
                                    isLoading = true

                                    val passHashed = sha(password,"SHA-256")
                                    val emailHashed = sha(email.toLowerCase(Locale.ROOT),"SHA-224")
                                    val data = mutableMapOf<String, String>()
                                    data.put("pass",passHashed)
                                    data.put("email",emailHashed)
                                    data.put("name",name)
                                    val regResult = postRegister(data)
                                    isLoading = false
                                    if (regResult.acknowledged=="OK") {
                                        userData.userSetter(regResult)
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Registered successfully",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                        delay(500)
                                        navController.popBackStack("main", inclusive = false)
                                    }else if(regResult.acknowledged=="exist"){
                                        emailError = "Email already exists"
                                    }else{
                                        Toast.makeText(context, "Couldn't complete registering", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceBright
                        ),
                        enabled = !isLoading
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(

                                    text = "Register",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Login",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { navController.popBackStack("login", inclusive = false) }
                        )
                    }
                }
            }

        }



    }

}

