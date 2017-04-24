package com.readinst.dbconnector;

import com.readinst.readinst.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import android.util.Log;
import java.util.List;
import java.util.Collections;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Created by dan on 02-04-17.
 */

public class DBconnect {
    private Connection conn;
    private String TAG = DBconnect.class.getSimpleName();

    public DBconnect()
    {
        conn = null;
        try
        {
            Class.forName(AppConfig.DRIVER);
            conn = DriverManager.getConnection(AppConfig.connectionString, AppConfig.db_user, AppConfig.db_pass);
        }
        catch(SQLException s)
        {
            Log.e(TAG, s.getMessage());
        }
        catch(ClassNotFoundException c)
        {
            Log.e(TAG, c.getMessage());
        }
    }
    public boolean insertUser(String UserEmail, String UserPassword, String UserSalt, String Dev, String DevName, String Table)
    {
        boolean result = false;
        if (conn!=null) {
            try {
                PreparedStatement st = conn.prepareStatement("INSERT INTO " + Table + " (Email, Password, Salt, Dev0, DevName) VALUES (?,?,?,?,?)");
                st.setString(1, UserEmail);
                st.setString(2, UserPassword);
                st.setString(3, UserSalt);
                st.setString(4, Dev);
                st.setString(5, DevName);
                result = st.execute();
                st.close();
            } catch (SQLException s) {
                Log.e(TAG, s.getMessage());
            }
        }
        return result;
    }

    public String getUserSalt(String UserEmail, String Table)
    {
        String Salt = "NULL";
        if (conn!=null) {
            try {
                String query = "SELECT * FROM " + Table + " WHERE Email = '" + UserEmail + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    Salt = rs.getString("Salt");
                }
            } catch (SQLException s) {
                Log.e(TAG, s.getMessage());
            }
        }
        return Salt;
    }



    public Boolean[] checkUser(String UserEmail, String UserPassword, String Table)
    {
        Boolean UserExists[] = {false, false};
        String StoredPassword;
        String StoredUserEmail;
        if (conn!=null)
        {
            try
            {
                String query = "SELECT * FROM " + Table + " WHERE Email = '"+ UserEmail +"'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    StoredUserEmail = rs.getString("Email");
                    StoredPassword = rs.getString("Password");
                    if (StoredPassword.equals(UserPassword)) {
                        UserExists[1] = true;
                    }
                    if (StoredUserEmail.equals(UserEmail)) {
                        UserExists[0] = true;
                    }
                }
            }
            catch (SQLException s)
            {
                Log.e(TAG, s.getMessage());
            }
        }
        return UserExists;
    }

    public List<String> readUser(String UserEmail, String Table)
    {
       List<String> Devs = Collections.emptyList();
        String Dev0;
        if (conn!=null) {
            try
            {
                String query = "SELECT * FROM " + Table + " WHERE Email = '" + UserEmail + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next())
                {
                    Dev0 = rs.getString("Dev0");
                    Devs.add(Dev0);
                }

                // TODO Read the table and get Dev0
            } catch (SQLException s) {
                Log.e(TAG, s.getMessage());
            }
        }
       return Devs;

    }


    public HashMap<String, String> getUser(String UserEmail, String Table)
    {
        HashMap<String, String> UserDevices = new HashMap<>();
        String query = "SELECT * FROM " + Table + " WHERE Email = '"+ UserEmail +"'";
            try
            {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next())
                {
                    UserDevices.put("Dev0", rs.getString("Dev0"));
                }
            }
            catch(SQLException s)
            {
                Log.e(TAG, s.getMessage());
            }
            return UserDevices;
    }

}
