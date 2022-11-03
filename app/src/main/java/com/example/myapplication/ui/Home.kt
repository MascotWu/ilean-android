package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    val companies = remember { mutableStateListOf<Company>() }

    val radioOptions = mapOf(
        "最新注册" to "date_created",
        "问题数量" to "count_of_issue",
        "员工数量" to "count_of_employees",
    )
    val selectedOption = remember { mutableStateOf("最新注册") }
    val onOptionSelected: (String) -> Unit = { key ->
        selectedOption.value = key
        companyService.getCompanies(pageNum = 1, orderBy = radioOptions[key]!!)
            .enqueue(object : Callback<Wrapper<Page<Company>>> {
                override fun onResponse(
                    call: Call<Wrapper<Page<Company>>>, response: Response<Wrapper<Page<Company>>>
                ) {
                    val body = response.body()?.data!!
                    total.value = body.total
                    companies.clear()
                    companies.addAll((body.list!!))
                }

                override fun onFailure(call: Call<Wrapper<Page<Company>>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
    onOptionSelected(selectedOption.value)
    val dismiss = remember { mutableStateOf(true) }
    if (!dismiss.value)
        Dialog(onDismissRequest = { dismiss.value = true }, content = {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                radioOptions.keys.forEach { key ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(key)
                                dismiss.value = true
                            }) {
                        RadioButton(
                            selected = key == selectedOption.value,
                            onClick = {
                                onOptionSelected(key)
                                dismiss.value = true
                            })
                        Text(key, fontSize = 16.sp)
                    }
                }
            }
        })
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            TextButton(onClick = { dismiss.value = false }) { Text(selectedOption.value) }
            if (total.value != null)
                Text(
                    "总数 ${total.value}",
                    Modifier.padding(start = 8.dp, bottom = 4.dp, end = 8.dp, top = 2.dp),
                    fontSize = 14.sp,
                    style = TextStyle(color = Color.Gray),
                )
        }

        LazyColumn {
            items(items = companies, itemContent = { company: Company ->
                Column(
                    modifier = Modifier
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            company.name ?: "",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp,
                        )
                        Text(
                            text = " #${company.companyId}",
                            maxLines = 1,
                            fontSize = 18.sp,
                            style = TextStyle(color = Color.Gray)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Row(modifier = Modifier.padding(top = 4.dp, end = 12.dp)) {
                            Text(
                                "问题 ",
                                style = TextStyle(color = Color.Gray),
                                fontSize = 14.sp
                            )
                            Text(
                                "${company.countOfIssue}",
                                style = TextStyle(color = Color.Blue),
                                fontSize = 14.sp
                            )
                        }
                        Row(modifier = Modifier.padding(top = 4.dp, end = 12.dp)) {
                            Text(
                                "员工 ",
                                style = TextStyle(color = Color.Gray),
                                fontSize = 14.sp
                            )
                            Text(
                                "${company.countOfEmployees}",
                                style = TextStyle(color = Color.Blue),
                                fontSize = 14.sp
                            )
                        }
                    }
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            "注册于 " + company.dateCreated,
                            style = TextStyle(color = Color.Gray),
                            fontSize = 14.sp
                        )
                    }
                }
            })
        }
    }
}
