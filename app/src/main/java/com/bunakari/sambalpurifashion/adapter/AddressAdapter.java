package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.AddressResponse;
import com.bunakari.sambalpurifashion.model.GetPrefs;

import java.util.ArrayList;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressView> {

    private Context context;
    private ArrayList<AddressResponse> addressList;
    private ItemClickListener itemClickListener;
    private SharedPreferences sharedPreferences;
    private String posString;

    public AddressAdapter(ItemClickListener itemClickListener, Context context, ArrayList<AddressResponse> addressList) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public AddressView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_list_item,parent,false);

        return new AddressView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressView holder, int position) {
        sharedPreferences = context.getSharedPreferences(GetPrefs.PREFS_NAME,context.MODE_PRIVATE);
        posString = sharedPreferences.getString(GetPrefs.PREFS_ADD_POS,"");

        AddressResponse addressResponse = addressList.get(position);

        holder.nameTextView.setText(addressResponse.getName());
        holder.addressTextView.setText(addressResponse.getAddress()+","+addressResponse.getArea()+","+addressResponse.getLandmark()+","+addressResponse.getCity()+","+addressResponse.getState()+","+addressResponse.getPincode());

        if (Integer.parseInt(posString) == position){
            holder.radioImageView.setImageResource(R.drawable.selectedradio);
            holder.goneLayout.setVisibility(View.VISIBLE);
        }else {
            holder.radioImageView.setImageResource(R.drawable.radio);
            holder.goneLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView,addressTextView,delieverAddressTextView,editAddTextView,deleteAddTextView;
        private ImageView radioImageView;
        private RelativeLayout goneLayout;

        public AddressView(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            delieverAddressTextView = itemView.findViewById(R.id.deliveryTextView);
            editAddTextView = itemView.findViewById(R.id.editAddTextView);
            deleteAddTextView = itemView.findViewById(R.id.deleteAddTextView);
            radioImageView = itemView.findViewById(R.id.radioImgView);
            goneLayout = itemView.findViewById(R.id.gonelayout);

            itemView.setOnClickListener(this);
            delieverAddressTextView.setOnClickListener(this);
            editAddTextView.setOnClickListener(this);
            deleteAddTextView.setOnClickListener(this);

            itemView.setTag(1);
            delieverAddressTextView.setTag(2);
            editAddTextView.setTag(3);
            deleteAddTextView.setTag(4);
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
