package com.stesso.android.lib

import io.reactivex.subjects.PublishSubject

object RxBus{
    val subject = PublishSubject.create<Event>()


    data class Event(val code:Int,val message:String)

    fun post(event:Event){
        subject.onNext(event)
    }

}