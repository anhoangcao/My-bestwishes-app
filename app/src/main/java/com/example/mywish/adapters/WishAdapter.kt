package com.example.mywish.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mywish.R
import com.example.mywish.databinding.ItemWishBinding
import com.example.mywish.models.Wish
import com.example.mywish.sharedpreferences.AppSharedPreferences

class WishAdapter(
    private val context: Context,
    private val wishList: List<Wish>,
    private val appSharedPreferences: AppSharedPreferences,
    private val iclickItemWish: IClickItemWish // Fix the interface name
) : RecyclerView.Adapter<WishAdapter.WishViewHolder>() {

    class WishViewHolder(val binding: ItemWishBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        return WishViewHolder(
            ItemWishBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return wishList.size
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        val wish: Wish = wishList[position]
        holder.binding.tvName.text = wish.name
        holder.binding.tvContent.text = wish.content
        Glide.with(context).load(wish.owner.avatar)
            .error(R.drawable.avt)
            .into(holder.binding.imgAvatar)

        if (appSharedPreferences.getIdUser("idUser").toString() == wish.owner._id) {
            holder.binding.imgUpdate.visibility = View.VISIBLE
            holder.binding.imgDelete.visibility = View.VISIBLE
        }

        holder.binding.imgDelete.setOnClickListener {
            iclickItemWish.onClickRemove(wish._id)
        }

        holder.binding.imgUpdate.setOnClickListener {
            iclickItemWish.onClickUpdate(wish._id, wish.name, wish.content)
        }
    }

    // Define the correct interface name
    interface IClickItemWish {
        fun onClickUpdate(idwish: String, fullName: String, content: String)
        fun onClickRemove(idwish: String)
    }
}
