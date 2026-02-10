package com.example.miniapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onLogoutSuccess()
        }
    }

    fun setUserName(name: String) {
        _userName.value = name
    }
}
