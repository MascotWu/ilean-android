package cn.net.ilean.cms.network

import cn.net.ilean.cms.network.response.Page
import cn.net.ilean.cms.network.response.User
import cn.net.ilean.cms.network.response.Wrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("user/getEmployees")
    fun getEmployees(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
    ): Call<Wrapper<Page<User>>>
}
