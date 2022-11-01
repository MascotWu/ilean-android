package com.example.myapplication.network

import com.example.myapplication.network.entity.Company
import com.example.myapplication.network.response.Page
import com.example.myapplication.network.response.Wrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CompanyService {
    @GET("company/v2/getCompanies")
    fun getCompanies(
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 0,
        @Query("name") name: String? = null,
        @Query("countOfIssue") countOfIssue: Int? = 20,
        @Query("deleted") deleted: Boolean = false,
        @Query("orderBy") orderBy: String = "date_created",
    ): Call<Wrapper<Page<Company>>>
}
