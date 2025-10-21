package com.example.componentapplication;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalListActivity extends AppCompatActivity {

    // 1. 数据准备
    private final String[] animalNames = {"Tiger", "Lion", "Monkey", "Elephant", "Cat", "Dog"};
    private final int[] animalImages = {
            R.drawable.tiger,
            R.drawable.lion,
            R.drawable.monkey,
            R.drawable.elephant,
            R.drawable.cat,
            R.drawable.dog
    };

    // 通知相关配置
    private static final String CHANNEL_ID = "animal_channel";
    private ListView lvAnimalList;
    private Button btnBackToMain;
    private Button btnDynamicAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        lvAnimalList = findViewById(R.id.lv_animal_list);
        btnBackToMain = findViewById(R.id.btn_back_to_main);
        btnDynamicAnimal = findViewById(R.id.btn_dynamic_animal); // 新增：绑定底部按钮
        btnDynamicAnimal.setText("Please select"); // 新增：设置初始文本

        // 2. 适配数据到SimpleAdapter
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < animalNames.length; i++) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("image", animalImages[i]);  // 绑定图片资源
            itemMap.put("name", animalNames[i]);    // 绑定名称
            dataList.add(itemMap);
        }

        // 3. 创建SimpleAdapter，关联数据与列表项布局
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                dataList,
                R.layout.item_animal,  // 列表项布局
                new String[]{"image", "name"},  // 数据key
                new int[]{R.id.iv_animal, R.id.tv_animal_name}  // 布局控件ID
        );

        lvAnimalList.setAdapter(adapter);

        lvAnimalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAnimal = animalNames[position];

                // 切换选中状态（点击一次选中，再点击取消）
                boolean isSelected = !view.isSelected();
                view.setSelected(isSelected); // 关键：更新状态

                // 显示Toast
                Toast.makeText(
                        AnimalListActivity.this,
                        "Target：" + selectedAnimal + (isSelected ? "（Selected）" : "（Unselected）"),
                        Toast.LENGTH_LONG
                ).show();

                btnDynamicAnimal.setText(selectedAnimal);
            }
        });

        // 返回主界面按钮点击事件
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到MainActivity的Intent
                Intent intent = new Intent(AnimalListActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 关闭当前Activity，避免返回栈堆积
            }
        });

        // 初始化通知渠道（Android 8.0+必需）
        createNotificationChannel();
    }

    // 创建通知渠道（适配Android 8.0及以上）
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "动物列表通知",  // 渠道名称
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("显示动物列表的选中通知");  // 渠道描述
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // （4）发送通知：图标为应用图标，标题为列表项内容
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void sendNotification(String animalName) {
        // 点击通知跳转的意图（此处跳回当前页面）
        Intent intent = new Intent(this, AnimalListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        // 构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)  // （4）通知图标：应用程序图标
                .setContentTitle(animalName)         // （4）通知标题：列表项内容
                .setContentText("你查看了[" + animalName + "]的信息")  // （4）通知内容自拟
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)  // 点击后自动取消
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 发送通知
        NotificationManagerCompat.from(this).notify(1, builder.build());
    }
}