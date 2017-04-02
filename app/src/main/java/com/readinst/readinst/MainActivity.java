package com.readinst.readinst;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import 	android.os.StrictMode;
import com.readinst.dbconnector.DBconnect;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DBconnect db_users = new DBconnect();
        db_users.insertUser("el.rache@gmail.com","123123", AppConfig.TABLE_USERS);
    }
}
