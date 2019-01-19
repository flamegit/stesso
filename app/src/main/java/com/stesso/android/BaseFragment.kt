package com.stesso.android

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.di.component.FragmentComponent
import com.stesso.android.di.module.FragmentModule
import javax.inject.Inject

open class BaseFragment : Fragment() {

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    protected fun getLayoutId() = R.layout.fragment_mine

    protected fun getActivityComponent(): FragmentComponent {
        return App.instance().component.plus(FragmentModule())
    }

}
