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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val companiesRepository: CompaniesRepository
) : ViewModel() {
    fun users(orderBy: String): PagingSource<Int, User> = usersRepository.users(orderBy)

    private val viewModelState = MutableStateFlow(MainViewModelState(true))

    val uiState = viewModelState.map { it.toUiState() }.stateIn(
        viewModelScope, SharingStarted.Eagerly, MainUiState.Companies()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelState.emit(
                MainViewModelState(isLoading = false, page = getCompanies("date_created"))
            )
        }
    }

    suspend fun getCompanies(orderBy: String): Page<Company>? {
        return companiesRepository.getCompanies(orderBy)
    }

    fun getCompany(companyId: Int): Flow<Company?> {
        return companiesRepository.getCompany(companyId)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }

    fun history(companyId: Int): Flow<List<IssueHistory>?> =
        companiesRepository.history(companyId).stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

data class MainViewModelState(val isLoading: Boolean, val page: Page<Company>? = null) {
    fun toUiState(): MainUiState.Companies = MainUiState.Companies(page)
}

class MainUiState {
    data class Companies(
        val page: Page<Company>? = null
    )
}