package com.yalemang.library.android.viewutils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
    private TextView tvTitle,tvContent,tvError;
    private Button btPre,btCancel;
    private ViewTree viewTree;
    private ViewTree targetViewTree;
    private ViewTreeAdapter viewTreeAdapter;
    private ViewTreeAdapter.OnItemClick onItemClick;
    private int times = 0;
    private Runnable runnable;
    private ViewTreeAdapter.OnItemLongClick onItemLongClick = position -> {
        ViewTree vt = viewTreeAdapter.getViewTrees()[position];
        Drawable bgDrawable = vt.getView().getBackground();
        vt.getView().setBackgroundColor(Color.RED);
        times = 0;
        //这里定位视图用一个图层来做
        Handler handler = new Handler();
        //闪烁动画的实现
        handler.postDelayed(runnable = () -> {
            times++;
            if(times != 6) {
                if (times % 2 == 0) {
                    vt.getView().setBackgroundColor(Color.RED);
                } else {
                    vt.getView().setBackground(bgDrawable);
                }
                handler.postDelayed(runnable, 500);
            }else {
                times = 0;
            }
        },500);
        bottomSheetDialog.dismiss();
    };
    private int currentViewDepth = -1;

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
        tvError = contentView.findViewById(R.id.tv_error);
        tvContent = contentView.findViewById(R.id.tv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        if(viewTree.getParent() != null){
            //非根部
            ViewTree[] viewTrees = viewTree.getChildren();
            viewTreeAdapter = new ViewTreeAdapter(viewTrees);
            int currentLevel = (viewTree.getLevel()+2);
            tvTitle.setText("嵌套级别:"+currentLevel);
            tvContent.setText(getViewTreeMessage(viewTrees[0].getParent()));
            currentViewDepth = currentLevel - 5;
            if(currentViewDepth >= ViewUtils.TARGET_VIEW_DEPTH){
                //已超过预期
                tvError.setVisibility(View.VISIBLE);
            }else {
                tvError.setVisibility(View.GONE);
            }
        }else {
            //根部
            ViewTree[] rootViewTrees = new ViewTree[1];
            rootViewTrees[0] = viewTree;
            viewTreeAdapter = new ViewTreeAdapter(rootViewTrees);
            int currentLevel = viewTree.getLevel() + 1;
            tvTitle.setText("嵌套级别:"+currentLevel);
            tvContent.setText("当前是根视图!");
            currentViewDepth = currentLevel - 5;
            if(currentViewDepth >= ViewUtils.TARGET_VIEW_DEPTH){
                //已超过预期
                tvError.setVisibility(View.VISIBLE);
            }else {
                tvError.setVisibility(View.GONE);
            }
        }
        viewTreeAdapter.setTargetViewTree(targetViewTree);
        viewTreeAdapter.setOnItemLongClick(onItemLongClick);
        viewTreeAdapter.setOnItemClick(onItemClick = position -> {
            ViewTree viewTree = viewTreeAdapter.getViewTrees()[position];
            if (viewTree.getChildren() != null) {
                ViewTree[] viewTrees = viewTree.getChildren();
                tvContent.setText(getViewTreeMessage(viewTrees[0].getParent()));
                viewTreeAdapter = new ViewTreeAdapter(viewTrees);
                viewTreeAdapter.setOnItemClick(onItemClick);
                viewTreeAdapter.setOnItemLongClick(onItemLongClick);
                viewTreeAdapter.setTargetViewTree(targetViewTree);
                recyclerView.setAdapter(viewTreeAdapter);
                int currentLevel = viewTrees[0].getLevel() + 1;
                tvTitle.setText("嵌套级别:"+currentLevel);
                btPre.setVisibility(View.VISIBLE);
                currentViewDepth = currentLevel - 5;
                if(currentViewDepth >= ViewUtils.TARGET_VIEW_DEPTH){
                    //已超过预期
                    tvError.setVisibility(View.VISIBLE);
                }else {
                    tvError.setVisibility(View.GONE);
                }
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
            int currentLevel = viewTrees[0].getLevel()+1;
            tvTitle.setText("嵌套级别:"+currentLevel);
            currentViewDepth = currentLevel - 5;
            if(currentViewDepth >= ViewUtils.TARGET_VIEW_DEPTH){
                //已超过预期
                tvError.setVisibility(View.VISIBLE);
            }else {
                tvError.setVisibility(View.GONE);
            }
            viewTreeAdapter = new ViewTreeAdapter(viewTrees);
            viewTreeAdapter.setOnItemClick(onItemClick);
            viewTreeAdapter.setOnItemLongClick(onItemLongClick);
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
