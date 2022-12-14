package cn.net.ilean.cms.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.net.ilean.cms.LeanDestination
import cn.net.ilean.cms.LeanNavigationActions
import cn.net.ilean.cms.ui.components.ChipFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Employees(
    navigationActions: LeanNavigationActions, mainViewModel: MainViewModel, currentRoute: String
) {
    val coroutineScope = rememberCoroutineScope()
    val radioOptions = mapOf(
        "最新登录" to "last_login_time",
        "记录问题" to "count_of_issue_created",
        "解决问题" to "count_of_issue_solved",
        "注册时间" to "register_time",
    )
    var option = remember { radioOptions.entries.first() }
    var pager by remember { mutableStateOf(mainViewModel.users(option.value)) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        AppDrawer(currentRoute, onDrawerItemSelected = { item ->
            when (item) {
                LeanDestination.COMPANIES_ROUTE -> navigationActions.navigateToCompanies()
                LeanDestination.EMPLOYEES_ROUTE -> navigationActions.navigateToEmployees()
            }
            coroutineScope.launch(Dispatchers.Default) { drawerState.close() }
        })
    }, content = {
        Scaffold(topBar = {
            LeanTopAppBar(title = "用户列表", onNavigationIcon = {
                IconButton(onClick = { coroutineScope.launch(Dispatchers.Default) { drawerState.open() } }) {
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            })
        }, content = { paddingValues ->
            val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
            LazyColumn(Modifier.padding(paddingValues)) {
                stickyHeader {
                    UsersScreenHeader { pager = mainViewModel.users(it) }
                }

                if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                items(lazyPagingItems) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row {
                            Text(
                                "${item?.name}",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            if (!item?.phone.isNullOrBlank()) Text(
                                "${item?.phone}", style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        Row {
                            Text(
                                "记录问题 ${item?.countOfIssueCreated} ",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                "解决问题 ${item?.countOfIssueSolved}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Text(
                            "上次登录 ${item?.lastLoginTime}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        if (!item?.registerTime.isNullOrBlank()) Text(
                            "注册时间 ${item?.registerTime}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                if (lazyPagingItems.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        })
    })
}

@Composable
fun UsersScreenHeader(onOptionSelected: (String) -> Unit) {
    Column(Modifier.background(MaterialTheme.colorScheme.background)) {
        ChipFilter(
            mapOf(
                "最新登录" to "last_login_time",
                "记录问题" to "count_of_issue_created",
                "解决问题" to "count_of_issue_solved",
                "注册时间" to "register_time",
            ), onOptionSelected
        )
    }
}