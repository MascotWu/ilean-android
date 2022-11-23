package cn.net.ilean.cms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.net.ilean.cms.companyService
import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.network.response.Page
import cn.net.ilean.cms.network.response.Wrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Companies(navigate: (Int) -> Unit) {
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
        val today: Calendar = Calendar.getInstance(Locale.CHINA)
        today.add(Calendar.MONTH, -3)
        companyService.getCompanies(
            pageNum = 1, orderBy = radioOptions[key]!!, lastUsedTime = SimpleDateFormat(
                "yyyy-MM-dd", Locale.CHINA
            ).format(Date(today.timeInMillis))
        ).enqueue(object : Callback<Wrapper<Page<Company>>> {
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
    if (!dismiss.value) Dialog(onDismissRequest = { dismiss.value = true }, content = {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {
            radioOptions.keys.forEach { key ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOptionSelected(key)
                            dismiss.value = true
                        }) {
                    RadioButton(selected = key == selectedOption.value, onClick = {
                        onOptionSelected(key)
                        dismiss.value = true
                    })
                    Text(key, fontSize = 16.sp)
                }
            }
        }
    })
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                "Companies",
                style = MaterialTheme.typography.labelLarge,
            )
        })
    }, bottomBar = {
        BottomAppBar(backgroundColor = Color.White) {
            var checkedTab by remember { mutableStateOf(0) }
            listOf(Icons.Filled.Home, Icons.Filled.AccountBox).forEachIndexed { i, imageVector ->
                IconToggleButton(checked = checkedTab == i, onCheckedChange = { checkedTab = i }) {
                    Icon(
                        imageVector = imageVector,
                        tint = if (checkedTab == i) Color.Magenta else Color.Gray,
                        contentDescription = "Home"
                    )
                }
            }
        }
    }) {
        Column(modifier = Modifier.padding(vertical = 2.dp)) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                TextButton(onClick = { dismiss.value = false }) { Text(selectedOption.value) }
                if (total.value != null) Text(
                    "总数 ${total.value}",
                    Modifier.padding(start = 8.dp, bottom = 4.dp, end = 8.dp, top = 2.dp),
                    fontSize = 14.sp,
                    style = TextStyle(color = Color.Gray),
                )
            }

            LazyColumn {
                items(items = companies, itemContent = { company: Company ->
                    Column(modifier = Modifier
                        .clickable { navigate(company.companyId!!) }
                        .padding(horizontal = 16.dp, vertical = 6.dp)) {
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
                                    "问题 ", style = TextStyle(color = Color.Gray), fontSize = 14.sp
                                )
                                Text(
                                    "${company.countOfIssue}",
                                    style = TextStyle(color = Color.Blue),
                                    fontSize = 14.sp
                                )
                            }
                            Row(modifier = Modifier.padding(top = 4.dp, end = 12.dp)) {
                                Text(
                                    "员工 ", style = TextStyle(color = Color.Gray), fontSize = 14.sp
                                )
                                Text(
                                    "${company.countOfEmployees}",
                                    style = TextStyle(color = Color.Blue),
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Column(modifier = Modifier.padding(top = 4.dp)) {
                            Text(
                                "注册于 " + company.dateCreated,
                                style = TextStyle(color = Color.Gray),
                                fontSize = 14.sp
                            )
                            if (company.lastUsedTime != null) {
                                val lastUsedTime = SimpleDateFormat(
                                    "yyyy-MM-dd", Locale.CHINA
                                ).parse(company.lastUsedTime!!)
                                val numberOfDaysInactive = TimeUnit.DAYS.convert(
                                    Date().time - lastUsedTime!!.time, TimeUnit.MILLISECONDS
                                )
                                if (numberOfDaysInactive < 30) {
                                    Text(
                                        "${numberOfDaysInactive}天前使用过",
                                        style = TextStyle(color = Color.Gray),
                                        fontSize = 14.sp
                                    )
                                } else {
                                    Text(
                                        "超过${numberOfDaysInactive}天未使用", style = TextStyle(
                                            color = Color.Red, fontWeight = FontWeight.W600
                                        ), fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                })
            }
        }
    }
}
