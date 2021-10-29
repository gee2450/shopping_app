package com.example.shoppingapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClothAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<SampleData> sample = new ArrayList<>();
    ViewGroup homeFragment;

    ClothAdapter(ViewGroup homeFragment) {
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder((ItemViewHolder) holder, position);
    }

    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(sample.get(position));
    }

    public void addItem(SampleData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        sample.add(data);
    }

    @Override
    public int getItemCount() {
        return sample.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView sampleData;
        ImageView imageView;
        TextView clothName;
        TextView clothPrice;

        SampleData data;

        ItemViewHolder(View itemView) {
            super(itemView);

            sampleData = (CardView) itemView.findViewById(R.id.sampleData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeFragment.findViewById(R.id.btnDelete).getVisibility() != View.VISIBLE) {
                        return;
                    }
                    if (data.isClick()) {
                        sampleData.setBackgroundColor(Color.WHITE);
                    }
                    else {
                        sampleData.setBackgroundColor(Color.parseColor("#11111111"));
                    }
                    data.setClick(!data.isClick());
                }
            });
            imageView = (ImageView) itemView.findViewById(R.id.cloth);
            clothName = (TextView) itemView.findViewById(R.id.clothName);
            clothPrice = (TextView) itemView.findViewById(R.id.clothPrice);
        }

        void onBind(SampleData data) {
            this.data = data;
            imageView.setImageBitmap(data.getCloth());
            clothName.setText(data.getClothName());
            clothPrice.setText(data.getClothPrice()+"원");
        }
    }

//    public View getView(int position, View converView, ViewGroup parent) {
//        View view = mLayoutInflater.inflate(R.layout.sample_item, null);
//
//        imageView.setImageBitmap(sample.get(position).getCloth());
//        clothName.setText(sample.get(position).getClothName());
//        clothPrice.setText(sample.get(position).getClothPrice()+"원");
//
//        return view;
//    }
}