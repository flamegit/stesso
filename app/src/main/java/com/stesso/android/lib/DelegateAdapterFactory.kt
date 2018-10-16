package com.example.flame.kotlinstudy.lib

const val HEADER = 1
const val FOOTER = 2
const val TYPE1 = 3
const val GIRL_TYPE = 4

class DelegateAdapterFactory {

    fun getDelegateAdapter(type: Int): ViewTypeDelegateAdapter {
        when (type) {
            HEADER -> return HeaderDelegateAdapter()
            FOOTER -> return FooterDelegateAdapter()
            TYPE1 -> return HeaderDelegateAdapter()
        }
        return DefaultDelegateAdapter()
    }

}