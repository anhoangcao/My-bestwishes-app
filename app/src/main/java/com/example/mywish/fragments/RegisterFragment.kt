package com.example.mywish.fragments

import android.os.Bundle
import com.example.mywish.apis.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mywish.R
import com.example.mywish.databinding.FragmentRegisterBinding
import com.example.mywish.models.RequestRegisterOrLogin
import com.example.mywish.sharedpreferences.AppSharedPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mAppSharedPreferences: AppSharedPreferences
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        mAppSharedPreferences = AppSharedPreferences(requireContext())

        binding.apply {
            btnRegister.setOnClickListener {
                if(edtUsername.text.isNotEmpty()) {
                    username = edtUsername.text.toString().trim()
                    /** Thực hiện call api đăng ký tài khoản */
                    resgisterUser(username)
                } else {
                    Snackbar.make(it, "Vui lòng nhập mã số sinh viên", Snackbar.LENGTH_LONG).show()
                }
            }
            tvLogin.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, LoginFragment())
                    .commit()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun resgisterUser(username: String) {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    val response = Constants.getInstance().registerUser(RequestRegisterOrLogin(username))
                        .body()
                    if(response != null) {
                        if(response.success) {
                            /** Đăng ký tài khoản thành công */
                            /** Nhận idUser và thực hiện lưu vào sharedPreferences */
                            mAppSharedPreferences.putIdUser("idUser", response.idUser!!)
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, WishListFragment())
                                .commit()
                            progressBar.visibility = View.GONE
                        } else {
                            /** Đăng ký tài khoản thất bại */
                            tvMessage.text = response.message
                            tvMessage.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}