package com.stesso.android

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.di.component.FragmentComponent
import com.stesso.android.di.module.FragmentModule
import com.stesso.android.model.RootNode
import com.stesso.android.utils.applySingleSchedulers
import com.stesso.android.utils.toast
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BaseFragment : Fragment() {

    private val disposableContainer = CompositeDisposable()
    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)

    }

    protected open fun getLayoutId() = R.layout.fragment_mine


    protected fun getFragmentComponent(): FragmentComponent {
        return App.instance().component.plus(FragmentModule())
    }

    protected fun <T> doHttpRequest(single: Single<RootNode<T>>, onSuccess: (T?) -> Unit) {
        val disposable = single.compose(applySingleSchedulers())
                .subscribe({ rootNode ->
                    if (rootNode.errno != 0) {
                        context?.toast(rootNode.errmsg ?: "")
                    } else {
                        onSuccess(rootNode.data)
                    }
                }, {
                    it.printStackTrace()
                })
        disposableContainer.add(disposable)
    }

    override fun onDestroy() {
        disposableContainer.dispose()
        super.onDestroy()
    }

}
