package com.rpsouza.planner.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val image: String = ""  // base64
) {
    fun isValid(): Boolean =
        name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && image.isNotBlank()
}
