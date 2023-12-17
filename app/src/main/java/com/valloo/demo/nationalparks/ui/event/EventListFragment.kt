package com.valloo.demo.nationalparks.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.valloo.demo.nationalparks.databinding.FragmentEvenlistBinding
import com.valloo.demo.nationalparks.ui.widget.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable


@AndroidEntryPoint
class EventListFragment : Fragment() {
    private val eventListViewModel: EventListViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    private var _binding: FragmentEvenlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventListDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEvenlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = EventListDataAdapter()
        adapter.addLoadStateListener { loadStates ->
            binding.searchProgressIndicator.visibility =
                if (loadStates.refresh is LoadState.Loading) VISIBLE else GONE

            binding.errorLayout.root.visibility =
                if (loadStates.refresh is LoadState.Error) VISIBLE else GONE

            val databaseNotLoading = loadStates.source.refresh is LoadState.NotLoading
            val endOfPaginationReached = loadStates.append.endOfPaginationReached
            val adapterIsEmpty = adapter.itemCount < 1
            binding.noResultsLayout.root.visibility =
                if (databaseNotLoading && endOfPaginationReached && adapterIsEmpty) {
                    VISIBLE
                } else {
                    GONE
                }
        }

        val layoutManager = LinearLayoutManager(this.context)

        binding.eventsRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(8)
        binding.eventsRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.eventsRecyclerView.adapter = adapter


        val disposable = eventListViewModel.getEventsList().subscribe(
            {
                adapter.submitData(lifecycle, it)
            },
            {
                binding.searchProgressIndicator.visibility = GONE
                binding.errorLayout.root.visibility = VISIBLE
            })
        compositeDisposable.add(disposable)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.dispose()
    }
}