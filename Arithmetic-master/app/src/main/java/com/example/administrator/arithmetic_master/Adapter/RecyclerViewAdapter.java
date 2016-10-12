package com.example.administrator.arithmetic_master.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.arithmetic_master.Model.Row_Item;
import com.example.administrator.arithmetic_master.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<Row_Item> listRowItem= Collections.emptyList();
    private LayoutInflater m_LayoutInflater;
    private Context m_Context;
    private int numCreate=0;
    public RecyclerViewAdapter(Context context,List<Row_Item> data)
    {
        m_Context=context;
        m_LayoutInflater=LayoutInflater.from(context);
        listRowItem=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = m_LayoutInflater.inflate(R.layout.row_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Row_Item currentItem=listRowItem.get(position);
        holder.item_icon.setImageResource(currentItem.gradeIconId);
        holder.item_level.setImageResource(currentItem.gradeFlagId);
    }

    @Override
    public int getItemCount() {
        return listRowItem.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView item_icon;
        ImageView item_level;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            item_icon=(ImageView)itemView.findViewById(R.id.item_icon);
            item_level=(ImageView)itemView.findViewById(R.id.item_level);
        }
    }
}

