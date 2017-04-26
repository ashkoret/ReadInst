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

import java.util.LinkedList;
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
    public boolean insertUser(String UserEmail, String UserPassword, String UserSalt, String Table)
    {
        boolean result = false;
        if (conn!=null) {
            try {
                PreparedStatement st = conn.prepareStatement("INSERT INTO " + Table + " (Email, Password, Salt) VALUES (?,?,?)");
                st.setString(1, UserEmail);
                st.setString(2, UserPassword);
                st.setString(3, UserSalt);
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

    public HashMap<String, String> readUser(String UserEmail, String Table)
    {
        HashMap<String, String> UserDevices = new HashMap<>();

        if (conn!=null) {
            try {
                String query = "SELECT * FROM " + Table + " WHERE Email = '" + UserEmail + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                int i=0;
                while (rs.next()) {
                    UserDevices.put("Dev"+Integer.toString(i), rs.getString("Dev0"));
                    UserDevices.put("Dev"+Integer.toString(i)+"Name", rs.getString("DevName"));
                    i++;
                }
                rs.close();
            } catch (SQLException s) {
                Log.e(TAG, s.getMessage());
            }
        }
       return UserDevices;
    }

    public HashMap<String, String> readUserInst(String PCID, String Table)
    {
        HashMap<String, String> DevIndicators = new HashMap<>();

        if (conn!=null) {
            try {
                String query = "SELECT * FROM " + Table + " WHERE DeviceID = '" + PCID + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                int i=0;
                while (rs.next()) {
                    DevIndicators.put("Instrument"+Integer.toString(i), rs.getString("Instrument"));
                    DevIndicators.put("Instrument"+Integer.toString(i)+"Value0", rs.getString("Value0"));
                    DevIndicators.put("Instrument"+Integer.toString(i)+"Value1", rs.getString("Value1"));
                    DevIndicators.put("Instrument"+Integer.toString(i)+"Value2", rs.getString("Value2"));
                    DevIndicators.put("Instrument"+Integer.toString(i)+"Value3", rs.getString("Value3"));
                    DevIndicators.put("Time"+Integer.toString(i), rs.getString("Time"));
                    i++;
                }
                rs.close();
            } catch (SQLException s) {
                Log.e(TAG, s.getMessage());
            }
        }

        return DevIndicators;
    }
}
