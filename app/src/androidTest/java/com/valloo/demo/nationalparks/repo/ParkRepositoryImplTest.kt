package com.valloo.demo.nationalparks.repo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.valloo.demo.nationalparks.infra.db.AppDatabase
import com.valloo.demo.nationalparks.infra.db.entity.ParkEntity
import com.valloo.demo.nationalparks.infra.http.jsonDto.ParkJsonDto
import com.valloo.demo.nationalparks.repo.park.ParkRepositoryImpl
import org.instancio.Instancio
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParkRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var parkRepository: ParkRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        parkRepository = ParkRepositoryImpl(database)
    }

    /**
     * Saves a [ParkJsonDto] in database, loads it from database and checks equality.
     */
    @Test
    fun saveAndGet() {
        val parkJsonDto = Instancio.create(ParkJsonDto::class.java)
        parkRepository.savePark(parkJsonDto).blockingSubscribe()

        val parkEntityFromJson =
            ParkEntity(
                parkId = parkJsonDto.id,
                description = parkJsonDto.description,
                designation = parkJsonDto.designation,
                fullName = parkJsonDto.fullName,
                name = parkJsonDto.name,
                parkCode = parkJsonDto.parkCode,
                url = parkJsonDto.url,
                weatherInfo = parkJsonDto.weatherInfo
            )

        val parkEntityFromDb = parkRepository.getParkEntity(parkJsonDto.id).blockingGet()
        assertEquals(parkEntityFromJson, parkEntityFromDb)

        val imagesFromDb = parkRepository.getParkImages(parkJsonDto.id).blockingGet()
        assertEquals(parkJsonDto.images.size, imagesFromDb.size)

        val activitiesFromDb = parkRepository.getParkActivities(parkJsonDto.id).blockingGet()
        assertEquals(parkJsonDto.activities.size, activitiesFromDb.size)
    }

    @After
    fun closeDb() {
        database.close()
    }
}
