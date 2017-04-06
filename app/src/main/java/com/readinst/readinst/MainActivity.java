package com.readinst.readinst;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import com.readinst.dbconnector.DBconnect;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button LoginButton;
    TextView UserEmail;
    TextView Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LoginButton = (Button) findViewById(R.id.LoginButton);
        LoginButton.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        UserEmail = (TextView) findViewById(R.id.Email);
        String Email = UserEmail.getText().toString();
        Password = (TextView) findViewById(R.id.Password);
        String Pass = Password.getText().toString();
        String UserString  = Email + "--"+Pass;
        try {
            File file = new File(this.getFilesDir(), "current.usr");
            FileWriter writer = new FileWriter(file);
            writer.append(UserString);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // TODO implement login procedure

        DBconnect db_users = new DBconnect();
        db_users.insertUser(Email, Pass, BCrypt.gensalt(12),"null", AppConfig.TABLE_USERS);

    }
}
