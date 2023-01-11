package cn.net.ilean.cms.data.employee

import androidx.paging.Pager
import cn.net.ilean.cms.network.response.User
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val employeesRemoteDataSource: EmployeesRemoteDataSource
) {
    fun users(orderBy: String): Pager<Int, User> = employeesRemoteDataSource.users(orderBy)
}

