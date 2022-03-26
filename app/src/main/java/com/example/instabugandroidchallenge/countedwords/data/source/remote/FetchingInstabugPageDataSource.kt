package com.example.instabugandroidchallenge.countedwords.data.source.remote

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL

class FetchingInstabugPageDataSource {

    fun fetchInstabugPage(instabugAddress: String): String {
        val instabugStream = getInstabugInputStream(instabugAddress)
        return getInstabugContent(instabugStream)
    }

    private fun getInstabugContent(instabugStream: InputStream): String {
        val instabugReader = InputStreamReader(instabugStream, charsetName)
        val instabugBuffer =  BufferedReader(instabugReader)
        var line = instabugBuffer.readLine()
        val respose = StringBuilder()
        while (line != null){
            respose.append(line)
            line = instabugBuffer.readLine()
        }
        instabugReader.close()
        instabugBuffer.close()
        return respose.toString()
    }

    private fun getInstabugInputStream(instabugAddress: String): InputStream {
        val instabugUrl =  URL(instabugAddress)
        val instabugURLConnection = instabugUrl.openConnection()
        return BufferedInputStream(instabugURLConnection.getInputStream())
    }

    companion object{
        const val charsetName = "UTF-8"
    }
}