package cn.net.ilean.cms.data.employee

import javax.inject.Inject

class UsersRepository @Inject constructor(
    val employeesRemoteDataSource: EmployeesRemoteDataSource
) {
    fun users(orderBy: String) = employeesRemoteDataSource.users(orderBy)
}

