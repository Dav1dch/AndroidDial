package com.tuffy.dial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author david
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private TextView testText;
    private static final int PERMISS_CONTACT = 1;
    private RecyclerView recyclerView;
    private TextView phonenum;
    private void init(){
        ArrayList<MyContacts> contacts = getAllContacts(MainActivity.this);
        findViewById(R.id.dial).setOnClickListener(this);
        String[] permissList = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};
        addPermissByPermissionList(MainActivity.this, permissList, PERMISS_CONTACT);
        phonenum = findViewById(R.id.phoneNum);
        ContactAdapter adapter = new ContactAdapter(contacts, phonenum);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        findViewById(R.id.btn_one).setOnClickListener(this);
        findViewById(R.id.btn_two).setOnClickListener(this);
        findViewById(R.id.btn_three).setOnClickListener(this);
        findViewById(R.id.btn_four).setOnClickListener(this);
        findViewById(R.id.btn_five).setOnClickListener(this);
        findViewById(R.id.btn_six).setOnClickListener(this);
        findViewById(R.id.btn_seven).setOnClickListener(this);
        findViewById(R.id.btn_eight).setOnClickListener(this);
        findViewById(R.id.btn_nine).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    /**
     * 动态权限
     */
    public void addPermissByPermissionList(Activity activity, String[] permissions, int request) {
        //Android 6.0开始的动态权限，这里进行版本判断
            ArrayList<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            //非初次进入App且已授权
            if (!mPermissionList.isEmpty()) {
                //请求权限方法
                //将List转为数组
                String[] permissionsNew = mPermissionList.toArray(new String[mPermissionList.size()]);
                //这个触发下面onRequestPermissionsResult这个回调
                ActivityCompat.requestPermissions(activity, permissionsNew, request);
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.dial:
               if (ContextCompat.checkSelfPermission(MainActivity.this,
                       Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                   // 没有获得授权，申请授权
                   if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                           Manifest.permission.CALL_PHONE)) {

                       // 帮跳转到该应用的设置界面，让用户手动授权
                       Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                       Uri uri = Uri.fromParts("package", getPackageName(), null);
                       intent.setData(uri);
                       startActivity(intent);
                   }else{
                       // 不需要解释为何需要该权限，直接请求授权
                       ActivityCompat.requestPermissions(MainActivity.this,
                               new String[]{Manifest.permission.CALL_PHONE},
                               MY_PERMISSIONS_REQUEST_CALL_PHONE);
                   }
               }else {
                   // 已经获得授权，可以打电话
                   Intent dialIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phonenum.getText()));
                   startActivity(dialIntent);
               }
               break;
           case R.id.btn_one:
           case R.id.btn_two:
           case R.id.btn_three:
           case R.id.btn_four:
           case R.id.btn_five:
           case R.id.btn_six:
           case R.id.btn_seven:
           case R.id.btn_eight:
           case R.id.btn_nine:
               phonenum.setText(phonenum.getText() + ((Button)v).getText().toString());
               break;
           case R.id.btn_back:
               phonenum.setText(phonenum.getText().subSequence(0, phonenum.getText().length()-1));
               break;
               default:
                   break;
       }
    }
    public static ArrayList<MyContacts> getAllContacts(Context context) {
        ArrayList<MyContacts> contacts = new ArrayList<MyContacts>();

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //新建一个联系人实例
            MyContacts temp = new MyContacts();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            temp.setName(name);

            //获取联系人电话号码
            Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replace("-", "");
                phone = phone.replace(" ", "");
                temp.setNum(phone);
            }
            contacts.add(temp);
            //记得要把cursor给close掉
            phoneCursor.close();
        }
        cursor.close();
        return contacts;
    }
}
