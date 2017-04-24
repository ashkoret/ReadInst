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
import java.io.FileReader;
import java.io.BufferedReader;
import android.content.Intent;
import java.util.Collections;
import java.util.List;

import com.readinst.dbconnector.DBconnect;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button LoginButton;
    TextView UserEmail;
    TextView UserPassword;
    String UsrFilename = AppConfig.USR_FILE;
    String Email = "";
    String HashedPassword = "";
    String TypedPassword = "";
    String Salt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LoginButton = (Button) findViewById(R.id.LoginButton);
        LoginButton.setOnClickListener(MainActivity.this);


        try {
            File file = new File(this.getFilesDir(), UsrFilename);
            // Check file exists and read the stuff from file
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String ReadString;
                if (((ReadString = br.readLine()) != null ) && ReadString.length() > 3)
                {
                    String[] Credentials = ReadString.split("--");
                    if ((Credentials[0]!= null) && (Credentials.length == 3))
                    {
                        Email = Credentials[0];
                        HashedPassword = Credentials[1];
                        Salt =  Credentials[2];

                        UserEmail = (TextView) findViewById(R.id.Email);
                        UserEmail.setText(Email);
                        DBconnect db_users = new DBconnect();
                        AppConfig.UserExists = db_users.checkUser(Email, HashedPassword, AppConfig.TABLE_USERS);
                        if (AppConfig.UserExists[0] & AppConfig.UserExists[1])
                        {
                            Intent intent = new Intent(this, Indicators.class);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                       file.delete();
                    }
                }
                br.close();

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        UserEmail = (TextView) findViewById(R.id.Email);
        Email = UserEmail.getText().toString();
        UserPassword = (TextView) findViewById(R.id.Password);
        TypedPassword = UserPassword.getText().toString();

        if (!AppConfig.UserExists[0]) {
            DBconnect db_users = new DBconnect();
            Salt = db_users.getUserSalt(Email, AppConfig.TABLE_USERS);
            if (!Salt.equals("NULL"))
            {
                HashedPassword = BCrypt.hashpw(TypedPassword, Salt);
                TypedPassword = "NULL";
                AppConfig.UserExists = db_users.checkUser(Email, HashedPassword, AppConfig.TABLE_USERS);

                if (AppConfig.UserExists[1]) {
                    String UserString = Email + "--" + HashedPassword + "--" + Salt;
                    try {
                        File file = new File(this.getFilesDir(), UsrFilename);
                        FileWriter writer = new FileWriter(file);
                        writer.append(UserString);
                        writer.flush();
                        writer.close();
                        AppConfig.UserExists[0] = true;
                        AppConfig.UserExists[1] = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, Indicators.class);
                    startActivity(intent);
                }
            }

            else
            {
                Salt = BCrypt.gensalt(12);
                HashedPassword = BCrypt.hashpw(TypedPassword, Salt);
                TypedPassword = "NULL";
                db_users.insertUser(Email, HashedPassword, Salt, "NULL", "NULL", AppConfig.TABLE_USERS);
                String UserString = Email + "--" + HashedPassword + "--" + Salt;
                try {
                    File file = new File(this.getFilesDir(), UsrFilename);
                    FileWriter writer = new FileWriter(file);
                    writer.append(UserString);
                    writer.flush();
                    writer.close();
                    AppConfig.UserExists[0] = true;
                    AppConfig.UserExists[1] = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, Indicators.class);
                startActivity(intent);
            }
        }

        else {
            if (BCrypt.checkpw(TypedPassword, HashedPassword)) {
                Intent intent = new Intent(this, Indicators.class);
                startActivity(intent);
            }
        }

    }

// TODO Move PullDeviceList
    public List<String> PulldeviceList(String Email, String Table)
    {
        DBconnect db_users = new DBconnect();
        List<String> AllDevs = Collections.emptyList();

        if (AppConfig.UserExists[1])
        {
            AllDevs = db_users.readUser(Email, Table);
        }

        return AllDevs;
    }
}
