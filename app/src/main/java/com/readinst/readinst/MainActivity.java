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

import com.readinst.dbconnector.DBconnect;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button LoginButton;
    TextView UserEmail;
    TextView UserPassword;
    String UsrFilename = AppConfig.USR_FILE;
    String Email = "";
    String HashedPassword = "";
    String Salt = "";
    Boolean UserExists = false;

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
                    if (Credentials[0]!= null)
                    {
                        Email = Credentials[0];
                        HashedPassword = Credentials[1];
                        Salt =  Credentials[2];
                        UserEmail = (TextView) findViewById(R.id.Email);
                        UserEmail.setText(Email);
                        UserExists = true;
                    }
                }
                br.close();
                // TODO Read file
                // Intent intent = new Intent(this, Indicators.class);
                // startActivity(intent);
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
        String Email = UserEmail.getText().toString();
        UserPassword = (TextView) findViewById(R.id.Password);
        String TypedPassword = UserPassword.getText().toString();

        if (!UserExists) {
            Salt = BCrypt.gensalt(12);
            String TypedHashedPassword = BCrypt.hashpw(UserPassword.getText().toString(), Salt);
            String UserString = Email + "--" + TypedHashedPassword + "--" + Salt;
            try {
                File file = new File(this.getFilesDir(), UsrFilename);
                FileWriter writer = new FileWriter(file);
                writer.append(UserString);
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // TODO implement login procedure
        if(BCrypt.checkpw(TypedPassword,HashedPassword))
        {
            DBconnect db_users = new DBconnect();
            db_users.insertUser(Email, HashedPassword, Salt,"null", AppConfig.TABLE_USERS);

            Intent intent = new Intent(this, Indicators.class);
            startActivity(intent);
        }

    }
}
