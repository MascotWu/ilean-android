package com.example.myapplication.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.companyService
import com.example.myapplication.network.entity.Company
import com.example.myapplication.network.response.Page
import com.example.myapplication.network.response.Wrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Companies() {
    val total = remember { mutableStateOf<Int?>(null) }
    val companies = remember {
        val companies = mutableStateListOf<Company>()
        companyService.getCompanies(pageNum = 1).enqueue(object : Callback<Wrapper<Page<Company>>> {
            override fun onResponse(
                call: Call<Wrapper<Page<Company>>>, response: Response<Wrapper<Page<Company>>>
            ) {
                val body = response.body()?.data!!
                total.value = body.total
                companies.addAll((body.list!!))
            }

            override fun onFailure(call: Call<Wrapper<Page<Company>>>, t: Throwable) {
                Log.e("TAG", "onFailure: ", t)
            }
        })
        companies
    }

    Column {
        if (total.value != null)
            Text(
                "总数 ${total.value}",
                Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                fontSize = 22.sp,
            )
        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(items = companies, itemContent = { company: Company ->
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            company.name ?: "",
                            Modifier.padding(top = 12.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "#${company.companyId}",
                            Modifier.padding(top = 12.dp),
                            maxLines = 1,
                            fontSize = 18.sp,
                            style = TextStyle(color = Color.Blue)
                        )
                    }
                    Text(
                        "注册于 " + company.dateCreated,
                        style = TextStyle(color = Color.Gray),
                        fontSize = 14.sp
                    )
                }
            })
        }
    }
}
