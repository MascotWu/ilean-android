package cn.net.ilean.cms.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.net.ilean.cms.companyService
import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.network.response.IssueHistory
import cn.net.ilean.cms.network.response.Wrapper
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
        }

        override fun onFailure(call: Call<Wrapper<Company>>, t: Throwable) {
            Log.e("TAG", "onFailure: ")
        }
    })

    val history = remember { mutableStateOf<List<IssueHistory>>(emptyList()) }
    companyService.history(companyId).enqueue(object : Callback<Wrapper<List<IssueHistory>>> {
        override fun onResponse(
            call: Call<Wrapper<List<IssueHistory>>>,
            response: Response<Wrapper<List<IssueHistory>>>
        ) {
            history.value = response.body()?.data!!
        }

        override fun onFailure(call: Call<Wrapper<List<IssueHistory>>>, t: Throwable) {
            TODO("Not yet implemented")
        }

    })
    Column(
        Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "日期",
                style = TextStyle(
                    color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.W600
                )
            )
            Text(
                "记录问题数量",
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            )
        }
        history.value.forEach { issueHistory ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    issueHistory.date!!,
                    style = TextStyle(color = Color.Gray, fontSize = 18.sp)
                )
                Text(
                    issueHistory.count.toString(),
                    style = TextStyle(color = Color.Blue, fontSize = 18.sp)
                )
            }
        }
    }
}
