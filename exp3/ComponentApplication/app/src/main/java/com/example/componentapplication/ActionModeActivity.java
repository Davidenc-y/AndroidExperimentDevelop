package com.example.componentapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActionModeActivity extends AppCompatActivity {

    private ListView lvActionMode;
    private Button btnBackToMain;
    private CustomAdapter adapter;
    private List<String> dataList;
    private List<Integer> selectedPositions = new ArrayList<>();

    private CheckBox cbSelectAll;
    private TextView tvSelectedCount;
    private ImageView ivDelete;

    // 全选复选框监听器
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = (buttonView, isChecked) -> {
        boolean isAlreadyFullSelected = selectedPositions.size() == dataList.size();
        if (isChecked && isAlreadyFullSelected) {
            return; // 过滤被动触发
        }

        if (isChecked) {
            // 全选：同步自定义列表 + 系统状态
            selectedPositions.clear();
            for (int i = 0; i < dataList.size(); i++) {
                selectedPositions.add(i);
                lvActionMode.setItemChecked(i, true);
            }
        } else {
            // 取消全选：同步自定义列表 + 系统状态
            selectedPositions.clear();
            for (int i = 0; i < dataList.size(); i++) {
                lvActionMode.setItemChecked(i, false);
            }
        }
        adapter.notifyDataSetChanged();
        lvActionMode.invalidateViews(); // 强制刷新
        updateSelectedCount();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_mode);

        // 绑定控件
        btnBackToMain = findViewById(R.id.btn_back_to_main);
        lvActionMode = findViewById(R.id.lv_actionmode);
        lvActionMode.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // 开启系统多选
        cbSelectAll = findViewById(R.id.cb_select_all);
        tvSelectedCount = findViewById(R.id.tv_selected_count);
        ivDelete = findViewById(R.id.iv_delete);

        // 列表数据
        dataList = new ArrayList<>();
        dataList.add("One");
        dataList.add("Two");
        dataList.add("Three");
        dataList.add("Four");
        dataList.add("Five");

        // 初始化适配器
        adapter = new CustomAdapter(
                this,
                R.layout.item_actionmode,
                R.id.tv_text,
                dataList,
                selectedPositions
        );
        lvActionMode.setAdapter(adapter);

        // 绑定全选监听器
        cbSelectAll.setOnCheckedChangeListener(checkedChangeListener);

        // 返回主界面
        btnBackToMain.setOnClickListener(v -> {
            startActivity(new Intent(ActionModeActivity.this, MainActivity.class));
            finish();
        });

        // 删除按钮逻辑
        ivDelete.setOnClickListener(v -> {
            Toast.makeText(this, "删除了 " + selectedPositions.size() + " 项", Toast.LENGTH_SHORT).show();
            // 倒序删除数据
            for (int i = selectedPositions.size() - 1; i >= 0; i--) {
                dataList.remove(selectedPositions.get(i).intValue());
            }
            // 取消所有系统选中状态
            for (int i = 0; i < dataList.size(); i++) {
                lvActionMode.setItemChecked(i, false);
            }
            selectedPositions.clear();
            adapter.notifyDataSetChanged();
            lvActionMode.invalidateViews(); // 强制刷新
            updateSelectedCount();
        });

        // 列表项点击事件（短按/长按）
        lvActionMode.setOnItemClickListener((parent, view, position, id) -> {
            toggleSelection(position, view);
        });
        lvActionMode.setOnItemLongClickListener((parent, view, position, id) -> {
            toggleSelection(position, view);
            return true;
        });
    }

    private void toggleSelection(int position, View clickView) {
        // 确认列表项点击事件触发
        Toast.makeText(this, "列表项 " + position + " 被点击", Toast.LENGTH_SHORT).show();

        // 原有逻辑（遍历判断选中、同步系统状态...）
        boolean isChecked = false;
        for (int i = 0; i < selectedPositions.size(); i++) {
            if (selectedPositions.get(i) == position) {
                isChecked = true;
                break;
            }
        }

        lvActionMode.setItemChecked(position, !isChecked);

        if (isChecked) {
            for (int i = 0; i < selectedPositions.size(); i++) {
                if (selectedPositions.get(i) == position) {
                    selectedPositions.remove(i);
                    break;
                }
            }
            clickView.setActivated(false);
        } else {
            selectedPositions.add(position);
            clickView.setActivated(true);
        }

        adapter.notifyDataSetChanged();
        lvActionMode.invalidateViews();
        updateSelectedCount();
    }

    // 更新选中数和全选状态
    private void updateSelectedCount() {
        tvSelectedCount.setText(selectedPositions.size() + " selected");
        // 临时移除监听器，避免循环触发
        cbSelectAll.setOnCheckedChangeListener(null);
        cbSelectAll.setChecked(selectedPositions.size() == dataList.size());
        cbSelectAll.setOnCheckedChangeListener(checkedChangeListener);
    }
}