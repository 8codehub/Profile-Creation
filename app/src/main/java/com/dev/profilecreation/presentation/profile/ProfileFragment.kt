package com.dev.profilecreation.presentation.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.dev.profilecreation.R
import com.dev.profilecreation.databinding.FragmentProfileBinding
import com.dev.profilecreation.presentation.core.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewStateListener()
        getUserData()
    }

    private fun getUserData() {
        val args: ProfileFragmentArgs by navArgs()
        val userId = args.userId
        viewModel.sendIntent(ProfileIntent.GetUserData(userId))
    }

    private fun setupViewStateListener() {
        lifecycleScope.launch {
            viewModel.viewState.collect {
                updateLoading(it.isBlockingLoading)
                updateUserName(it.profileUserInfo.firstName)
                updateUserEmail(it.profileUserInfo.email)
                updateUserWebSite(it.profileUserInfo.webSite)
                updateUserAvatar(it.profileUserInfo.avatar)

            }
        }

    }

    private fun updateLoading(isblockingLoading: Boolean) {
        binding.progressBar.visibility = if (isblockingLoading) View.VISIBLE else View.GONE
    }

    private fun updateUserEmail(email: String) {
        binding.userEmail.text = email
    }

    private fun updateUserAvatar(avatar: Bitmap?) {
        if (avatar == null) {
            binding.userAvatar.visibility = View.INVISIBLE
        } else {
            binding.userAvatar.visibility = View.VISIBLE
            binding.userAvatar.setImageBitmap(avatar)

        }
    }

    private fun updateUserWebSite(webSite: String?) {
        if (webSite.isNullOrBlank()) {
            binding.userWebSite.visibility = View.GONE
        } else {
            binding.userWebSite.text = webSite
            binding.userWebSite.visibility = View.VISIBLE
        }
    }

    private fun updateUserName(userName: String?) {
        if (userName.isNullOrBlank()) {
            binding.profileName.visibility = View.INVISIBLE
            binding.userName.visibility = View.INVISIBLE
        } else {
            binding.profileName.visibility = View.VISIBLE
            binding.userName.visibility = View.VISIBLE
            binding.profileName.text = String.format(Locale.getDefault(), getString(R.string.profile_name), userName)
            binding.userName.text = userName
        }
    }
}

