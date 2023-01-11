package cn.net.ilean.cms.network

import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.network.response.IssueHistory
import cn.net.ilean.cms.network.response.Page
import cn.net.ilean.cms.network.response.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*

val retrofit = Retrofit.Builder().baseUrl("https://ilean.net.cn/api/")
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create()).build()
val loginService = retrofit.create(LoginService::class.java)
val userService = retrofit.create(UserService::class.java)

class CompanyServiceImpl {
    private val companyService = retrofit.create(CompanyService::class.java)

    suspend fun getCompanies(orderBy: String): Page<Company>? {
        val today: Calendar = Calendar.getInstance(Locale.CHINA)
        today.add(Calendar.MONTH, -3)
        return companyService.getCompanies(
            pageNum = 1, orderBy = orderBy, lastUsedTime = SimpleDateFormat(
                "yyyy-MM-dd", Locale.CHINA
            ).format(Date(today.timeInMillis))
        ).execute().body()?.data
    }

    fun getCompany(companyId: Int): Flow<Company?> = flow {
        emit(companyService.getCompanies(companyId).execute().body()?.data)
    }.flowOn(Dispatchers.IO)

    fun history(companyId: Int): Flow<List<IssueHistory>?> = flow {
        emit(companyService.history(companyId).execute().body()?.data)
    }.flowOn(Dispatchers.IO)
}

class UserServiceImpl {
    fun getUsers(page: Int, pageSize: Int, orderBy: String): Page<User>? {
        return userService.getEmployees(
            page = page, pageSize = pageSize, orderBy = orderBy,
        ).execute().body()?.data
    }
}