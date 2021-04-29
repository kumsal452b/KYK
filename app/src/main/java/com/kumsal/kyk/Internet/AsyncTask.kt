package com.kumsal.kyk.Internet

import android.os.AsyncTask
import java.io.InputStream
import java.net.URL

class AsyncTask: AsyncTask<String, Void, InputStream>() {
    override fun doInBackground(vararg params: String?): InputStream {
        var url= URL(params[0])
        var connection=url.openConnection()
        connection.connect()
        return connection.getInputStream()
    }
}