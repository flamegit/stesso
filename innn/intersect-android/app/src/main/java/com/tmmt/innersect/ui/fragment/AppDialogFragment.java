package com.tmmt.innersect.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tmmt.innersect.R;

public class AppDialogFragment extends DialogFragment implements OnClickListener {

    public static AppDialogFragment newInstance(int layoutId, String title) {
        Bundle args = new Bundle();
        AppDialogFragment fragment = new AppDialogFragment();
        args.putInt("layoutId", layoutId);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public static AppDialogFragment newInstance(int layoutId, String title, String content) {
        Bundle args = new Bundle();
        AppDialogFragment fragment = new AppDialogFragment();
        args.putInt("layoutId", layoutId);

        if (content != null) {
            args.putString("content",content);
        }
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public static AppDialogFragment newInstance(int layoutId, String title, String content, String cancel, String ok) {
        Bundle args = new Bundle();
        AppDialogFragment fragment = new AppDialogFragment();
        args.putInt("layoutId", layoutId);

        if (content != null) {
            args.putString("content",content);
        }

        if (cancel != null) {
            args.putString("cancel", cancel);
        }

        if (ok != null) {
            args.putString("ok",ok);
        }

        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public static class Builder{
        int layoutId;
        String title;
        String content;
        String cancel;
        String ok;

        public Builder setLayout(int id){
            layoutId=id;
            return this;
        }

        public Builder setTitle(String title){
            this.title=title;
            return this;
        }
        public Builder setContent(String content){
            this.content=content;
            return this;
        }
        public Builder setCancel(String cancel){
            this.cancel=cancel;
            return this;
        }
        public Builder setOk(String ok){
            this.ok=ok;
            return this;
        }

        public AppDialogFragment build(){
           return newInstance(layoutId,title,content,cancel,ok);
        }
    }

    ActionListener mListener;
    public interface ActionListener {
        void doAction();
        void cancel();
    }

    TextView mTitleView;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId = getArguments().getInt("layoutId");
        String title =getArguments().getString("title");
        String content=getArguments().getString("content");
        String cancelText =getArguments().getString("cancel");
        String okText=getArguments().getString("ok");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(layoutId, null);
        mTitleView = view.findViewById(R.id.title_view);
        if(title!=null&&!title.isEmpty()){
            mTitleView.setText(title);
        }
        TextView contentView=view.findViewById(R.id.content_view);
        if(contentView!=null &&content!=null){
            contentView.setText(content);
        }
        TextView cancel = view.findViewById(R.id.cancel);
        TextView ok = view.findViewById(R.id.ok);
        if (cancel != null) {
            if (cancelText != null) {
                cancel.setText(cancelText);
            }
            cancel.setOnClickListener(this);
        }

        if (okText != null) {
            ok.setText(okText);
        }

        ok.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok) {
            if (mListener != null) {
                mListener.doAction();
            }
            getDialog().cancel();
        }
        if (v.getId() == R.id.cancel) {
            if (mListener != null) {
                mListener.cancel();
            }
            getDialog().cancel();
        }
    }
}
