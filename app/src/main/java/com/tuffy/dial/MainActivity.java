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
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author david
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private TextView testText;
    private static final int PERMISS_CONTACT = 1;
    private RecyclerView recyclerView;
    protected TextView phonenum;
    protected TabHost tabhost;
    protected EditText message_phone;

    /*** init function
     *
     */
    private void init() {
        addPermissions();
        initTabHost();
        initRecyclerView();
        initBtn();
    }

    private void initRecyclerView() {
        ArrayList<MyContacts> contacts = getAllContacts(MainActivity.this);
        phonenum = findViewById(R.id.phoneNum);
        ContactAdapter adapter = new ContactAdapter(contacts, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initBtn() {
        findViewById(R.id.btn_dial).setOnClickListener(this);
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
        findViewById(R.id.btn_sendmessage).setOnClickListener(this);
        message_phone = findViewById(R.id.message_phone);
    }


    private void initTabHost() {
        tabhost = findViewById(R.id.tabhost);
        tabhost.setup();
        LayoutInflater.from(this).inflate(R.layout.dial, tabhost.getTabContentView());
        LayoutInflater.from(this).inflate(R.layout.message, tabhost.getTabContentView());
        tabhost.addTab(tabhost.newTabSpec("tab01").setIndicator("打电话", null).setContent(R.id.dial));
        tabhost.addTab(tabhost.newTabSpec("message").setIndicator("发短信", null).setContent(R.id.message));
    }

    private void getPermissions() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float initx = 0;
        float currentx = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initx = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentx = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                /*左右滑动事件处理*/
                if ((currentx - initx) > 25) {
                    if (tabhost.getCurrentTab() != 0) {
                        tabhost.setCurrentTab(tabhost.getCurrentTab() - 1);
                    }
                } else if ((currentx - initx) < -25) {
                    if (tabhost.getCurrentTab() != tabhost.getTabContentView().getChildCount()) {
                        tabhost.setCurrentTab(tabhost.getCurrentTab() + 1);
                    }
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 动态权限
     */
    public void addPermissions() {
        String[] permissionsList = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS};
        ArrayList<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissionsList.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissionsList[i])
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissionsList[i]);
            }
        }
        //非初次进入App且已授权
        if (!mPermissionList.isEmpty()) {
            //请求权限方法
            //将List转为数组
            String[] permissionsNew = mPermissionList.toArray(new String[mPermissionList.size()]);
            //这个触发下面onRequestPermissionsResult这个回调
            ActivityCompat.requestPermissions(MainActivity.this, permissionsNew, PERMISS_CONTACT);
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
        switch (v.getId()) {
            case R.id.btn_dial:
                // 已经获得授权，可以打电话
                Intent dialIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phonenum.getText()));
                startActivity(dialIntent);
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
                phonenum.setText(phonenum.getText() + ((Button) v).getText().toString());
                break;
            case R.id.btn_back:
                if (!"".equals(phonenum.getText())){
                    phonenum.setText(phonenum.getText().subSequence(0, phonenum.getText().length() - 1));
                }
                break;
            case R.id.btn_sendmessage:
                EditText messagePhone = findViewById(R.id.message_phone);
                EditText messageContent = findViewById(R.id.message_content);
                String number = messagePhone.getText().toString();
                String content = messageContent.getText().toString();
                if(!"".equals(number) && !"".equals(content)){
                    ArrayList<String> message = SmsManager.getDefault().divideMessage(content);
                    for (String text : message){
                        SmsManager.getDefault().sendTextMessage(number, null, text, null, null);
                    }
                    messageContent.setText("");
                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "手机号码或者短信内容不可以为空", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
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
        Comparator<MyContacts> comparator = new Comparator<MyContacts>() {
            @Override
            public int compare(MyContacts o1, MyContacts o2) {
                char[] char1 = FirstCharUtil.first(o1.getName()).toCharArray();
                char[] char2 = FirstCharUtil.first(o2.getName()).toCharArray();
                int result = (int)char1[0] - (int)char2[0];
                return result;
            }
        };
        cursor.close();
        contacts.sort(comparator);
        return contacts;
    }

}
