package cn.net.ilean.cms.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.net.ilean.cms.LeanDestination
import cn.net.ilean.cms.LeanNavigationActions
import cn.net.ilean.cms.network.response.User
import cn.net.ilean.cms.userService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Employees(navigationActions: LeanNavigationActions) {
    val pager = remember {
        Pager(
            PagingConfig(
                initialLoadSize = 20,
                pageSize = 20,
                enablePlaceholders = true,
            )
        ) {
            UserSource()
        }
    }
    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: LeanDestination.COMPANIES_ROUTE
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

class UserSource : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult.Page<Int, User> {
        val users = coroutineScope {
            withContext(Dispatchers.IO) {
                userService.getEmployees(page = params.key ?: 1, pageSize = params.loadSize)
                    .execute().body()?.data
            }
        }
        return LoadResult.Page(
            users!!.list!!,
            if (users.pageNum!! <= 1) null else users.pageNum?.minus(1),
            if (users.list!!.isNotEmpty()) users.pageNum?.plus(1) else null
        )
    }
}
