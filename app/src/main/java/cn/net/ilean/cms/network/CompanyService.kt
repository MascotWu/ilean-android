package cn.net.ilean.cms.network

import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.network.response.IssueHistory
import cn.net.ilean.cms.network.response.Page
import cn.net.ilean.cms.network.response.Wrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CompanyService {
    @GET("company/v2/getCompanies")
    fun getCompanies(
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 0,
        @Query("name") name: String? = null,
        @Query("countOfIssue") countOfIssue: Int? = 20,
        @Query("deleted") deleted: Boolean = false,
        @Query("lastUsedTime") lastUsedTime: String? = null,
        @Query("orderBy") orderBy: String = "date_created",
    ): Call<Wrapper<Page<Company>>>

    @GET("company/getCompany/{companyId}")
    fun getCompanies(@Path("companyId") companyId: Int): Call<Wrapper<Company>>

    @GET("issue/history")
    fun history(@Query("companyId") companyId: Int): Call<Wrapper<List<IssueHistory>>>
}
