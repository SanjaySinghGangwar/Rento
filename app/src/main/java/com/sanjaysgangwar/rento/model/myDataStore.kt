package com.sanjaysgangwar.rento.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.sanjaysgangwar.rento.R
import kotlinx.coroutines.flow.first

class myDataStore(context: Context) {

    private var dataStore: DataStore<Preferences>

    init {
        dataStore = context.createDataStore(name = context.resources.getString(R.string.app_name))
    }

    public suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    public suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}
