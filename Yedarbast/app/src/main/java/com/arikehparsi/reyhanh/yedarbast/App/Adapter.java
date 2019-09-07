package com.arikehparsi.reyhanh.yedarbast.App;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arikehparsi.reyhanh.yedarbast.NewsActivity;
import com.arikehparsi.reyhanh.yedarbast.NewsDetailActivity;
import com.arikehparsi.reyhanh.yedarbast.R;
import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private Context context;
    private List<Info> data;
    private LayoutInflater inflater;
    private Handler handler;
    public Adapter(Context context, List<Info> data) {
        this.context = context;
        this.data = data;
        inflater=LayoutInflater.from(context);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View row=inflater.inflate(R.layout.row,parent,false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Info curs=data.get(position);
        holder.name.setText(curs.infname);
        holder.date.setText(curs.infdate);
        holder.matn.setText(curs.infmatn);
        Picasso.get()
                .load(curs.infimg)
                .fit()
                .into(holder.img);
        //Toast.makeText(context, curs.infimg+"bbb", Toast.LENGTH_SHORT).show();
        //  holder.img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private MaterialRippleLayout cv;
        private TextView name,date,matn;
        private ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.card);
            name=itemView.findViewById(R.id.tv);
            date=itemView.findViewById(R.id.row_date);
            img=itemView.findViewById(R.id.row_img);
            matn=itemView.findViewById(R.id.row_matn);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,NewsDetailActivity.class);
                    i.putExtra("matn",matn.getText());
                    i.putExtra("titr",name.getText());
                    i.putExtra("img",NewsActivity.jimg[getAdapterPosition()]);
                    context.startActivity(i);
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            });



        }
    }



}
