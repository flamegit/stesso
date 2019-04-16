package com.stesso.android.model

data class ExpressInfo(val logisticCode: String, val shipperName: String, val trace: List<Trace>) {

    data class Trace(val acceptStation: String, val acceptTime: String)
}
