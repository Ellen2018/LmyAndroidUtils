package com.yalemang.library.android.viewutils.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yalemang.library.R;
import com.yalemang.library.android.viewutils.bean.ViewTree;

public class ViewTreeAdapter extends RecyclerView.Adapter<ViewTreeAdapter.ViewTreeViewHolder> {


    private ViewTree[] viewTrees;
    private OnItemClick onItemClick;
    private OnItemLongClick onItemLongClick;
    private ViewTree targetViewTree;

    public ViewTree[] getViewTrees() {
        return viewTrees;
    }

    public void setOnItemLongClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public void setTargetViewTree(ViewTree targetViewTree) {
        this.targetViewTree = targetViewTree;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ViewTreeAdapter(ViewTree[] viewTrees) {
        this.viewTrees = viewTrees;
    }

    @NonNull
    @Override
    public ViewTreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_tree, parent, false);
        return new ViewTreeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTreeViewHolder holder, int position) {
        ViewTree viewTree = viewTrees[position];
        holder.tv.setText(viewTree.getView().getClass().getName());
        if(onItemClick != null){
            holder.itemView.setOnClickListener(v -> onItemClick.clickItem(position));
        }
        if(onItemLongClick != null){
            holder.itemView.setOnLongClickListener(v -> {
                onItemLongClick.longClickItem(position);
                return false;
            });
        }
        View view = viewTree.getView();
        if(view instanceof ViewGroup){
            holder.tvType.setText("ViewGroup");
        }else if(view instanceof ViewStub){
            holder.tvType.setText("ViewStub");
        }else {
            holder.tvType.setText("View");
        }
        if(targetViewTree != null){
            if(targetViewTree.getView().equals(viewTree.getView())){
                holder.itemView.setBackgroundColor(Color.BLUE);
                holder.tv.setTextColor(Color.WHITE);
                holder.tvType.setTextColor(Color.WHITE);
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.tv.setTextColor(Color.BLACK);
                holder.tvType.setTextColor(Color.BLACK);
            }
        }else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.tv.setTextColor(Color.BLACK);
            holder.tvType.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return viewTrees.length;
    }

    static class ViewTreeViewHolder extends RecyclerView.ViewHolder {

        TextView tv,tvType;

        public ViewTreeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            tvType = itemView.findViewById(R.id.tv_view_type);
        }
    }

    public interface OnItemClick{
        void clickItem(int position);
    }

    public interface OnItemLongClick{
        void longClickItem(int position);
    }
}
