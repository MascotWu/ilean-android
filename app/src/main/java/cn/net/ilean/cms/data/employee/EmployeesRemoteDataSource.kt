package cn.net.ilean.cms.data.employee

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.net.ilean.cms.network.response.User
import cn.net.ilean.cms.userService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeesRemoteDataSource @Inject constructor() {
    val users = UserPagingSource()
}

class UserPagingSource : PagingSource<Int, User>() {
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