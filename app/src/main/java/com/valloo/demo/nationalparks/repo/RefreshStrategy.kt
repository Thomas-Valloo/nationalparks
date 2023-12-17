package com.valloo.demo.nationalparks.repo

import java.util.concurrent.TimeUnit

/**
 * Strategy to apply to refresh the cache used for data.
 */
enum class RefreshStrategy(val cacheTimeoutInMs: Long) {
    ALWAYS(0),
    MINUTE(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)),
    MINUTES_FIVE(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES)),
    DAY(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)),
    NEVER(Long.MAX_VALUE)
}