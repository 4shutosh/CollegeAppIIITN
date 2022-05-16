package com.college.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.college.app.R
import com.college.app.databinding.FragmentProfileSettingsBinding
import com.college.app.ui.onboarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class ProfileSettingsFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingsBinding

    private val viewModel: ProfileSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        binding.tvLogOut.setOnClickListener {
            viewModel.actionLogOut()
        }
        binding.tvExit.setOnClickListener {
            viewModel.actionExit()
        }
    }

    private fun setUpObservers() {
        viewModel.command.observe(viewLifecycleOwner) {
            processCommand(it)
        }
    }

    private fun processCommand(it: ProfileSettingsViewModel.Command) {
        when (it) {
            ProfileSettingsViewModel.Command.Exit -> {
                requireActivity().finishAffinity()
                exitProcess(0)
            }
            is ProfileSettingsViewModel.Command.LoadInitialData -> {
                binding.ivDp.load(it.userImage) {
                    placeholder(R.drawable.user_image)
                }
                binding.tvUserName.text = it.userName
            }
            ProfileSettingsViewModel.Command.LogOut -> {
                requireActivity().finish()
                requireActivity().startActivity(Intent(requireActivity(),
                    OnBoardingActivity::class.java))
            }
        }
    }

    companion object {
        const val FRAGMENT_PROFILE_SETTINGS_ID = R.layout.fragment_profile_settings
    }
}