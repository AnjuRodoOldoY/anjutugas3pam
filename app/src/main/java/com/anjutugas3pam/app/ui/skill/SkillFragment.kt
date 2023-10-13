package com.anjutugas3pam.app.ui.skill

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anjutugas3pam.app.databinding.FragmentSkillBinding
import com.anjutugas3pam.app.R

class SkillFragment : Fragment() {

    private var _binding: FragmentSkillBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val data = ArrayList<itemsViewModel>()
    val adapter = CustomAdapter(data)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val skillViewModel =
            ViewModelProvider(this).get(SkillViewModel::class.java)

        _binding = FragmentSkillBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textforskill

        val recyclerview: RecyclerView = root.findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val dataSkill = resources.getStringArray(R.array.daftar_skill)
        val pengalamanSkill = resources.getIntArray(R.array.pengalaman_skill)
        for(i in 0 until dataSkill.size){
            val tampilSkill = dataSkill.get(i).toString()
            val tampilPengalaman = pengalamanSkill.get(i).toString()
            data.add(itemsViewModel(tampilSkill, "$tampilPengalaman Tahun"))
        }
        val searchviewID : SearchView = root.findViewById(R.id.searchViewID)
        searchviewID.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
        recyclerview.adapter = adapter
        skillViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        adapter.notifyDataSetChanged()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filter(text : String){
        val filteredlist : ArrayList<itemsViewModel> = ArrayList()
        for(item in data){
            if(item.nama.toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(item)
            }
        }
        if(filteredlist.isEmpty()){
            Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(requireContext(), "Data found", Toast.LENGTH_SHORT).show()
            activity?.runOnUiThread{
                adapter.filterList(filteredlist)
            }
        }
    }

}