package com.example.wifiopener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private SwitchCompat wifiSwitch;
    private WifiManager wifiManager;
    ListView wifiDevices;
    ArrayList<String>devices;
    ArrayList<ScanResult>wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiDevices =findViewById(R.id.lv_availableDevices);
        WifiAdapter wifiAdapter =new WifiAdapter();
        wifiDevices.setAdapter(wifiAdapter);

        wifiSwitch = findViewById(R.id.wifi_switch);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    wifiSwitch.setText(R.string.wifi_is_on);
                } else {
                    wifiManager.setWifiEnabled(false);
                    wifiSwitch.setText(R.string.wifi_is_off);
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }
    private final BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    wifiSwitch.setChecked(true);
                    wifiSwitch.setText(R.string.wifi_is_on);
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    wifiSwitch.setChecked(false);
                    wifiSwitch.setText(R.string.wifi_is_off);
                    break;
            }
        }
    };
    public class WifiAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return wifiList.size();
        }

        @Override
        public String getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DeviceViewHolder holder;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.list_item,parent,false);
                holder=new DeviceViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder=(DeviceViewHolder)convertView.getTag();
            }
            String device=getItem(position);
            holder.tv_wifiDevice.setText(device);
            return convertView;
        }
        class DeviceViewHolder {
            TextView tvDeviceName;
            DeviceViewHolder (View convertView){
                tvDeviceName=convertView.findViewById(R.id.tv_wifiDevice);
            }
        }
    }
}