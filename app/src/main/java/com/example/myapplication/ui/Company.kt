package com.example.myapplication.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.companyService
import com.example.myapplication.network.entity.Company
import com.example.myapplication.network.response.Wrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Company(companyId: Int, navigate: () -> Unit) {
    val company = remember { mutableStateOf<Company?>(Company()) }
    companyService.getCompanies(companyId).enqueue(object : Callback<Wrapper<Company>> {
        override fun onResponse(
            call: Call<Wrapper<Company>>, response: Response<Wrapper<Company>>
        ) {
            company.value = response.body()?.data
            Log.e("TAG", "onResponse: ")
        }

        override fun onFailure(call: Call<Wrapper<Company>>, t: Throwable) {
            Log.e("TAG", "onFailure: ")
        }
    })
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "企业详情") },
            navigationIcon = {
                IconButton(onClick = { navigate() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            })
    }, content = {
        Column(Modifier.padding(16.dp)) {
            Text(
                company.value?.name ?: "",
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "注册于 ${company.value?.dateCreated}",
                style = TextStyle(color = Color.Gray, fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            val registrant = company.value?.registrant
            Text(
                registrant?.name ?: "",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
            )
            val phoneNumber = registrant?.phone
            if (phoneNumber?.isNotEmpty() == true)
                Row {
                    Icons.Outlined.Phone
                    Text(
                        registrant.phone!!,
                        style = TextStyle(color = Color.Blue, fontSize = 18.sp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            val lastLoginTime: String? = registrant?.lastLoginTime
            if (lastLoginTime != null)
                Text(
                    "上次登录时间 $lastLoginTime",
                    style = TextStyle(color = Color.Gray, fontSize = 18.sp)
                )
        }
    })
}
