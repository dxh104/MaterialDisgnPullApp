package com.example.administer.materialdisgnpullapp3.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administer.materialdisgnpullapp3.R;


/**
 * Created by XHD on 2019/11/20
 */
public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView tvItem;

    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        tvItem = (TextView) itemView.findViewById(R.id.tv_item);

    }
}
