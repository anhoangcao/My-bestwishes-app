package com.example.mywish.fragments

import android.os.Bundle
import com.example.mywish.apis.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywish.R
import com.example.mywish.adapters.WishAdapter
import com.example.mywish.sharedpreferences.AppSharedPreferences
import com.example.mywish.databinding.FragmentWishListBinding
import com.example.mywish.models.RequestDeleteWish
import com.example.mywish.models.Wish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishListFragment : Fragment() {
    private lateinit var binding: FragmentWishListBinding
    private lateinit var mWishList: ArrayList<Wish>
    private lateinit var mWishAdapter: WishAdapter
    private lateinit var mAppSharedPreferences: AppSharedPreferences
    private var idUser = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentWishListBinding.inflate(layoutInflater, container, false)
        // Khoi tao mAppSharedPreferences va lay ra idUser
        mAppSharedPreferences = AppSharedPreferences(requireActivity())
        idUser = mAppSharedPreferences.getIdUser("idUser").toString()

        // Khoi tao mWishList
        mWishList = ArrayList()

        // Thuc hien call api lay danh sach dieu uoc
        getWishList()

        binding.btnAdd.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, AddFragment())
                .commit()
        }
        return binding.root
    }

    private fun getWishList() {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val response = Constants.getInstance().getWishList().body()
                if(response != null) {
                    mWishList.addAll(response)
                    // Khoi tao adapter va thiet lap giao dien
                    initAdapterAndSetLayout()
                }
            }
        }
    }

    private fun initAdapterAndSetLayout() {
        if(context == null) {
            return
        }
        mWishAdapter = WishAdapter(requireActivity(), mWishList, mAppSharedPreferences,
            object : WishAdapter.IClickItemWish {
                override fun onClickUpdate(idWish: String, fullName: String, content: String) {
                    // Luu thong tin dieu uoc vao  mAppSharedPreferences va chuyen vao man hinh cap nhat dieu uoc
                    mAppSharedPreferences.putWish("idWish", idWish)
                    mAppSharedPreferences.putWish("fullName", fullName)
                    mAppSharedPreferences.putWish("content", content)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, UpdateFragment())
                        .commit()
                }

                override fun onClickRemove(idWish: String) {
                    // Thuc hien call api xoa dieu uoc
                    deleteWish(idWish)
                }
            })
        binding.rcvWishList.adapter = mWishAdapter
        binding.rcvWishList.layoutManager = LinearLayoutManager(requireActivity())
        binding.progressBar.visibility = View.GONE
    }

    private fun deleteWish(idWish: String) {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val response = Constants.getInstance().deleteWish(RequestDeleteWish(idUser, idWish))
                    .body()
                if(response != null) {
                    if(response.success) {
                        binding.progressBar.visibility = View.GONE
                        // Xoa dieu uoc thanh cong
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                        // Load lai man hinh WishList
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, WishListFragment())
                            .commit()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        // Xoa dieu uoc khong thanh cong
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