package com.example.sayit.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.sayit.data.api.ApiService
import com.example.sayit.database.RemoteKeys
import com.example.sayit.database.WordDatabase
import com.example.sayit.model.WordItem
import com.example.sayit.utils.DataMapper

@OptIn(ExperimentalPagingApi::class)
class WordRemoteMediator (
    val token: String,
    val database: WordDatabase,
    private val apiService: ApiService,
): RemoteMediator<Int, WordItem>(){

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WordItem>
    ): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosititon(state)
                remoteKeys?.nextKey?.minus(1) ?: INITITAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?:
                return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?:
                return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getWords("Bearer $token", page, state.config.pageSize)
//            Log.d("INI RESPONSE COK", responseData.word.toString())
            val wordDomain = DataMapper.getWordsDataClassed(responseData)
//            Log.d("INI WORD DOMAIN COK", wordDomain.toString())
            val endOfPaginationReached = wordDomain.isEmpty()


            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.wordDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = wordDomain.map {
                    RemoteKeys(id = it.id.toString(), prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.wordDao().insertWord(wordDomain)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            Log.e("RemoteMediator", "Error loading data: ${exception.message}", exception)
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, WordItem>): RemoteKeys? {
        return state.pages.lastOrNull {it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, WordItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosititon(state: PagingState<Int, WordItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    companion object {
        const val INITITAL_PAGE_INDEX = 1
    }

}