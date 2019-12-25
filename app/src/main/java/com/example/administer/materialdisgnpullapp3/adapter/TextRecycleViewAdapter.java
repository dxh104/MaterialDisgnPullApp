package com.example.administer.materialdisgnpullapp3.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administer.materialdisgnpullapp3.R;
import com.example.administer.materialdisgnpullapp3.entity.TextData;
import com.example.administer.materialdisgnpullapp3.viewHolder.TextViewHolder;

import java.util.List;

/**
 * Created by XHD on 2019/11/20
 */
public class TextRecycleViewAdapter extends RecyclerView.Adapter<TextViewHolder> {
    private List<TextData> textDataList;
    private Context mContext;

    public TextRecycleViewAdapter(List<TextData> textDataList) {
        this.textDataList = textDataList;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_text, viewGroup, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder textViewHolder, int i) {
        textViewHolder.tvItem.setText(textDataList.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return textDataList.size();
    }


}
