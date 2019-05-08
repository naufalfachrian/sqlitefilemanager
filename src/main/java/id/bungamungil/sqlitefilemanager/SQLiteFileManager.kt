package id.bungamungil.sqlitefilemanager

import android.content.Context
import id.bungamungil.sqlitefilemanager.callback.SQLiteCopyFromAssetsCallback
import id.bungamungil.sqlitefilemanager.callback.SQLiteReplaceFromAssetsCallback

interface SQLiteFileManager {

    fun copyFromAssets(assetName: String, withContext: Context, callback: SQLiteCopyFromAssetsCallback)

    fun replaceFromAssets(assetName: String, withContext: Context, callback: SQLiteReplaceFromAssetsCallback)

}