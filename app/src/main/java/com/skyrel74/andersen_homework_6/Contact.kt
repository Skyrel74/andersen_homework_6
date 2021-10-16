package com.skyrel74.andersen_homework_6

import java.io.Serializable

data class Contact(
    val id: Int,
    var imageUrl: String,
    var name: String,
    var surname: String,
    var phone: String
) : Serializable