package com.stesso.android.model

data class SuggestionDTO(val mobile: String, val feedType: Int, val content: String, val orderId: Int, val productIds: String? = "")
