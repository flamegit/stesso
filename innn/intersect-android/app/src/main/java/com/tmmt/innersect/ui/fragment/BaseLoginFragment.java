package com.tmmt.innersect.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tmmt.innersect.utils.SystemUtil;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/10/8.
 */
public abstract class BaseLoginFragment extends Fragment  implements ViewTreeObserver.OnGlobalLayoutListener{


    private  int mNavigationHeight;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(getLayout(),container,false);
        mNavigationHeight= SystemUtil.getNavigationBarHeight(getContext());
        ButterKnife.bind(this,view);
        initView(view);
        return view;
    }
    protected void initView(View view){}

    abstract int getLayout();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            getActivity().onBackPressed();
        }
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        //获取当前界面可视部分
        if(getActivity()==null){
            return;
        }
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = getActivity().getWindow().getDecorView().getRootView().getHeight();
        //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
        int heightDifference = screenHeight - r.bottom;
        Log.d("Keyboard Size", "Size: " + heightDifference);
        if(SystemUtil.checkDeviceHasNavigationBar(getContext())){
            onSoftKeyboardChange(heightDifference-mNavigationHeight);
        }else {
            onSoftKeyboardChange(heightDifference);
        }
        //showViewOverKeyBoard(heightDifference);
    }
    public abstract void onSoftKeyboardChange(int height);
}
