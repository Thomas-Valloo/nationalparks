package com.valloo.demo.nationalparks.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.valloo.demo.nationalparks.databinding.FragmentEventViewpagerBinding
import com.valloo.demo.nationalparks.repo.USState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventViewPagerFragment : Fragment() {

    private lateinit var eventViewPagerAdapter: EventViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    private var _binding: FragmentEventViewpagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventViewpagerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        eventViewPagerAdapter = EventViewPagerAdapter(this)

        viewPager = binding.viewpager
        viewPager.adapter = eventViewPagerAdapter

        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = USState.values()[position].stateName
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


