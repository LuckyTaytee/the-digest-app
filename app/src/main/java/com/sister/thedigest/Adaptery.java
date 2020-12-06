package com.sister.thedigest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

    private Context mContext;
    private List<Article> articlesList;
    private OnArticleListener mOnArticleListener;

    public Adaptery(Context mContext, List articlesList, OnArticleListener onArticleListener) {
        this.mContext = mContext;
        this.articlesList = articlesList;
        this.mOnArticleListener = onArticleListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v ;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.article_itemfigma, parent, false);
        return new MyViewHolder(v, mOnArticleListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(articlesList.get(position).getTitle());
        //holder.source.setText(articlesList.get(position).getSource());

        Glide.with(mContext)
                .load(articlesList.get(position).getImg())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        //TextView source;
        ImageView img;

        OnArticleListener onArticleListener;

        public MyViewHolder(View itemView, OnArticleListener onArticleListener) {
            super(itemView);

            title = itemView.findViewById(R.id.tvmaintitle);
            //source = itemView.findViewById(R.id.tvmainsource);
            img = itemView.findViewById(R.id.ivmainimg);
            this.onArticleListener = onArticleListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onArticleListener.onArticleClick(getAdapterPosition());
        }
    }

    public interface OnArticleListener {
        void onArticleClick(int position);
    }
}
