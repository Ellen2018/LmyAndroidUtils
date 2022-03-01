package com.yalemang.library.android.viewutils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalemang.library.R;
import com.yalemang.library.android.viewutils.adapter.ViewTreeAdapter;
import com.yalemang.library.android.viewutils.bean.ViewTree;

public class ViewTreeDialog {

    private BottomSheetDialog bottomSheetDialog;
    private View contentView;
    private RecyclerView recyclerView;
    private TextView tvTitle;
    private Button btPre;
    private ViewTree viewTree;
    private ViewTree targetViewTree;
    private ViewTreeAdapter viewTreeAdapter;
    private ViewTreeAdapter.OnItemClick onItemClick;

    public ViewTreeDialog(Context context, ViewTree viewTree) {
        this.viewTree = viewTree;
        bottomSheetDialog = new BottomSheetDialog(context);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_view_tree, null);
        initView();
        initData();
        bottomSheetDialog.setContentView(contentView);
    }

    public ViewTreeDialog(Context context, ViewTree viewTree,ViewTree targetViewTree) {
        this.viewTree = viewTree;
        this.targetViewTree = targetViewTree;
        bottomSheetDialog = new BottomSheetDialog(context);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_view_tree, null);
        initView();
        initData();
        bottomSheetDialog.setContentView(contentView);
    }

    private void initData() {
    }

    public void show() {
        bottomSheetDialog.show();
    }

    private void initView() {
        recyclerView = contentView.findViewById(R.id.recycler_view);
        btPre = contentView.findViewById(R.id.bt_pre);
        tvTitle = contentView.findViewById(R.id.tv_title);
        if(viewTree.getParent() != null){
            btPre.setVisibility(View.VISIBLE);
        }else {
            btPre.setVisibility(View.GONE);
        }
        tvTitle.setText("嵌套级别:"+(viewTree.getLevel()+1));
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        ViewTree[] rootViewTrees = new ViewTree[1];
        rootViewTrees[0] = viewTree;
        viewTreeAdapter = new ViewTreeAdapter(rootViewTrees);
        viewTreeAdapter.setTargetViewTree(targetViewTree);
        viewTreeAdapter.setOnItemClick(onItemClick = position -> {
            ViewTree viewTree = viewTreeAdapter.getViewTrees()[position];
            if (viewTree.getChildren() != null) {
                ViewTree[] viewTrees = viewTree.getChildren();
                viewTreeAdapter = new ViewTreeAdapter(viewTrees);
                viewTreeAdapter.setOnItemClick(onItemClick);
                viewTreeAdapter.setTargetViewTree(targetViewTree);
                recyclerView.setAdapter(viewTreeAdapter);
                tvTitle.setText("嵌套级别:"+(viewTrees[0].getLevel()+1));
            }
            btPre.setVisibility(View.VISIBLE);
        });
        recyclerView.setAdapter(viewTreeAdapter);
        btPre.setOnClickListener(v -> {
            ViewTree parent = viewTreeAdapter.getViewTrees()[0].getParent().getParent();
            ViewTree[] viewTrees;
            if(parent != null){
                viewTrees = parent.getChildren();
                tvTitle.setText("嵌套级别:"+(viewTrees[0].getLevel()+1));
            }else {
                viewTrees = new ViewTree[1];
                viewTrees[0] = viewTreeAdapter.getViewTrees()[0].getParent();
                tvTitle.setText("嵌套级别:"+(viewTrees[0].getLevel()+1));
                btPre.setVisibility(View.GONE);
            }
            viewTreeAdapter = new ViewTreeAdapter(viewTrees);
            viewTreeAdapter.setOnItemClick(onItemClick);
            viewTreeAdapter.setTargetViewTree(targetViewTree);
            recyclerView.setAdapter(viewTreeAdapter);

        });
    }

}
