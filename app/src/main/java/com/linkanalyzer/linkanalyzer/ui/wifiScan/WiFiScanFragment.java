package com.linkanalyzer.linkanalyzer.ui.wifiScan;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.linkanalyzer.linkanalyzer.R;
import com.linkanalyzer.linkanalyzer.databinding.FragmentSlideshowBinding;
import com.linkanalyzer.linkanalyzer.linkInfo.ScanWifi;

import java.util.LinkedHashMap;
import java.util.Map;

public class WiFiScanFragment extends Fragment {

    private WiFiScanViewModel wiFiScanViewModel;
    private FragmentSlideshowBinding binding;
    TableLayout table;
    Handler handler;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wiFiScanViewModel =
                new ViewModelProvider(this).get(WiFiScanViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        wiFiScanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                int count = 0;
                handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        ScanWifi scanWifi = new ScanWifi();
                        Map<String, String> wifi_scan_data = new LinkedHashMap<String, String>();
                        wifi_scan_data = scanWifi.scan_wifi(getView().getContext());
                        setScanData((LinkedHashMap<String, String>) wifi_scan_data);
                    }
                };
                if(count == 0){
                    handler.postDelayed(runnable, 100);
                }else {
                    handler.postDelayed(runnable, 30000);
                }
                count += 1;

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setScanData(LinkedHashMap<String, String> wifi_scan_data ){
        table = (TableLayout)getView().findViewById(R.id.table);
        table.removeAllViews();
        int i = 0;
        for (Map.Entry<String, String> entry : wifi_scan_data.entrySet()){
            TableRow tbrow = new TableRow(getActivity());
            if (i % 2 == 0) {
                tbrow.setBackgroundColor(Color.rgb(211, 211, 211));
            } else {
                tbrow.setBackgroundColor(Color.rgb(192, 192, 192));
            }

            tbrow.setBackgroundResource(R.drawable.border);
            tbrow.setPadding(0, 25, 0, 25);
            TextView key_view = new TextView(getActivity());
            TextView val_view = new TextView(getActivity());
            val_view.setText(entry.getValue());
            val_view.setTextSize(15);
            val_view.setTextColor(Color.BLACK);
            val_view.setGravity(Gravity.LEFT);
            val_view.setPadding(10,10,10, 0);
            tbrow.addView(val_view);
            table.addView(tbrow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            i = i + 1;

        }
    }
}