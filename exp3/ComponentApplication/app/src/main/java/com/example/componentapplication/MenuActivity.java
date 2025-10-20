package com.example.componentapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private TextView tvTest;  // 测试文本控件（用于修改字体样式）
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnBackToMain = findViewById(R.id.btn_back_to_main);
        tvTest = findViewById(R.id.tv_test);

        // 返回主界面按钮点击事件
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到MainActivity的Intent
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 关闭当前Activity，避免返回栈堆积
            }
        });
    }

    // 加载菜单（将XML菜单 Inflate 到界面）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 处理菜单项点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.size_small) {
            tvTest.setTextSize(10); // 设置10号字
            return true;
        } else if (id == R.id.size_medium) {
            tvTest.setTextSize(16); // 设置16号字
            return true;
        } else if (id == R.id.size_large) {
            tvTest.setTextSize(20); // 设置20号字
            return true;
        } else if (id == R.id.menu_normal) {
            Toast.makeText(this, "点击了普通菜单项", Toast.LENGTH_SHORT).show(); // Toast提示
            return true;
        } else if (id == R.id.color_red) {
            tvTest.setTextColor(Color.RED); // 设置红色
            return true;
        } else if (id == R.id.color_black) {
            tvTest.setTextColor(Color.BLACK); // 设置黑色
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}