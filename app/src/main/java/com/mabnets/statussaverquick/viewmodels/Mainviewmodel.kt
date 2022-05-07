package com.mabnets.statussaverquick.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mabnets.statussaverquick.BuildConfig
import com.mabnets.statussaverquick.models.STATUS_TYPE
import com.mabnets.statussaverquick.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class Mainviewmodel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val _statusList = MutableLiveData<List<Status>>()
    val statusList: LiveData<List<Status>>
        get() = _statusList
    val context: Context = context
    fun getallstatuscontentz(typ: String) {
        val mainPath: String? = context.getExternalFilesDir(null)?.absolutePath
        if (mainPath != null) {
            val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                    + File.separator + "files")
            val validPath = mainPath.replace(extraPortion, "")
            //val mstring=if (Build.VERSION.SDK_INT <= 30) "WhatsApp/Media/.Statuses" else "Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
            val oldpath = "WhatsApp/Media/.Statuses"
            val newpath = "Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
            val savingpath = "WhatsAppStatusaved"
            val oldstatusPath = validPath + oldpath
            val newstatusPath = validPath + newpath
            val savingstatusPath = validPath + savingpath
            val newvalidFile = File(newstatusPath)
            val validFile = if (!typ.equals("all")) {
                if (newvalidFile.exists()) {
                    File(newstatusPath)
                } else {
                    File(oldstatusPath)
                }
            } else {
                File(savingstatusPath)
            }
            if (validFile.listFiles() != null) {
                val files: MutableList<File> = validFile.listFiles().toMutableList()
                //files.filter { it.extension == "jpg" }
                files.sortByDescending { it.lastModified() }
                val statusList: MutableList<Status> = mutableListOf()
                files.iterator().forEach {
                    var extension: STATUS_TYPE? = null;
                    if (it.extension == "jpg" && typ.equals("jpg")) {
                        extension = STATUS_TYPE.IMAGE;
                    } else if (it.extension == "mp4" && typ.equals("mp4")) {
                        extension = STATUS_TYPE.VIDEO
                    } else if (typ.equals("all")) {
                        if (it.extension == "jpg") {
                            extension = STATUS_TYPE.IMAGE;
                        }
                        if (it.extension == "mp4") {
                            extension = STATUS_TYPE.VIDEO
                        }
                    }
                    if (extension != null) {
                        statusList.add(
                            Status(
                                it.absolutePath,
                                extension
                            )
                        );
                    }

                }
                _statusList.value = statusList
            } else {
                println("Failing")
            }
        }
    }


}