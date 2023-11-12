package com.example.mywish.models

import com.google.gson.annotations.SerializedName

data class RequestAddWish(
    val idUser: String,
    // SerializedName Truyen tham so len server
    @SerializedName("name")
    val fullName: String,
    val content: String
)
