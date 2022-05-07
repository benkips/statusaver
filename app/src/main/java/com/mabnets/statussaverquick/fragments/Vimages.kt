package com.mabnets.statussaverquick.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mabnets.statussaverquick.BuildConfig
import com.mabnets.statussaverquick.R
import com.mabnets.statussaverquick.adapters.StatusAdapter
import com.mabnets.statussaverquick.databinding.FragmentVimagesBinding
import com.mabnets.statussaverquick.models.Status
import com.mabnets.statussaverquick.viewmodels.Mainviewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File

@AndroidEntryPoint
class Vimages : Fragment(R.layout.fragment_vimages) {
    private var _binding: FragmentVimagesBinding? = null
    private val binding get() = _binding!!
    private  val viewmodel by viewModels<Mainviewmodel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =  FragmentVimagesBinding.bind(view)

        viewmodel.getallstatuscontentz("jpg")

        val spanCount = 2
        val manager = GridLayoutManager(context, spanCount)
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 5 == 0) { //your condition for showing ad
                    2 //replace 3 with the number of items in each row
                } else 1
            }
        }
        viewmodel.statusList.observe(viewLifecycleOwner, Observer {
            //Toast.makeText(context, it.size.toString(), Toast.LENGTH_SHORT).show()
            showStatus(it.size)
            binding.recyclerView.also { rv ->
                rv.layoutManager = manager
                rv.setHasFixedSize(true)
                rv.adapter= StatusAdapter(it.toMutableList(),requireContext(),R.id.vimages,getMainPath())
            }

        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

}