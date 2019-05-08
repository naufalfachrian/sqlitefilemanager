package id.bungamungil.sqlitefilemanager.callback

interface SQLiteReplaceFromAssetsCallback {

    fun onSQLiteFileReplaced()

    fun onFailedToReplaceSQLiteFile(reason: Throwable)

}