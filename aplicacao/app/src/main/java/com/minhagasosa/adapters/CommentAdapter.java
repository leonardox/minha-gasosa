package com.minhagasosa.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhagasosa.R;
import com.minhagasosa.Transfer.Comments;


public class CommentAdapter extends ArrayAdapter<Comments> {

	private Context context;
	private List<Comments> evts = new ArrayList<Comments>();
	
    public CommentAdapter(Context ctx, List<Comments> resource) {
    	super(ctx, -1, resource);
		this.evts = resource;
		this.context = ctx;
		// TODO Auto-generated constructor stub
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.comment_adapter, null);
        }

        final Comments comment = evts.get(position);

        TextView tvAuthor = (TextView) v.findViewById(R.id.tv_comment_author);
        if(comment.getAuthor() != null){
            tvAuthor.setText(comment.getAuthor().getFirstName());
        }

        TextView tvCommentText = (TextView) v.findViewById(R.id.tv_comment_text);
        tvCommentText.setText(comment.getText());

        return v;
    }

}
