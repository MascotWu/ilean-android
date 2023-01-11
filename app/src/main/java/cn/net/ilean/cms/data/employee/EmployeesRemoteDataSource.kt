package cn.net.ilean.cms.data.employee

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.net.ilean.cms.network.UserServiceImpl
import cn.net.ilean.cms.network.response.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeesRemoteDataSource @Inject constructor() {
    fun users(orderBy: String) = Pager(
        PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
        ),
        pagingSourceFactory = { UserPagingSource(orderBy) },
    )
}

class UserPagingSource(private val orderBy: String) : PagingSource<Int, User>() {
    private val userService = UserServiceImpl()
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        Log.e("UserPagingSource", ".getRefreshKey")
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return coroutineScope {
            withContext(Dispatchers.IO) {
                try {
                    val users = userService.getUsers(params.key ?: 1, params.loadSize, orderBy)
                    LoadResult.Page(
                        users!!.list!!,
                        if (users.pageNum!! <= 1) null else users.pageNum?.minus(1),
                        if (users.list!!.isNotEmpty()) users.pageNum?.plus(1) else null
                    )
                } catch (e: IOException) {
                    LoadResult.Error(e)
                }
            }
        }
    }
}