package com.minhagasosa.activites.maps;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minhagasosa.R;
import com.minhagasosa.Transfer.Comments;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Comments> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mName, mComment, mDate;
        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mName = (TextView) v.findViewById(R.id.tv_comment_name);
            mComment = (TextView) v.findViewById(R.id.tv_comment);
            mDate = (TextView) v.findViewById(R.id.tv_date);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Comments> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_commets, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mDataset.get(position) != null && mDataset.get(position).getAuthor() != null ) {
            if (mDataset.get(position).getAuthor().getFirstName() != null) {
                holder.mName.setText(mDataset.get(position).getAuthor().getFirstName());
            }
            if (mDataset.get(position).getText() != null) {
                holder.mComment.setText(mDataset.get(position).getText());
            }

            if (mDataset.get(position).getCreationDate() != null) {
                holder.mDate.setText(mDataset.get(position).getCreationDate());
            }else{
                holder.mDate.setText("Enviado: 23/10/2016 as 14:37");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}