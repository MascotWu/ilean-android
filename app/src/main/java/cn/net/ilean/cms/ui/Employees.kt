package cn.net.ilean.cms.ui

import android.app.Dialog
import android.util.Log
import android.widget.RadioGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.net.ilean.cms.LeanDestination
import cn.net.ilean.cms.LeanNavigationActions
import cn.net.ilean.cms.network.UserService
import cn.net.ilean.cms.network.response.User
import cn.net.ilean.cms.userService
import dagger.hilt.InstallIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Composable
fun Employees(navigationActions: LeanNavigationActions, mainViewModel: MainViewModel) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
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
    var pager = remember {
        Pager(
            PagingConfig(
                initialLoadSize = 20,
                pageSize = 20,
                enablePlaceholders = true,
            )
        ) {
            mainViewModel.users(option.value)
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            LeanTopAppBar(onNavigationIcon = {
                IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }
            })
        },
        drawerContent = {
            LeanDrawer(currentRoute, onDrawerItemSelected = { item ->
                when (item) {
                    LeanDestination.COMPANIES_ROUTE -> navigationActions.navigateToCompanies()
                    LeanDestination.EMPLOYEES_ROUTE -> navigationActions.navigateToEmployees()
                }
                scope.launch { scaffoldState.drawerState.close() }
            })
        },
    ) {
        println("<top>.Employees 切换之后，这里会重新执行")
        var dialogVisible by remember { mutableStateOf(false) }
        if (dialogVisible) Dialog(onDismissRequest = { dialogVisible = false }) {
            Column(Modifier.background(MaterialTheme.colors.background)) {
                radioOptions.entries.forEach {
                    Row {
                        RadioButton(selected = option.key == it.key, onClick = {
                            option = it; dialogVisible = false;
                            println("option = ${option.value}")
                            pager = Pager(
                                PagingConfig(
                                    initialLoadSize = 20,
                                    pageSize = 20,
                                    enablePlaceholders = true,
                                )
                            ) {
                                mainViewModel.users(option.value)
                            }
                        })
                        Text(text = it.key)
                    }
                }
            }
        }
        Column {
            TextButton(onClick = { dialogVisible = true }) {
                Text(option.key)
            }

            val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                    item {
                        Text(
                            text = "Waiting for items to load from the backend",
                            modifier = Modifier
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
                        if (!item?.phone.isNullOrBlank()) Text("${item?.phone}", fontSize = 18.sp)
                        Text("上次登录 ${item?.lastLoginTime}", fontSize = 18.sp)
                        Text("记录问题 ${item?.countOfIssueCreated}", fontSize = 18.sp)
                        Text("解决问题 ${item?.countOfIssueSolved}", fontSize = 18.sp)
                        Text("注册时间 ${item?.registerTime ?: "(空)"}", fontSize = 18.sp)
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
        }
    }
}