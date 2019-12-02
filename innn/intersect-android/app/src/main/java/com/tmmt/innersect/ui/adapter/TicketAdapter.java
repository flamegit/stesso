//package com.tmmt.innersect.ui.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.SpuViewModel;
//import com.tmmt.innersect.ui.adapter.viewholder.BaseViewHolder;
//import com.tmmt.innersect.widget.CircleQuantityView;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
///**
// * Created by flame on 2017/7/24.
// */
//
//public class TicketAdapter extends RecyclerView.Adapter<BaseViewHolder<SpuViewModel.Group>> {
//
//    public interface OnSizeChangeListener{
//        void onSizeChanged();
//    }
//    private List<List<SpuViewModel.Group>> mTickets;
//    private List<SpuViewModel.Group> mCurrTickets;
//    private int mIndex;
//    private OnSizeChangeListener mListener;
//    private ArrayList<SpuViewModel.Group> mSelect;
//    private static final int CONTENT=1;
//    //private static final int FOOTER=2;
//    public void addItems(SpuViewModel.Data data){
//        for(int i=0;i<4;i++){
//            if(data.getTicket(i)!=null){
//                mTickets.get(i).addAll(data.getTicket(i));
//                Collections.sort(mTickets.get(i), new Comparator<SpuViewModel.Group>() {
//                    @Override
//                    public int compare(SpuViewModel.Group o1, SpuViewModel.Group o2) {
//                        return o2.order-o1.order;
//                    }
//                });
//            }
//        }
//    }
//
//    public void setListener(OnSizeChangeListener listener){
//        mListener=listener;
//    }
//
//    public TicketAdapter(){
//        mTickets=new ArrayList<>();
//        mSelect=new ArrayList<>();
//        mTickets.add(new ArrayList<SpuViewModel.Group>());
//        mTickets.add(new ArrayList<SpuViewModel.Group>());
//        mTickets.add(new ArrayList<SpuViewModel.Group>());
//        mTickets.add(new ArrayList<SpuViewModel.Group>());
//        mCurrTickets=mTickets.get(0);
//        mIndex=0;
//    }
//
//    public void changeData(int index){
//        mCurrTickets=mTickets.get(index);
//        notifyDataSetChanged();
//        mIndex=index;
//    }
//
//    public int getSelectCount(){
//        int count=0;
//        for(SpuViewModel.Group group:mSelect){
//            count+=group.quantity;
//        }
//        return count;
//    }
//
//    public float getSelectPrice(){
//        float price=0;
//        for(SpuViewModel.Group group:mSelect){
//            price+=group.quantity*group.salePrice;
//        }
//        return price;
//    }
//
//    private String getTime(){
//        switch(mIndex){
//            case 0:
//                return "3日套票";
//            case 1:
//                return "10月5日";
//            case 2:
//                return "10月6日";
//            case 3:
//                return "10月7日";
//        }
//        return "10月5-7日";
//    }
//
//    @Override
//    public void onBindViewHolder(BaseViewHolder holder, final int position) {
//        final Context context=holder.itemView.getContext();
//        if(getItemViewType(position)==CONTENT){
//            final TicketViewHolder viewHolder=(TicketViewHolder)holder;
//            viewHolder.bindViewHolder(mCurrTickets.get(position),position);
//
//            final SpuViewModel.Group group=mCurrTickets.get(position);
//            group.date=getTime();
//            viewHolder.addView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    group.quantity=1;
//                    mSelect.add(group);
//                    viewHolder.addView.setVisibility(View.INVISIBLE);
//                    viewHolder.quantityView.setQuantity(1);
//                    viewHolder.quantityView.setVisibility(View.VISIBLE);
//                    if(mListener!=null){
//                        mListener.onSizeChanged();
//                    }
//                }
//            });
//            viewHolder.quantityView.setQuantityChangeListener(new CircleQuantityView.OnQuantityChangeListener() {
//                @Override
//                public void onQuantityChanged(int newQuantity, boolean programmatically) {
//                    if(newQuantity==0){
//                        viewHolder.addView.setVisibility(View.VISIBLE);
//                        viewHolder.quantityView.setVisibility(View.INVISIBLE);
//                        mSelect.remove(group);
//                    }
//                    group.quantity=newQuantity;
//                    if(mListener!=null){
//                        mListener.onSizeChanged();
//                    }
//                }
//                @Override
//                public void onLimitReached() {
//                    Toast.makeText(context,"已达到限购数量",Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }
//
//    public ArrayList<SpuViewModel.Group> getSelect(){
//        return mSelect;
//    }
//
//    @Override
//    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view;
//        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_ticket,parent,false);
//        return new TicketViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//       return mCurrTickets.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return CONTENT;
//    }
//
//    static class  TicketViewHolder extends BaseViewHolder<SpuViewModel.Group>{
//
//        @BindView(R.id.name_view)
//        TextView nameView;
//        @BindView(R.id.limit_view)
//        TextView limitView;
//        @BindView(R.id.gift_view)
//        TextView giftView;
//        @BindView(R.id.discount_view)
//        TextView discountView;
//        @BindView(R.id.price_view)
//        TextView priceView;
//        @BindView(R.id.add_view)
//        ImageView addView;
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
//            discountView.setText(String.format("￥%.2f",group.salePrice));
//            //priceView.setText("原价￥"+group.originalPrice);
//            //priceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//            giftView.setText(group.giftInfo);
//            nameView.setText(group.getSkuName());
//            quantityView.setQuantity(group.quantity);
//
//            if(group.quantity>0){
//                addView.setVisibility(View.INVISIBLE);
//                quantityView.setVisibility(View.VISIBLE);
//            }else {
//                addView.setVisibility(View.VISIBLE);
//                quantityView.setVisibility(View.INVISIBLE);
//            }
//
//            if(group.maxAvailableCount==0){
//                quantityView.setMaxQuantity(group.availableCount);
//            }else {
//                quantityView.setMaxQuantity(Math.min(group.maxAvailableCount,group.availableCount));
//            }
//            if(group.maxAvailableCount!=0){
//                limitView.setVisibility(View.VISIBLE);
//                limitView.setText(String.format("每人限购%d张",Math.min(group.maxAvailableCount,group.availableCount)));
//            }else {
//                limitView.setVisibility(View.INVISIBLE);
//            }
//        }
//    }
//
//}
