package com.books.app.di

import com.books.app.util.Constants.FETCH_INTERVAL
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class FirebaseConfigManager {
    fun initFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(FETCH_INTERVAL)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        return remoteConfig
    }
}
