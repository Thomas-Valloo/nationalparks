package com.valloo.demo.nationalparks.ui.park.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.valloo.demo.nationalparks.R
import com.valloo.demo.nationalparks.infra.db.entity.ImageDataEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkActivityEntity
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity
import com.valloo.demo.nationalparks.repo.park.ParkRepository
import com.valloo.demo.nationalparks.ui.LiveDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class ParkDetailViewModel
@Inject
constructor(private val parkRepository: ParkRepository, state: SavedStateHandle) : ViewModel() {

    private val _park = MutableLiveData<LiveDataState<ParkEntity>>()
    val park: LiveData<LiveDataState<ParkEntity>>
        get() = _park

    private val id = state.get<String>("id")!!

    private var disposable =
        parkRepository
            .getParkEntity(id)
            .doOnSubscribe { _park.postValue(LiveDataState.Loading()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _park.postValue(LiveDataState.Success(it)) },
                {
                    // Note : error message could be different depending on the exception.
                    _park.postValue(LiveDataState.Error(R.string.error_generic_label))
                }
            )

    fun getParkImages(): Single<List<ImageDataEntity>> {
        return parkRepository.getParkImages(id)
    }

    fun getParkActivities(): Single<List<ParkActivityEntity>> {
        return parkRepository.getParkActivities(id)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
