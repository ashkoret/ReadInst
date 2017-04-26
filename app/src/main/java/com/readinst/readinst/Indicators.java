package com.readinst.readinst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.readinst.dbconnector.DBconnect;

public class Indicators extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicators);
        DBconnect db_users = new DBconnect();
        db_users.readUser(AppConfig.UserEmail, AppConfig.TABLE_USER_DEVS);
        // TODO Code the PC list retrieval here
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
