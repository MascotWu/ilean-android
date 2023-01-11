package cn.net.ilean.cms.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.net.ilean.cms.LeanDestination
import cn.net.ilean.cms.LeanNavigationActions
import cn.net.ilean.cms.ui.components.ChipFilter
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Employees(navigationActions: LeanNavigationActions, mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: LeanDestination.COMPANIES_ROUTE
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
            mainViewModel.viewModelScope.launch { drawerState.close() }
        })
    }, content = {
        Scaffold(topBar = {
            LeanTopAppBar(onNavigationIcon = {
                IconButton(onClick = { mainViewModel.viewModelScope.launch { drawerState.open() } }) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
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
                        Text("${item?.name}", fontSize = 20.sp)
                        if (!item?.phone.isNullOrBlank()) Text(
                            "${item?.phone}", fontSize = 18.sp
                        )
                        Text("上次登录 ${item?.lastLoginTime}", fontSize = 18.sp)
                        Text("记录问题 ${item?.countOfIssueCreated}", fontSize = 18.sp)
                        Text("解决问题 ${item?.countOfIssueSolved}", fontSize = 18.sp)
                        if (!item?.registerTime.isNullOrBlank()) Text(
                            "注册时间 ${item?.registerTime ?: "(空)"}", fontSize = 18.sp
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