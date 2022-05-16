package com.college.app.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DataStoreRepository {
    suspend fun setUserId(userId: String)
    suspend fun getUserId(): String?

    suspend fun setUserImageUrl(url: String)
    suspend fun getUserImageUrl(): String?

    suspend fun setUserName(name: String)
    suspend fun getUserName(): String?

    suspend fun setUserEmail(email: String)
    suspend fun getUserEmail(): String?

    suspend fun getAccessToken(): String?
    suspend fun setAccessToken(token: String)

    suspend fun logOut()
}

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : DataStoreRepository {

    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_PREFERENCES)
    private val dataStore: DataStore<Preferences> = context._dataStore


    override suspend fun logOut() {
        dataStore.edit {
            it.clear()
        }
    }

    override suspend fun setUserId(userId: String) {
        dataStore.edit { it[USER_KEY] = userId }
    }

    override suspend fun getUserId(): String? {
        return dataStore.data.map { it[USER_KEY] }.firstOrNull()
    }

    override suspend fun setUserImageUrl(url: String) {
        dataStore.edit { it[USER_IMAGE_URL] = url }
    }

    override suspend fun getUserImageUrl(): String? {
        return dataStore.data.map { it[USER_IMAGE_URL] }.firstOrNull()
    }

    override suspend fun setUserName(name: String) {
        dataStore.edit { it[USER_NAME] = name }
    }

    override suspend fun getUserName(): String? {
        return dataStore.data.map { it[USER_NAME] }.firstOrNull()
    }

    override suspend fun setUserEmail(email: String) {
        dataStore.edit { it[USER_EMAIL] = email }
    }

    override suspend fun getUserEmail(): String? {
        return dataStore.data.map { it[USER_EMAIL] }.firstOrNull()
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.map { it[USER_TOKEN] }.firstOrNull()
    }

    override suspend fun setAccessToken(token: String) {
        dataStore.edit { it[USER_TOKEN] = token }
    }

    companion object {
        const val APP_PREFERENCES = "college_app_preferences"

        val USER_KEY = stringPreferencesKey("user_id_key")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_IMAGE_URL = stringPreferencesKey("user_url")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

}