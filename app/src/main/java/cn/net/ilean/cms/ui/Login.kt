package cn.net.ilean.cms.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Login(onSuccess: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text = "爱精益后台管理系统")
        val phoneNumber = remember { mutableStateOf("13419549231") }
        val verificationCode = remember { mutableStateOf("999999") }
        TextField(value = phoneNumber.value, onValueChange = {})
        TextField(value = verificationCode.value, onValueChange = {})
        Button(onClick = {
//            loginService.login(
//                LoginRequest(
//                    phoneNumber.value,
//                    verificationCode = verificationCode.value
//                )
//            ).enqueue(object : Callback<LoginResponse> {
//                override fun onResponse(
//                    call: Call<LoginResponse>,
//                    response: Response<LoginResponse>
//                ) {
//                    if (response.body()?.code == "0") {
//                        onSuccess()
//                    }
//                }
//
//                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {}
//            })
        }) { Text(text = "登录") }
    }
}