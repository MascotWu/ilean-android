package cn.net.ilean.cms.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import cn.net.ilean.cms.data.company.CompaniesRepository
import cn.net.ilean.cms.data.employee.UsersRepository
import cn.net.ilean.cms.network.entity.Company
import cn.net.ilean.cms.network.response.IssueHistory
import cn.net.ilean.cms.network.response.Page
import cn.net.ilean.cms.network.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val usersRepository: UsersRepository, val companiesRepository: CompaniesRepository
) : ViewModel() {
    fun users(orderBy: String): PagingSource<Int, User> = usersRepository.users(orderBy)

    fun getCompanies(orderBy: String): Flow<Page<Company>?> {
        return companiesRepository.getCompanies(orderBy)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }

    fun getCompany(companyId: Int): Flow<Company?> {
        return companiesRepository.getCompany(companyId)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }

    fun history(companyId: Int): Flow<List<IssueHistory>?> =
        companiesRepository.history(companyId).stateIn(viewModelScope, SharingStarted.Eagerly, null)

}