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

public class OrderFailDialog extends DialogFragment implements OnClickListener{

	 public static OrderFailDialog newInstance(String orderNo) {

		Bundle args = new Bundle();
		OrderFailDialog fragment = new OrderFailDialog();
		args.putString("orderNo",orderNo);
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public void onStart() {
		super.onStart();
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
		View view=inflater.inflate(R.layout.dialog_order, null);
		View cancel=view.findViewById(R.id.cancel);
		View ok=view.findViewById(R.id.ok);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		return view;
	}


	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ok){

		}
		if(v.getId()==R.id.cancel){
			getDialog().cancel();
		}

	}
}
