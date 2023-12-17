package com.valloo.demo.nationalparks.infra

import android.content.Context
import androidx.preference.PreferenceManager
import com.valloo.demo.nationalparks.repo.RefreshStrategy
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesStoreImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesStore {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getAddDelayToDebug(): Boolean {
        return sharedPreferences.getBoolean(KEY_ADD_DELAY_TO_DEBUG, false)
    }

    override fun getParksLastUpdate(): Long {
        return sharedPreferences.getLong(KEY_LAST_UPDATE, 0)
    }

    override fun setParksLastUpdate(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(KEY_LAST_UPDATE, timestamp)
            .apply()
    }

    override fun getRefreshStrategy(): RefreshStrategy {
        val value = sharedPreferences.getString(KEY_REFRESH_STRATEGY, RefreshStrategy.NEVER.name)
        return RefreshStrategy.valueOf(value!!)
    }

    companion object {
        const val KEY_ADD_DELAY_TO_DEBUG = "addDelayToDebug"
        const val KEY_LAST_UPDATE = "lastUpdate"
        const val KEY_REFRESH_STRATEGY = "refreshStrategy"
        const val KEY_SYNCHRONIZE_SERVER = "synchronizeServer"
    }
}