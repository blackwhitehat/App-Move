package com.example.newmvvm.AllLink.PAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.newmvvm.Move.Pro.Link;
import com.example.newmvvm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllProduct extends RecyclerView.Adapter<AllProduct.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<AllPT> mModels;

    public AllProduct(Context context, List<AllPT> mModels) {
        this.context = context;
        this.mModels = mModels;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_all_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     AllPT model=mModels.get(position);
        Picasso.get().load(model.getPoster()).into(holder.imageView);
        holder.name.setText(model.getTitle());
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.all_img);
            name=itemView.findViewById(R.id.all_name);
        }
    }
}
