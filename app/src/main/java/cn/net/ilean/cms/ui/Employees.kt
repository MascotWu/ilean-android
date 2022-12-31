package cn.net.ilean.cms.ui

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.net.ilean.cms.LeanNavigationActions
import cn.net.ilean.cms.userService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

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
            Source()
        }
    }
    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

    LazyColumn {
        items(
            lazyPagingItems,
        ) { message ->
            Row {
                Text(message ?: "Null", modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

class Source : PagingSource<Int, String>() {
    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        Log.e("Source", "getRefreshKey: ")
        return 2
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult.Page<Int, String> {
        Log.e("Source", "load: key: ${params.key}, loadSize: ${params.loadSize}")
        val users = coroutineScope {
            withContext(Dispatchers.IO) {
                userService.getEmployees(page = params.key ?: 1, pageSize = params.loadSize)
                    .execute().body()?.data
            }
        }
        Log.e("Source", "list.size: ${users?.list!!.size}")
        return LoadResult.Page(
            users.list!!.map { it.name ?: "Null" },
            if (users.pageNum!! <= 1) null else users.pageNum?.minus(1),
            if (users.list!!.isNotEmpty()) users.pageNum?.plus(1) else null
        )
    }
}