package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.NetworkResponse;

import java.util.List;


public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.TransView> {

    private Context context;
    private List<NetworkResponse> nwResponseList;
    private ItemClickListener itemClickListener;

    public NetworkAdapter(ItemClickListener itemClickListener, Context context, List<NetworkResponse> nwResponseList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.nwResponseList = nwResponseList;
    }

    @NonNull
    @Override
    public TransView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.network_list_item,parent,false);
        return new TransView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransView holder, int position) {
        NetworkResponse networkResponse = nwResponseList.get(position);
        holder.nameTextView.setText(networkResponse.getName());
        holder.refCodeTextView.setText("Ref Code :\n"+networkResponse.getUserappreferralcode());
        holder.memberTextView.setText("Total Members : "+networkResponse.getUsernetworkcount());
    }

    @Override
    public int getItemCount() {
        return nwResponseList.size();
    }

    public class TransView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView,refCodeTextView,memberTextView;

        public TransView(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            refCodeTextView = itemView.findViewById(R.id.refCodeTextView);
            memberTextView = itemView.findViewById(R.id.membersTextView);

            itemView.setOnClickListener(this);
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
