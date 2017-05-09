package com.readinst.readinst;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.readinst.barcode.BarcodeCaptureActivity;
import com.readinst.dbconnector.DBconnect;
import com.readinst.readinst.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

//import static com.readinst.readinst.AppConfig.BARCODE_READER_REQUEST_CODE;

public class Indicators extends AppCompatActivity
{
    private static final String LOG_TAG = Indicators.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    static int totalEditTexts = 0;
    LinearLayout IndicatorLayout;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicators);


        HashMap<String, String> UserDevs;
        LinkedHashMap<String, String> DevIndicators;
        DBconnect db_users = new DBconnect();
        UserDevs = db_users.readUser(AppConfig.UserEmail, AppConfig.TABLE_USER_DEVS);
        ArrayList<String> DevList = new ArrayList<>(UserDevs.values());
        ArrayList<String> DevListNames = new ArrayList<>(UserDevs.keySet());
        ArrayList<ArrayList<String>> Indicators = new ArrayList<>();
        IndicatorLayout = (LinearLayout) findViewById(R.id.indicators);
        for (int i = 0; i<DevList.size(); i++)
        {
            String DeviceID = DevList.get(i);
            DevIndicators = db_users.readDeviceIndicators(DeviceID, AppConfig.TABLE_DEVICES);
            Indicators.add(new ArrayList<>(DevIndicators.values()));
        }

        ConstraintSet set = new ConstraintSet();
        ArrayList<EditText> TextViews = new ArrayList<>();

        for (int i = 0; i<DevList.size(); i++) {
            EditText editTextPC = new EditText(getBaseContext());
            editTextPC.setText(DevListNames.get(i));
            editTextPC.setTextColor(Color.rgb(0,0,152));
            editTextPC.setGravity(Gravity.CENTER);
            TextViews.add(editTextPC);
            IndicatorLayout.addView(editTextPC);
            for (int j = 0; j < Indicators.get(i).size(); j++)
            {
                String DevName = Indicators.get(i).get(j).replace("\r\n","").replace("\r","").replace("\n","");

                if (!DevName.equals("0"))
                {
                    EditText editText = new EditText(getBaseContext());
                    editText.setTag("Indicator" + totalEditTexts);
                    if (j == Math.ceil(j/6)*6)
                    {
                        editText.setTextColor(Color.rgb(152,0,0));
                    }
                    else if ((j-1) == Math.ceil((j-1)/6)*6)
                    {
                        editText.setTextColor(Color.rgb(0,102,0));
                    }
                    else
                    {
                        editText.setTextColor(Color.rgb(64,64,64));
                    }
                    editText.setId(i * 6 + j);
                    editText.setText(DevName);
                    TextViews.add(editText);
                    IndicatorLayout.addView(editText);
                    totalEditTexts++;
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.AddPC:
                Toast.makeText(Indicators.this, "Add PC using QR", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
                return true;
            case R.id.AddPCman:
                Toast.makeText(Indicators.this, "Add PC manually", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Logout:
                Toast.makeText(Indicators.this, "Log out", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Exit:
                Toast.makeText(Indicators.this, "Exit", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    Toast.makeText(Indicators.this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(Indicators.this, R.string.no_barcode_captured, Toast.LENGTH_SHORT).show();
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }
// TODO add floating menu with EditText device name, return PC-ID to the menu and to the list of the devices.
// TODO Describe Log-off, Exit, Remove Manual add PC

}
