package cn.net.ilean.cms.data.company

import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.network.response.IssueHistory
import cn.net.ilean.cms.network.response.Page
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CompaniesRepository @Inject constructor(
    val companiesRemoteDataSource: CompaniesRemoteDataSource
) {
    fun getCompanies(orderBy: String): Flow<Page<Company>?> {
        return companiesRemoteDataSource.getCompanies(orderBy)
    }

    fun getCompany(companyId: Int): Flow<Company?> = companiesRemoteDataSource.getCompany(companyId)

    fun history(companyId: Int): Flow<List<IssueHistory>?> = companiesRemoteDataSource.history(companyId)
}