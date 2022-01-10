package com.example.yolc_kotlin.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yolc_kotlin.MainActivity
import com.example.yolc_kotlin.databinding.FragmentProfileBinding

class FragProfile: Fragment(){
    var mainActivity: MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.editProfile.setOnClickListener{mainActivity?.goEditProfile()}

        return binding.root
    }
    override fun onAttach(context: Context){
        super.onAttach(context)
        if(context is MainActivity) mainActivity = context
    }
}

