package com.valloo.demo.nationalparks.infra

import com.valloo.demo.nationalparks.repo.RefreshStrategy

interface PreferencesStore {
    /**
     * @return 'true' if a delay should be added for debug purposes.
     *      The download worker will then be slower and easier to observe.
     */
    fun getAddDelayToDebug(): Boolean

    /**
     * @return the timestamp of the last update. '0' if there is none.
     */
    fun getParksLastUpdate(): Long

    /**
     * @param timestamp Timestamp of the last data download.
     */
    fun setParksLastUpdate(timestamp: Long)

    /**
     * @return the strategy to adopt for the cache timeout.
     */
    fun getRefreshStrategy(): RefreshStrategy
}