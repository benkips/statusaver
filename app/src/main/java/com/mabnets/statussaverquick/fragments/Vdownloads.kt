package com.mabnets.statussaverquick.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mabnets.statussaverquick.BuildConfig
import com.mabnets.statussaverquick.R
import com.mabnets.statussaverquick.adapters.StatusAdapter
import com.mabnets.statussaverquick.databinding.FragmentVdownloadsBinding
import com.mabnets.statussaverquick.viewmodels.Mainviewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File




@AndroidEntryPoint
class Vdownloads : Fragment(R.layout.fragment_vdownloads) {
    private var _binding: FragmentVdownloadsBinding? = null
    private val binding get() = _binding!!
    private  val viewmodel by viewModels<Mainviewmodel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =  FragmentVdownloadsBinding.bind(view)
        cretingnewfilestorage()
        viewmodel.getallstatuscontentz("all")
        val spanCount = 2
        val manager = GridLayoutManager(context, spanCount)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 5 == 0) { //your condition for showing ad
                    2 //replace 3 with the number of items in each row
                } else 1
            }
        }
        viewmodel.statusList.observe(viewLifecycleOwner, Observer {
            showStatus(it.size)
            binding.recyclerView.also { rv ->
                rv.layoutManager = manager
                rv.setHasFixedSize(true)
                rv.adapter= StatusAdapter(it.toMutableList(),requireContext(),
                    R.id.vdownloads2,getMainPath())
            }

        })


    }

    private fun getMainPath(): String? {
        if(context!=null) {
            val mainPath: String? = requireContext().getExternalFilesDir(null)?.absolutePath
            if (mainPath != null) {
                val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                        + File.separator + "files")
                return mainPath.replace(extraPortion, "")
            }
        }
        return null;
    }
    private fun showStatus(value: Int) {
        binding.textViewStatus.text= value.toString() + " status found"
        if(value == 0) {
            binding.frameEmptyStatus.visibility = View.VISIBLE
        }
        else {
            binding.frameEmptyStatus.visibility = View.GONE

        }
    }
    private fun cretingnewfilestorage(){
        val mainPath: String? = context?.getExternalFilesDir(null)?.absolutePath
        if (mainPath != null) {
            val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                    + File.separator + "files")
            val validPath = mainPath.replace(extraPortion, "")
            val folderSaved: File = File(validPath + "WhatsAppStatusaved")
            if(!folderSaved.exists()) {
                folderSaved.mkdirs()
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}