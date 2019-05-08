package id.bungamungil.sqlitefilemanager.callback

interface SQLiteCopyFromAssetsCallback {

    fun onSQLiteFileExisted()

    fun onSQLiteFileCopied()

    fun onFailedToCopySQLiteFromAssets(reason: Throwable)

}