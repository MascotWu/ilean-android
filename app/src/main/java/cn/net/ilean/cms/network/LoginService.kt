package cn.net.ilean.cms.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface LoginService {
    @Headers("authenticationType: weixin")
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}

class LoginResponse(var code: String? = null, var token: String? = null)

class LoginRequest(
    var phone: String? = null,
    var verificationCode: String? = null
)