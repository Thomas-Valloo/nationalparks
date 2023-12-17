package com.valloo.demo.nationalparks.ui.park.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.valloo.demo.nationalparks.databinding.FragmentParklistBinding
import com.valloo.demo.nationalparks.ui.widget.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable


@AndroidEntryPoint
class ParkListFragment : Fragment() {

    private val parkListViewModel: ParkListViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    private var _binding: FragmentParklistBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ParkListDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ParkListFragment", "onCreateView")
        _binding = FragmentParklistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ParkListDataAdapter()
        adapter.addLoadStateListener { loadStates ->
            if (_binding == null) return@addLoadStateListener

            binding.searchProgressIndicator.visibility =
                if (loadStates.refresh is LoadState.Loading && adapter.itemCount < 1) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            binding.errorLayout.root.visibility =
                if (loadStates.refresh is LoadState.Error) View.VISIBLE else View.GONE

            binding.noResultsLayout.root.visibility =
                if (loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && adapter.itemCount < 1) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        val layoutManager = LinearLayoutManager(this.context)
        binding.parksRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(8)
        binding.parksRecyclerView.addItemDecoration(dividerItemDecoration)

        binding.parksRecyclerView.adapter = adapter

        val disposable = parkListViewModel.getParkList()
            .subscribe(
                {
                    adapter.submitData(lifecycle, it)
                },
                {
                    binding.searchProgressIndicator.visibility = View.GONE
                    binding.errorLayout.root.visibility = View.VISIBLE
                })
        compositeDisposable.add(disposable)

        return root
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        _binding = null

        super.onDestroyView()
    }
}