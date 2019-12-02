//package com.tmmt.innersect.ui.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.SpuViewModel;
//import com.tmmt.innersect.ui.adapter.viewholder.BaseViewHolder;
//import com.tmmt.innersect.widget.CircleQuantityView;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
///**
// * Created by flame on 2017/7/24.
// */
//
//public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketViewHolder> {
//
//    private List<SpuViewModel.Group> mTickets;
//
//    private TicketAdapter.OnSizeChangeListener mListener;
//
//    public void setListener(TicketAdapter.OnSizeChangeListener listener){
//        mListener=listener;
//    }
//
//    public void addItems(List<SpuViewModel.Group> groups){
//        mTickets=groups;
//        notifyDataSetChanged();
//    }
//
//    public TicketListAdapter(){
//        mTickets=new ArrayList<>();
//    }
//
//    @Override
//    public void onBindViewHolder(TicketViewHolder holder, final int position) {
//        final Context context=holder.itemView.getContext();
//        final SpuViewModel.Group group=mTickets.get(position);
//        holder.bindViewHolder(group,position);
//        holder.quantityView.setQuantityChangeListener(new CircleQuantityView.OnQuantityChangeListener() {
//            @Override
//            public void onQuantityChanged(int newQuantity, boolean programmatically) {
//                group.quantity=newQuantity;
//                if(newQuantity==0){
//                    mTickets.remove(group);
//                    notifyItemRemoved(position);
//                }
//
//                if(mListener!=null){
//                    mListener.onSizeChanged();
//                }
//            }
//            @Override
//            public void onLimitReached() {
//                Toast.makeText(context,"已达到限购数量",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view;
//        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_ticket_item,parent,false);
//        return new TicketViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//       return mTickets.size();
//    }
//
//    static class  TicketViewHolder extends BaseViewHolder<SpuViewModel.Group> {
//
//        @BindView(R.id.title_view)
//        TextView nameView;
//
//        @BindView(R.id.price_view)
//        TextView priceView;
//        @BindView(R.id.time_view)
//        TextView timeView;
//        @BindView(R.id.quantity_view)
//        CircleQuantityView quantityView;
//
//        public TicketViewHolder(View view){
//            super(view);
//            ButterKnife.bind(this,view);
//        }
//
//        @Override
//        public void bindViewHolder(final SpuViewModel.Group group, int position) {
//
//            nameView.setText(group.getSkuName());
//            timeView.setText(group.date);
//            priceView.setText("￥"+group.salePrice);
//            quantityView.setQuantity(group.quantity);
//
//            if(group.maxAvailableCount==0){
//                quantityView.setMaxQuantity(group.availableCount);
//            }else {
//                quantityView.setMaxQuantity(group.maxAvailableCount);
//            }
//        }
//    }
//
//}
