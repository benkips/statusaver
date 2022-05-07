package com.mabnets.statussaverquick.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.mabnets.statussaverquick.R
import com.mabnets.statussaverquick.databinding.StatusItemViewBinding
import com.mabnets.statussaverquick.models.STATUS_TYPE
import com.mabnets.statussaverquick.models.Status
import java.io.*
import java.util.*

class StatusAdapter(
    private val dataSets: MutableList<Status>,
    private val context: Context,
    private val targetId: Int,
    private val mainPath: String?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val AD_TYPE = 1
    private val DEFAULT_VIEW_TYPE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        if (viewType == AD_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.banner_ad,
                parent,
                false
            )
            return adholderc(view)

        }else {
            val binding =
                StatusItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StatusViewHolder(binding)
        }
    }


    override fun getItemCount(): Int {
        var itemCount: Int = dataSets.size
        itemCount += itemCount / 5
        return itemCount
    }


    override fun getItemViewType(position: Int): Int {
        return if (position > 1 && position % 5 == 0) {
            AD_TYPE
        } else DEFAULT_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is StatusViewHolder) {
            return
        }
        val itemPosition = position - position / 5
        val currentitem=dataSets[itemPosition];
        if(currentitem!=null){
            holder.bind(currentitem)
        }

    }


    inner class StatusViewHolder(val binding: StatusItemViewBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(statusData: Status) {
            binding.apply {
                Glide.with(itemView)
                    .load(statusData.path)
                    .into(imageView)
            }
            if (statusData.type == STATUS_TYPE.VIDEO) {
                binding.imageTypeVideo.visibility = View.VISIBLE
            } else {
                binding.imageTypeVideo.visibility = View.GONE
            }
            itemView.setOnClickListener {
                view(File(statusData.path), statusData.type)
            }
            if (targetId == R.id.vdownloads2) {
                binding.fabDownload.visibility = View.GONE
                binding.fabDelete.visibility = View.VISIBLE
            } else {
                binding.fabDownload.visibility = View.VISIBLE
                binding.fabDelete.visibility = View.GONE
            }

            binding.fabShare.setOnClickListener {
                shareImage(statusData.path, statusData.type)
            }
            binding.fabDownload.setOnClickListener {
                copyFile(File(statusData.path))
             /*val navcontroller=Navigation.findNavController(it)
                    navcontroller.run {
                       // popBackStack()
                        navigate(
                            R.id.vdownloads2
                        )
              }*/
            }
            binding.fabDelete.setOnClickListener {
                deleteFile(File(statusData.path), position)
            }
        }
    }

    private fun view(file: File, statusType: STATUS_TYPE) {
        var fileData: Uri = FileProvider.getUriForFile(
            context,
            context.packageName, file
        )
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(fileData, if (statusType == STATUS_TYPE.IMAGE) "image/*" else "video/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            shareIntent
        )
    }

    private fun shareImage(uriToImage: String, statusType: STATUS_TYPE) {
        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            context.packageName, File(uriToImage)
        )
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = if (statusType == STATUS_TYPE.IMAGE) "image/jpg" else "video/mp4"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.resources.getText(R.string.send_to)
            )
        )
    }

    private fun deleteFile(file: File, position: Int) {
        if (file.exists()) {
            file.delete()
            Toast.makeText(context, "File has been deleted", Toast.LENGTH_SHORT).show()
            dataSets.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    private fun copyFile(source: File) {
        println("Date before copy::" + Date(System.currentTimeMillis()))
        val dest: File = File(
            mainPath + "WhatsAppStatusaved/" + source.name + "." + source.extension
        )
        if (source.exists()) {
            if (!dest.exists()) {
                val inputStream: InputStream = FileInputStream(source)
                val outputStream: OutputStream = FileOutputStream(dest)
                val buf = ByteArray(1024)
                var len: Int = inputStream.read(buf)
                Thread(Runnable {
                    while (len > 0) {
                        outputStream.write(buf, 0, len)
                        len = inputStream.read(buf)
                    }
                    inputStream.close()
                    outputStream.close()
                    println("Write operation done");
                }).start()

                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Already exits", Toast.LENGTH_SHORT).show()
            }
        }
        println("Date after copy::" + Date(System.currentTimeMillis()))
    }

    inner class adholderc(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var adView:AdView
        private lateinit var linearbanner: LinearLayout

        init {
            adView = AdView(itemView.context)
            linearbanner = itemView.findViewById(R.id.banner_container)
            linearbanner.addView(adView)
            adView.adUnitId = "ca-app-pub-4814079884774543/6008920982"

            adView.adSize = AdSize.SMART_BANNER
            val adRequest = AdRequest
                .Builder()
                .build()
            // Start loading the ad in the background.
            adView.loadAd(adRequest)
        }


    }
}