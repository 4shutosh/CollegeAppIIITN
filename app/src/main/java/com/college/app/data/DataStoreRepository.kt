package com.college.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DataStoreRepository {
    suspend fun setUserId(userId: Long)
    suspend fun getUserId(): Long?

    fun getAccessToken(): String?
    suspend fun setAccessToken(token: String?)
}

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : DataStoreRepository {

    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_PREFERENCES)
    private val dataStore: DataStore<Preferences> = context._dataStore

    override suspend fun setUserId(userId: Long) {
        dataStore.edit { it[USER_KEY] = userId }
    }

    override suspend fun getUserId(): Long? {
        return dataStore.data.map { it[USER_KEY] }.firstOrNull()
    }

    override fun getAccessToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setAccessToken(token: String?) {
        TODO("Not yet implemented")
    }

    companion object {
        const val APP_PREFERENCES = "college_app_preferences"

        val USER_KEY = longPreferencesKey("user_id_key")
    }

}