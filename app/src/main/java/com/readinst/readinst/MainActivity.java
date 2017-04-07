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
                    if ((Credentials[0]!= null) && (Credentials.length == 3))
                    {
                        Email = Credentials[0];
                        HashedPassword = Credentials[1];
                        Salt =  Credentials[2];
                        UserEmail = (TextView) findViewById(R.id.Email);
                        UserEmail.setText(Email);
                        UserExists = true;
                    }
                    else
                    {
                       file.delete();
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

    // TODO
    // TODO implement login procedure
    // TODO

    @Override
    public void onClick(View v) {
        UserEmail = (TextView) findViewById(R.id.Email);
        String Email = UserEmail.getText().toString();
        UserPassword = (TextView) findViewById(R.id.Password);
        String TypedPassword = UserPassword.getText().toString();

        if (!UserExists) {
            // TODO check user in DB
            // TODO if exists, get salt
            // TODO if not - then put him in DB and this:
            connectAndPull(Email, HashedPassword, AppConfig.TABLE_USERS);
            String TypedHashedPassword = BCrypt.hashpw(UserPassword.getText().toString(), Salt);
            String UserString = Email + "--" + TypedHashedPassword + "--" + Salt;
            try {
                File file = new File(this.getFilesDir(), UsrFilename);
                FileWriter writer = new FileWriter(file);
                writer.append(UserString);
                writer.flush();
                writer.close();
                UserExists = true;
                }
            catch (IOException e)
                {
                  e.printStackTrace();
                }

            Intent intent = new Intent(this, Indicators.class);
            startActivity(intent);
        }

        else {
            if (BCrypt.checkpw(TypedPassword, HashedPassword)) {

                connectAndPull(Email, HashedPassword, AppConfig.TABLE_USERS);
                Intent intent = new Intent(this, Indicators.class);
                startActivity(intent);
            }
        }

    }

    public List<String> connectAndPull(String Email, String Password, String Table)
    {
        List<String> AllDevs = Collections.emptyList();
        DBconnect db_users = new DBconnect();
        Boolean[] UserExits = db_users.checkUser(Email, Password, Table);
        if (UserExits[1])
        {
            AllDevs = db_users.readUser(Email, Table);
            Salt = db_users.getUserSalt(Email, Table);
        }
        else
        {
            Salt = BCrypt.gensalt(12);
            String TypedHashedPassword = BCrypt.hashpw(UserPassword.getText().toString(), Salt);
            db_users.insertUser(Email,TypedHashedPassword, Salt,"null", "null", Table);
        }
        return AllDevs;
    }
}
