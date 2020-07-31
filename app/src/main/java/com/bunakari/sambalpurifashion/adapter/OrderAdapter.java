package com.bunakari.sambalpurifashion.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.OrderResponse;

import java.util.List;



public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderView> {

    private Context context;
    private List<OrderResponse> orderResponseList;
    private ItemClickListener itemClickListener;
    private Activity activity;

    public OrderAdapter(Activity activity, ItemClickListener itemClickListener, Context context, List<OrderResponse> orderResponseList) {
        this.activity = activity;
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.orderResponseList = orderResponseList;
    }

    @NonNull
    @Override
    public OrderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_item,parent,false);

        return new OrderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderView holder, int position) {

        OrderResponse orderResponse = orderResponseList.get(position);

        holder.oDateTextView.setText(orderResponse.getDate());
        holder.oIdTextView.setText(orderResponse.getOrderid());
        holder.oAmtTextView.setText("\u20B9 "+orderResponse.getTotal_amount() + " (Item : " + orderResponse.getTotal_item() + " Qty)");

         if (orderResponse.getOrder_status().equalsIgnoreCase("2")){
            holder.transportTextView.setVisibility(View.VISIBLE);

            holder.transportTextView.setOnClickListener(view -> BasicFunction.showDailogBox(activity,orderResponse.getOrder_status()));
        }


    }

    @Override
    public int getItemCount() {
        return orderResponseList.size();
    }

    public class OrderView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView oStatusTextView,oIdTextView,oDateTextView,oAmtTextView,oItemTextView;
        private TextView orderDetailTextView,transportTextView;

        public OrderView(@NonNull View itemView) {
            super(itemView);

            oStatusTextView = itemView.findViewById(R.id.statusTextView);
            oIdTextView = itemView.findViewById(R.id.orderIdValueTextView);
            oDateTextView = itemView.findViewById(R.id.odateValueTextView);
            oAmtTextView = itemView.findViewById(R.id.ototalValueTextView);
            orderDetailTextView = itemView.findViewById(R.id.odetailTextView);
            transportTextView = itemView.findViewById(R.id.transportTextView);

            orderDetailTextView.setOnClickListener(this);
            transportTextView.setOnClickListener(this);

            orderDetailTextView.setTag(1);
            transportTextView.setTag(2);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view,getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
