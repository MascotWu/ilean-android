package cn.net.ilean.cms.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.net.ilean.cms.LeanNavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Company(
    navigationActions: LeanNavigationActions,
    companyId: Int,
    mainViewModel: MainViewModel
) {
    val company by mainViewModel.getCompany(companyId).collectAsState(initial = null)
    val history by mainViewModel.history(companyId).collectAsState(initial = null)

    Scaffold(topBar = {
        LeanTopAppBar(title = "企业详情", onNavigationIcon = {
            IconButton(onClick = { navigationActions.navigateToCompanies() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        })
    }, content = { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                company?.name ?: "",
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "注册于 ${company?.dateCreated}",
                style = TextStyle(color = Color.Gray, fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            val registrant = company?.registrant
            Text(
                registrant?.name ?: "",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
            )
            val phoneNumber = registrant?.phone
            if (phoneNumber?.isNotEmpty() == true) Row {
                Icons.Outlined.Phone
                Text(
                    registrant.phone!!,
                    style = TextStyle(color = Color.Blue, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            val lastLoginTime: String? = registrant?.lastLoginTime
            if (lastLoginTime != null) Text(
                "上次登录时间 $lastLoginTime", style = TextStyle(color = Color.Gray, fontSize = 18.sp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "日期", style = TextStyle(
                        color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.W600
                    )
                )
                Text(
                    "记录问题数量", style = TextStyle(
                        color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.W600
                    )
                )
            }
            history?.forEach { issueHistory ->
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
    })
}
