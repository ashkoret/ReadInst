package com.readinst.readinst;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import 	android.os.StrictMode;
import android.widget.Button;
import android.view.View;
import com.readinst.dbconnector.DBconnect;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {

        DBconnect db_users = new DBconnect();
        db_users.insertUser("el.rache@gmail.com","123123", BCrypt.gensalt(12),"27FA-AE34-1FC5-EECA-4E48-D19D-DFBA-4B68", AppConfig.TABLE_USERS);
    }
}
