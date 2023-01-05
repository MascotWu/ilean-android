package cn.net.ilean.cms.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagingSource
import cn.net.ilean.cms.data.employee.UsersRepository
import cn.net.ilean.cms.network.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val usersRepository: UsersRepository
) : ViewModel() {
    val users: PagingSource<Int, User> = usersRepository.users
}