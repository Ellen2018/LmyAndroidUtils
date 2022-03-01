package com.yalemang.library.android.viewutils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView tvTitle,tvContent;
    private Button btPre,btCancel;
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
        btCancel = contentView.findViewById(R.id.bt_cancel);
        tvTitle = contentView.findViewById(R.id.tv_title);
        tvContent = contentView.findViewById(R.id.tv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        if(viewTree.getParent() != null){
            //非根部
            ViewTree[] viewTrees = viewTree.getChildren();
            viewTreeAdapter = new ViewTreeAdapter(viewTrees);
            tvTitle.setText("嵌套级别:"+(viewTree.getLevel()+2));
            tvContent.setText(getViewTreeMessage(viewTrees[0].getParent()));
        }else {
            //根部
            ViewTree[] rootViewTrees = new ViewTree[1];
            rootViewTrees[0] = viewTree;
            viewTreeAdapter = new ViewTreeAdapter(rootViewTrees);
            tvTitle.setText("嵌套级别:"+(viewTree.getLevel()+1));
            tvContent.setText("当前是根视图!");
        }
        viewTreeAdapter.setTargetViewTree(targetViewTree);
        viewTreeAdapter.setOnItemClick(onItemClick = position -> {
            ViewTree viewTree = viewTreeAdapter.getViewTrees()[position];
            if (viewTree.getChildren() != null) {
                ViewTree[] viewTrees = viewTree.getChildren();
                tvContent.setText(getViewTreeMessage(viewTrees[0].getParent()));
                viewTreeAdapter = new ViewTreeAdapter(viewTrees);
                viewTreeAdapter.setOnItemClick(onItemClick);
                viewTreeAdapter.setTargetViewTree(targetViewTree);
                recyclerView.setAdapter(viewTreeAdapter);
                tvTitle.setText("嵌套级别:"+(viewTrees[0].getLevel()+1));
                btPre.setVisibility(View.VISIBLE);
            }else if(viewTree.getView() instanceof ViewStub){
                Toast.makeText(btPre.getContext(),"ViewStub找不到子级",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(btPre.getContext(),"View视图没有子级",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(viewTreeAdapter);
        btPre.setOnClickListener(v -> {
            if(viewTreeAdapter.getViewTrees()[0].getParent() == null){
                Toast.makeText(btPre.getContext(),"根视图无父级",Toast.LENGTH_SHORT).show();
                return;
            }
            ViewTree parent = viewTreeAdapter.getViewTrees()[0].getParent().getParent();
            ViewTree[] viewTrees;
            if(parent != null){
                viewTrees = parent.getChildren();
                tvContent.setText(getViewTreeMessage(parent));
            }else {
                viewTrees = new ViewTree[1];
                viewTrees[0] = viewTreeAdapter.getViewTrees()[0].getParent();
                tvContent.setText("当前是根视图!");
            }
            tvTitle.setText("嵌套级别:"+(viewTrees[0].getLevel()+1));
            viewTreeAdapter = new ViewTreeAdapter(viewTrees);
            viewTreeAdapter.setOnItemClick(onItemClick);
            viewTreeAdapter.setTargetViewTree(targetViewTree);
            recyclerView.setAdapter(viewTreeAdapter);

        });
        btCancel.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            bottomSheetDialog = null;
        });
    }

    private String getViewTreeMessage(ViewTree viewTree){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("父控件:\n");
        stringBuilder.append(viewTree.getView().getClass().getName());
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

}
