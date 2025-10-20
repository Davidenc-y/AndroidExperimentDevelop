package com.example.componentapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    private List<Integer> selectedPositions;
    // 引入颜色资源（避免硬编码）
    private int colorSelected;
    private int colorDefault;

    public CustomAdapter(Context context, int resource, int textViewResourceId,
                         List<String> objects, List<Integer> selectedPositions) {
        super(context, resource, textViewResourceId, objects);
        this.selectedPositions = selectedPositions;
        // 初始化颜色（从资源中获取，确保和选择器一致）
        colorSelected = context.getResources().getColor(R.color.blue_selected, context.getTheme());
        colorDefault = context.getResources().getColor(R.color.white, context.getTheme());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_actionmode, parent, false);
        }

        TextView tvText = convertView.findViewById(R.id.tv_text);
        tvText.setText(getItem(position));

        // 1. 打印日志：确认系统选中状态是否正确
        boolean isActivated = ((ListView) parent).isItemChecked(position);
        // 查看 Logcat（筛选“AdapterState”），确认选中时isActivated为true
        android.util.Log.d("AdapterState", "位置" + position + "：isActivated=" + isActivated);

        // 2. 先通过系统状态设置 activated（触发选择器）
        convertView.setActivated(isActivated);

        // 3. 兜底：若选择器仍不生效，直接设置背景色（用于排查问题）
        if (isActivated) {
            convertView.setBackgroundColor(colorSelected); // 直接设蓝色
        } else {
            convertView.setBackgroundColor(colorDefault); // 直接设白色
        }

        return convertView;
    }
}