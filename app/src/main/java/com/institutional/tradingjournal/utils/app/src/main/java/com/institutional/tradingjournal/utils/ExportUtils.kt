package com.institutional.tradingjournal.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import java.io.File

object ExportUtils {

    fun exportData(context: Context, data: List<Any>, fileName: String = "Orderflow_Backup.json") {
        try {
            val json = Gson().toJson(data)
            val file = File(context.getExternalFilesDir(null), fileName)
            file.writeText(json)
            Toast.makeText(context, "Backup Saved Successfully:\n${file.name}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Export Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun importData(context: Context, fileName: String = "Orderflow_Backup.json"): String? {
        return try {
            val file = File(context.getExternalFilesDir(null), fileName)
            if (file.exists()) {
                val content = file.readText()
                Toast.makeText(context, "Backup Loaded Successfully!", Toast.LENGTH_SHORT).show()
                content
            } else {
                Toast.makeText(context, "No backup file found to import", Toast.LENGTH_SHORT).show()
                null
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Import Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }
}
