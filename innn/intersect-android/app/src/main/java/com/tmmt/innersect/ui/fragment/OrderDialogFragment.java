package com.tmmt.innersect.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.ui.activity.OrdersListActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDialogFragment extends DialogFragment implements OnClickListener{

	 public static OrderDialogFragment newInstance(String orderNo) {

		Bundle args = new Bundle();
		OrderDialogFragment fragment = new OrderDialogFragment();
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

			JSONStringer jsonStringer = new JSONStringer();
			try {
				jsonStringer.object()
						.key("orderNo").value(getArguments().getString(Constants.ORDER_NO))
						.endObject();
				KLog.json(jsonStringer.toString());
			} catch (JSONException e) {
				KLog.i("JsonException");
			}
			RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
			ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).cancelOrder(body)
					.enqueue(new Callback<Status>() {
				@Override
				public void onResponse(Call<Status> call, Response<Status> response) {
					if(response.isSuccessful()){
                        KLog.d(response.body().code);
                        Intent intent=new Intent(getContext(),OrdersListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getContext().startActivity(intent);
						getDialog().cancel();
					}
				}
				
				@Override
				public void onFailure(Call<Status> call, Throwable t) {
					KLog.d(t);
					getDialog().cancel();
				}
			});

		}else {
			getDialog().cancel();
		}
	}
}
