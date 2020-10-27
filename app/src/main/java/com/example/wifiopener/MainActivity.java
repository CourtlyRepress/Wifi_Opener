package com.example.wifiopener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private SwitchCompat wifiSwitch;
    private WifiManager wifiManager;
    ListView wifiDevices;//wifiList
//    ArrayList<String>devices;
    List<ScanResult> wifiList;//mywifiList


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiDevices =findViewById(R.id.lv_availableDevices);
//        wifiAdapter =new WifiAdapter();
//        wifiDevices.setAdapter(wifiAdapter);

        wifiSwitch = findViewById(R.id.wifi_switch);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiReceiver wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }else{
            scanWifiList();
        }
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

    private void scanWifiList() {
        wifiManager.startScan();
        wifiList= wifiManager.getScanResults();
        WifiAdapter wifiAdapter = new WifiAdapter();
        wifiDevices.setAdapter(wifiAdapter);
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
    public class WifiAdapter/*ListAdapter*/ extends BaseAdapter {
        /*
        Context context;
        LayoutInflater inflater;
        List<ScanResult>wifiList;
        public WifiAdapter(Context context,List<ScanResult>wifiList){
            this.context=context;
            this.wifiList=wifiList;
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        */
        @Override
        public int getCount() {
            return wifiList.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DeviceViewHolder holder;
//            View view=convertView;
            if(convertView == null){
//                view=inflater.inflate(R.layout.list_item,null);
                convertView = getLayoutInflater().inflate(R.layout.list_item,parent,false);
//                holder = new Holder();
                holder=new DeviceViewHolder(convertView);
//                holder.tvDeviceName=(TextView)convertView.findViewById(R.id.tv_wifiDevice);
                convertView.setTag(holder);
            }else{
                holder=(DeviceViewHolder)convertView.getTag();
            }
//            String device=getItem(position);
            holder.tvDeviceName.setText(wifiDevices.get(position).SSID);
            return convertView;
        }

        //Holder class defined
        class DeviceViewHolder {
            TextView tvDeviceName;//tvDetails
            DeviceViewHolder (View convertView){
                tvDeviceName=convertView.findViewById(R.id.tv_wifiDevice);
            }
        }
    }
    static class WifiReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}