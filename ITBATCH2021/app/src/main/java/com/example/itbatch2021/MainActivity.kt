package com.example.itbatch2021

import android.os.Bundle
import com.example.itbatch2021.databinding.ContentMainBinding
import com.example.itbatch2021.databinding.MainactBinding

class MainActivity : Navigation(){
   lateinit var  mainactBinding: MainactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainactBinding = MainactBinding.inflate(layoutInflater)
        setContentView(mainactBinding.root)
        setTitle("Home")
    }


}
