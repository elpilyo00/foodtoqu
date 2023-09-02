package com.example.foodtoqu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recName.setText(dataList.get(position).getDataName());
        holder.recCalorie.setText(dataList.get(position).getDataCalorie());
        holder.recFat.setText(dataList.get(position).getDataFat());
        holder.recCholesterol.setText(dataList.get(position).getDataCholesterol());
        holder.recSodium.setText(dataList.get(position).getDataSodium());
        holder.recCarbo.setText(dataList.get(position).getDataCarbo());
        holder.recSugar.setText(dataList.get(position).getDataSugar());
        holder.recProtein.setText(dataList.get(position).getDataProtein());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Food Images", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataName());
                intent.putExtra("Calorie", dataList.get(holder.getAdapterPosition()).getDataCalorie());
                intent.putExtra("Total Fat", dataList.get(holder.getAdapterPosition()).getDataFat());
                intent.putExtra("Cholesterol", dataList.get(holder.getAdapterPosition()).getDataCholesterol());
                intent.putExtra("Sodium", dataList.get(holder.getAdapterPosition()).getDataSodium());
                intent.putExtra("Total Carbohydrate", dataList.get(holder.getAdapterPosition()).getDataCarbo());
                intent.putExtra("Total Sugar", dataList.get(holder.getAdapterPosition()).getDataSugar());
                intent.putExtra("Protein", dataList.get(holder.getAdapterPosition()).getDataProtein());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class  MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recName, recCalorie, recFat, recCholesterol, recSodium, recCarbo, recSugar, recProtein;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recName = itemView.findViewById(R.id.recName);
        recCalorie = itemView.findViewById(R.id.recCalorie);
        recFat = itemView.findViewById(R.id.recFat);
        recCholesterol = itemView.findViewById(R.id.recCholesterol);
        recSodium = itemView.findViewById(R.id.recSodium);
        recCarbo = itemView.findViewById(R.id.recCarbo);
        recSugar = itemView.findViewById(R.id.recSugar);
        recProtein = itemView.findViewById(R.id.recProtein);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
