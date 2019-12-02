//package com.tmmt.innersect.ui.fragment;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.tmmt.innersect.App;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.mvp.model.LotteryInfo;
//
//public class LotteryDialogFragment extends DialogFragment {
//
//    public static LotteryDialogFragment newInstance(LotteryInfo info) {
//        Bundle args = new Bundle();
//        LotteryDialogFragment fragment = new LotteryDialogFragment();
//        args.putParcelable(Constants.LOTTERY_INFO,info);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = inflater.inflate(R.layout.dialog_lottery, null);
//        ImageView imageView=(ImageView)view.findViewById(R.id.image_view);
//        TextView codeView=(TextView)view.findViewById(R.id.code_view);
//
//        LotteryInfo info=getArguments().getParcelable(Constants.LOTTERY_INFO);
//        if(info!=null){
//
//            Glide.with(App.getAppContext()).load(info.imgUrl).into(imageView);
//            codeView.setText(info.qrCode);
//        }
//        return view;
//    }
//
//}
