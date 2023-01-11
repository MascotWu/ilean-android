package cn.net.ilean.cms.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.net.ilean.cms.LeanDestination.COMPANIES_ROUTE
import cn.net.ilean.cms.LeanDestination.EMPLOYEES_ROUTE
import cn.net.ilean.cms.LeanNavigationActions
import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.ui.components.ChipFilter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Companies(
    navigationActions: LeanNavigationActions,
    mainViewModel: MainViewModel,
    currentRoute: String,
    onSelectCompany: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val uiState by mainViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        AppDrawer(currentRoute, onDrawerItemSelected = { item ->
            when (item) {
                COMPANIES_ROUTE -> navigationActions.navigateToCompanies()
                EMPLOYEES_ROUTE -> navigationActions.navigateToEmployees()
            }
            scope.launch { drawerState.close() }
        })
    }, content = {
        Scaffold(topBar = {
            LeanTopAppBar(title = "企业列表", onNavigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            })
        }, content = { paddingValues ->
            CompaniesScreenContent(
                mainViewModel, uiState, onSelectCompany, modifier = Modifier.padding(paddingValues)
            )
        })
    })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompaniesScreenContent(
    mainViewModel: MainViewModel,
    uiState: MainUiState.Companies,
    onSelectCompany: (Int) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(modifier) {
        stickyHeader {
            CompaniesScreenHeader(mainViewModel, uiState)
        }
        items(items = uiState.page?.list ?: emptyList(), itemContent = { company: Company ->
            Column(modifier = Modifier
                .clickable { onSelectCompany(company.companyId!!) }
                .padding(horizontal = 16.dp, vertical = 6.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = company.name ?: "",
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = " #${company.companyId}",
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
                ) {
                    Row(modifier = Modifier.padding(top = 4.dp, end = 12.dp)) {
                        Text("问题 ", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${company.countOfIssue}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Row(modifier = Modifier.padding(top = 4.dp, end = 12.dp)) {
                        Text("员工 ", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${company.countOfEmployees}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        "注册于 " + company.dateCreated,
                        style = MaterialTheme.typography.bodyMedium,
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
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        } else {
                            Text(
                                "超过${numberOfDaysInactive}天未使用",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        })
    }
}

@Composable
fun CompaniesScreenHeader(
    mainViewModel: MainViewModel, uiState: MainUiState.Companies
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ChipFilter(
            mapOf(
                "最新注册" to "date_created",
                "问题数量" to "count_of_issue",
                "员工数量" to "count_of_employees",
            )
        ) { mainViewModel.getCompanies(it) }
        if (uiState.page != null) Text(
            "总数 ${uiState.page.total}",
            Modifier.padding(start = 8.dp, bottom = 4.dp, end = 8.dp, top = 2.dp),
            style = MaterialTheme.typography.titleSmall,
        )
    }
}