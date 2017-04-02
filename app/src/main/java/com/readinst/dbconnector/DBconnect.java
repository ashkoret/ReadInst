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
            //String just = "just";
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
    public boolean insertUser(String UserEmail, String UserPassword, String Table)
    {
        boolean result = false;
        try
        {
            PreparedStatement st = conn.prepareStatement("INSERT INTO " + Table + " (Email, Password) VALUES (?,?)");
            st.setString(1,UserEmail);
            st.setString(2,UserPassword);
            result = st.execute();
            st.close();
        }
        catch(SQLException s)
        {
            Log.e(TAG, s.getMessage());
        }
        return result;
    }

    public HashMap<String, String> getUser(String UserEmail, String UserPassword)
    {
        HashMap<String, String> UserDevices = new HashMap<String, String>();
        String query = "SELECT * FROM " + AppConfig.TABLE_USERS + "WHERE Email = '"+ UserEmail +"' AND Password='" + UserPassword +"')";
            try
            {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next())
                {
                    UserDevices.put("Email", rs.getString("Email"));
                    UserDevices.put("Dev0", rs.getString("Dev0"));
                    UserDevices.put("Dev1", rs.getString("Dev1"));
                    UserDevices.put("Dev2", rs.getString("Dev2"));
                }
            }
            catch(SQLException s)
            {
                Log.e(TAG, s.getMessage());
            }
            return UserDevices;
    }

}
