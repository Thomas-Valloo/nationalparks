package com.valloo.demo.nationalparks.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.valloo.demo.nationalparks.repo.USState
import com.valloo.demo.nationalparks.ui.event.EventListViewModel.Companion.ARG_STATE_CODE

class EventViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = USState.values().size

    override fun createFragment(position: Int): Fragment {
        val fragment = EventListFragment()
        val usState = USState.values()[position]
        fragment.arguments = Bundle().apply {
            putString(ARG_STATE_CODE, usState.code)
        }
        return fragment
    }
}

