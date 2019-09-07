package com.example.newmvvm.Move.Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmvvm.Move.Pro.Link;
import com.example.newmvvm.Move.Pro.Search;
import com.example.newmvvm.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

public class MAdapter extends RecyclerView.Adapter<MAdapter.ViewHolde> {
    private Context context;
    private LayoutInflater inflater;
    private List<Link> list;
    private OnPrime prime;
    public MAdapter(Context context, List<Link> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_move,parent,false);
        return new ViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolde holder, int position) {
    Link search=list.get(position);
    holder.title.setText(search.getSearch().get(position).getTitle());
    holder.year.setText(search.getSearch().get(position).getYear());
    holder.cat.setText(search.getSearch().get(position).getType());
    holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            prime.ocClick(position+"");
        }
    });
        Picasso.get().load(search.getSearch().get(position).getPoster()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView);

    }

    public void setPrime(OnPrime prime) {
        this.prime = prime;
    }

    public interface OnPrime{
        void ocClick(String ids);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolde extends RecyclerView.ViewHolder{

        private TextView title,cat,year;
        private ImageView imageView;
        private CardView cardView;
        private CarouselView carouselView;
        public ViewHolde(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.row_main_title);
            cat=itemView.findViewById(R.id.row_main_cat);
            year=itemView.findViewById(R.id.row_main_year);
            imageView=itemView.findViewById(R.id.row_main_img);
            cardView=itemView.findViewById(R.id.pro_Move_Item);

        }
    }
}
