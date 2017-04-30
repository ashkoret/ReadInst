package com.readinst.readinst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.readinst.dbconnector.DBconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
public class Indicators extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicators);
        HashMap<String, String> UserDevs;
        LinkedHashMap<String, String> DevIndicators;
        DBconnect db_users = new DBconnect();
        UserDevs = db_users.readUser(AppConfig.UserEmail, AppConfig.TABLE_USER_DEVS);
        ArrayList<String> DevNameList = new ArrayList<>(UserDevs.keySet());
        ArrayList<String> DevList = new ArrayList<>(UserDevs.values());
        ArrayList<ArrayList<String>> Indicators = new ArrayList<>();

        for (int i = 0; i<DevList.size(); i++)
        {
            String DeviceID = DevList.get(i);
            DevIndicators = db_users.readDeviceIndicators(DeviceID, AppConfig.TABLE_DEVICES);
            /*if (i==0)
            {
            Indicators.add(new ArrayList<>(DevIndicators.keySet()));
            }*/
            Indicators.add(new ArrayList<>(DevIndicators.values()));
            boolean foo = true;
            // TODO split the UserDevs in two lists of Devs and DevNames
            // TODO get all the Instruments from all the DEVs one by one Dev from the UserDevs
        }

        // TODO Code the indicator list retrieval here
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
}
