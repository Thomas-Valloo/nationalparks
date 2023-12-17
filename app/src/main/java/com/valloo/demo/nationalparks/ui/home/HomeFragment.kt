package com.valloo.demo.nationalparks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.databinding.FragmentHomeBinding
import com.valloo.demo.nationalparks.work.DownloadParksWorker.Companion.WORK_DATA_KEY_MAX_PROGRESS
import com.valloo.demo.nationalparks.work.DownloadParksWorker.Companion.WORK_DATA_KEY_PROGRESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_settings -> {
                        findNavController().navigate(R.id.navigation_settings)
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.seeParksButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToParkList()
            )
        }

        homeViewModel.progress.observe(viewLifecycleOwner) {
            when (it?.state) {
                null,
                WorkInfo.State.SUCCEEDED,
                WorkInfo.State.FAILED,
                WorkInfo.State.CANCELLED -> {
                    // No worker is running to download parks data, access to the list is available
                    binding.seeParksButton.visibility = View.VISIBLE

                    binding.downloadProgressIndicator.visibility = View.GONE
                }

                WorkInfo.State.ENQUEUED,
                WorkInfo.State.BLOCKED,
                WorkInfo.State.RUNNING -> {
                    // A worker is running or about to run, access to the list is NOT available and progress
                    // is displayed.
                    binding.seeParksButton.visibility = View.GONE

                    val progress = it.progress.getInt(WORK_DATA_KEY_PROGRESS, 0)

                    binding.downloadProgressIndicator.visibility = View.VISIBLE
                    if (progress == 0) {
                        binding.downloadProgressIndicator.isIndeterminate = true
                    } else {
                        binding.downloadProgressIndicator.isIndeterminate = false
                        binding.downloadProgressIndicator.progress = progress
                        binding.downloadProgressIndicator.max = it.progress.getInt(
                            WORK_DATA_KEY_MAX_PROGRESS, 0
                        )
                    }

                }
            }
        }

        binding.seeEventsButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToEventViewPager()
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}