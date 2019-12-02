package com.tmmt.innersect.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.tmmt.innersect.R;
import com.tmmt.innersect.ui.activity.FeedbackActivity;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;

public class RateDialogFragment extends DialogFragment implements OnClickListener {

    @Override
    public void onStart() {
        super.onStart();
        setCancelable(false);
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_rate, null);

        View cancel = view.findViewById(R.id.cancel);
        View rate = view.findViewById(R.id.rate);
        View feedback=view.findViewById(R.id.feedback);

        cancel.setOnClickListener(this);
        rate.setOnClickListener(this);
        feedback.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rate) {
            SystemUtil.goToMarket(getContext(),"com.tmmt.innersect");
            AnalyticsUtil.reportEvent(AnalyticsUtil.POPUPWINDOW_GOOD);

        }
        if(v.getId()==R.id.feedback){
            Util.startActivity(getContext(), FeedbackActivity.class);
            AnalyticsUtil.reportEvent(AnalyticsUtil.POPUPWINDOW_NEGATIVE_SUBMIT);
        }
        if(v.getId()==R.id.cancel){
            AnalyticsUtil.reportEvent(AnalyticsUtil.POPUPWINDOW_CANCEL);

        }
        getDialog().cancel();


    }
}
