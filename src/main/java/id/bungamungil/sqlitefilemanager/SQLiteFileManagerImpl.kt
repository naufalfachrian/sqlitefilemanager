package id.bungamungil.sqlitefilemanager

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import id.bungamungil.sqlitefilemanager.callback.SQLiteCopyFromAssetsCallback
import id.bungamungil.sqlitefilemanager.callback.SQLiteReplaceFromAssetsCallback
import java.io.File
import java.io.FileOutputStream

class SQLiteFileManagerImpl : SQLiteFileManager {

    override fun copyFromAssets(assetName: String, withContext: Context, callback: SQLiteCopyFromAssetsCallback) {
        try {
            if (databaseFileExisted(determineFilePath(assetName, withContext))) {
                callback.onSQLiteFileExisted()
                return
            }
            copyFileFromAssets(assetName, withContext)
            callback.onSQLiteFileCopied()
        } catch (reason: Throwable) {
            callback.onFailedToCopySQLiteFromAssets(reason)
        }
    }

    override fun replaceFromAssets(assetName: String, withContext: Context, callback: SQLiteReplaceFromAssetsCallback) {
        try {
            removeFile(determineFilePath(assetName, withContext))
            copyFileFromAssets(assetName, withContext)
            callback.onSQLiteFileReplaced()
        } catch (reason: Throwable) {
            callback.onFailedToReplaceSQLiteFile(reason)
        }
    }

    private fun databaseFileExisted(onPath: String): Boolean {
        var db: SQLiteDatabase? = null
        try {
            if (fileExisted(onPath)) {
                db = SQLiteDatabase.openDatabase(onPath, null, SQLiteDatabase.OPEN_READONLY)
            }
        } catch (reason: SQLiteException) {
            throw RuntimeException("Failed to check database exists because ${reason.message}.")
        }
        db?.close()
        return db != null
    }

    private fun fileExisted(onPath: String): Boolean {
        val file = File(onPath)
        return file.exists() && !file.isDirectory
    }

    private fun removeFile(onPath: String): Boolean {
        return File(onPath).delete()
    }

    private fun copyFileFromAssets(assetName: String, withContext: Context) {
        try {
            val inputStream = withContext.assets.open(assetName)
            val outputFilePath = determineFilePath(assetName, withContext)
            val outputStream = FileOutputStream(outputFilePath)
            val buffer = ByteArray(1024)
            var length = inputStream.read(buffer)
            while (length > 0) {
                outputStream.write(buffer, 0, length)
                length = inputStream.read(buffer)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (reason: Throwable) {
            throw RuntimeException("Failed to copy file from assets because ${reason.message}")
        }
    }

    private fun determineFilePath(fromAssetName: String, withContext: Context): String {
        try {
            val outputDirPath = withContext.getExternalFilesDir(null)?.absolutePath
                    ?: throw RuntimeException("Output directory is null")
            return "$outputDirPath/$fromAssetName"
        } catch (reason: Throwable) {
            throw RuntimeException("Failed to determine file path because ${reason.message}")
        }
    }

}