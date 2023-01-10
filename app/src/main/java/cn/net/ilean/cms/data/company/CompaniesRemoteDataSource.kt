package cn.net.ilean.cms.data.company

import cn.net.ilean.cms.network.CompanyServiceImpl
import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.network.response.IssueHistory
import cn.net.ilean.cms.network.response.Page
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompaniesRemoteDataSource @Inject constructor() {
    private val companyService = CompanyServiceImpl()

   suspend fun getCompanies(orderBy: String): Page<Company>? {
        return companyService.getCompanies(orderBy)
    }

    fun getCompany(companyId: Int): Flow<Company?> = companyService.getCompany(companyId)

    fun history(companyId: Int): Flow<List<IssueHistory>?> = companyService.history(companyId)
}