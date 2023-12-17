package com.valloo.demo.nationalparks.ui.park.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.snackbar.Snackbar
import com.valloo.demo.nationalparks.databinding.FragmentParkdetailBinding
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity
import com.valloo.demo.nationalparks.ui.LiveDataState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable


@AndroidEntryPoint
class ParkDetailFragment : Fragment() {

    private var _binding: FragmentParkdetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var imagesAdapter: ImageDataAdapter
    private lateinit var parkActivitiesAdapter: ParkActivityDataAdapter

    private val parkDetailViewModel: ParkDetailViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParkdetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        parkDetailViewModel.park.observe(viewLifecycleOwner) { liveDataState: LiveDataState<ParkEntity> ->
            when (liveDataState) {
                is LiveDataState.Loading -> binding.progressIndicator.visibility = View.VISIBLE

                is LiveDataState.Success -> {
                    binding.progressIndicator.visibility = View.GONE

                    val park = liveDataState.data
                    (requireActivity() as AppCompatActivity).supportActionBar?.title = park.name

                    binding.park = park
                }

                is LiveDataState.Error -> {
                    Snackbar.make(binding.root, liveDataState.errorMessageId, Snackbar.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }
            }
        }

        // Images
        imagesAdapter = ImageDataAdapter(emptyList())
        binding.imagesRecyclerView.apply {
            this.layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(this)
            this.adapter = imagesAdapter
        }
        val imagesDisposable = parkDetailViewModel.getParkImages().subscribe(
            {
                imagesAdapter.updateDataset(it)
            },
            {
                binding.imagesRecyclerView.visibility = View.GONE
                binding.imagesErrorLayout.root.visibility = View.VISIBLE
            })
        compositeDisposable.add(imagesDisposable)

        // Activities
        parkActivitiesAdapter = ParkActivityDataAdapter(emptyList())
        binding.parkActivitiesRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = parkActivitiesAdapter
        }
        val activitiesDisposable = parkDetailViewModel.getParkActivities().subscribe(
            {
                parkActivitiesAdapter.updateDataset(it)
            },
            {
                binding.parkActivitiesRecyclerView.visibility = View.GONE
                binding.parkActivitiesErrorLayout.root.visibility = View.VISIBLE
            })
        compositeDisposable.add(activitiesDisposable)

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}