package com.example.componentapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AlertDialogActivity extends AppCompatActivity {
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

        btnBackToMain = findViewById(R.id.btn_back_to_main);

        // 点击按钮弹出自定义对话框
        Button btnShowDialog = findViewById(R.id.btn_show_dialog);
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog();
            }
        });

        // 返回主界面按钮点击事件
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到MainActivity的Intent
                Intent intent = new Intent(AlertDialogActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 关闭当前Activity，避免返回栈堆积
            }
        });
    }

    // 显示自定义AlertDialog（文档“调用setView()添加布局”要求）
    private void showCustomAlertDialog() {
        // 1. 加载自定义布局
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null);

        // 2. 创建AlertDialog.Builder并设置布局
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)  // 核心：添加自定义布局
                .setTitle("User Login");  // 对话框标题（可选）

        // 3. 获取布局中的控件（用户名、密码、按钮）
        EditText etUsername = dialogView.findViewById(R.id.et_username);
        EditText etPassword = dialogView.findViewById(R.id.et_password);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSignIn = dialogView.findViewById(R.id.btn_signin);

        // 4. 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();

        // 5. 绑定Cancel按钮事件（关闭对话框）
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 6. 绑定Sign in按钮事件（验证输入并提示）
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // 简单输入验证
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AlertDialogActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AlertDialogActivity.this, "登录成功！用户名：" + username, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();  // 关闭对话框
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
