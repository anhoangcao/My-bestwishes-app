package com.example.mywish.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mywish.R
import com.example.mywish.apis.Constants
import com.example.mywish.databinding.FragmentAddBinding
import com.example.mywish.databinding.FragmentUpdateBinding
import com.example.mywish.models.RequestAddWish
import com.example.mywish.models.RequestUpdateWish
import com.example.mywish.sharedpreferences.AppSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var mAppSharedPreferences: AppSharedPreferences
    private var idUser = ""
    private var idWish = ""
    private var fullName = ""
    private var content = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        // Khoi tao mAppSharedPreferences va lay idUser tu mAppSharedPreference
        mAppSharedPreferences = AppSharedPreferences(requireContext())
        idUser = mAppSharedPreferences.getIdUser("idUser").toString()
        idWish = mAppSharedPreferences.getIdUser("idWish").toString()
        fullName = mAppSharedPreferences.getIdUser("fullName").toString()
        content = mAppSharedPreferences.getIdUser("content").toString()

        // Thiet lap noi dung len giao dien
        binding.edtFullName.setText(fullName)
        binding.edtContent.setText(content)

        binding.apply {
            btnSave.setOnClickListener {
                if(edtFullName.text.isNotEmpty() && edtContent.text.isNotEmpty()) {
                    fullName = edtFullName.text.toString().trim()
                    content = edtContent.text.toString().trim()
                    // Thuc hien call api them dieu uoc
                    updateWish(fullName, content)
                }
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun updateWish(fullName: String, content: String) {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val response = Constants.getInstance()
                    .updateWish(RequestUpdateWish(idUser, idWish, fullName, content))
                    .body()
                if(response != null) {
                    if(response.success) {
                        binding.progressBar.visibility = View.GONE
                        // Cap nhat dieu uoc thanh cong
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, WishListFragment())
                            .commit()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        // Cap nhat dieu uoc khong thanh cong
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, LoginFragment())
                            .commit()
                    }
                }
            }
        }
    }

}