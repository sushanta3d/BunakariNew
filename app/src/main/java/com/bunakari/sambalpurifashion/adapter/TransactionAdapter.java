package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.TransactionResponse;

import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransView> {

    private Context context;
    private List<TransactionResponse> transResponseList;

    public TransactionAdapter(Context context, List<TransactionResponse> transResponseList) {
        this.context = context;
        this.transResponseList = transResponseList;
    }

    @NonNull
    @Override
    public TransView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_list_item,parent,false);
        return new TransView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransView holder, int position) {
        TransactionResponse transactionResponse = transResponseList.get(position);
        holder.rIdTextView.setText(transactionResponse.getReqid());
        holder.amtTextView.setText("\u20B9 "+transactionResponse.getAmount());
        holder.dateTextView.setText(transactionResponse.getDate());
    }

    @Override
    public int getItemCount() {
        return transResponseList.size();
    }

    public class TransView extends RecyclerView.ViewHolder{

        private TextView rIdTextView,amtTextView,dateTextView;

        public TransView(@NonNull View itemView) {
            super(itemView);
            rIdTextView = itemView.findViewById(R.id.rIdValueTextView);
            amtTextView = itemView.findViewById(R.id.amtValueTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
