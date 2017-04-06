package com.readinst.dbconnector;

import com.readinst.readinst.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
    public boolean insertUser(String UserEmail, String UserPassword, String UserSalt, String Dev,String Table)
    {
        boolean result = false;
        String hashedPassword = BCrypt.hashpw(UserPassword,UserSalt);
        if (conn!=null) {
            try {
                PreparedStatement st = conn.prepareStatement("INSERT INTO " + Table + " (Email, Password, Salt, Dev0) VALUES (?,?,?,?)");
                st.setString(1, UserEmail);
                st.setString(2, hashedPassword);
                st.setString(3, UserSalt);
                st.setString(4, Dev);
                result = st.execute();
                st.close();
            } catch (SQLException s) {
                Log.e(TAG, s.getMessage());
            }
        }
        return result;
    }

    public String checkUser(String UserEmail, String UserPassword, String Table)
    {
       String Salt="";
        // TODO Read the table and get user salt, if no user return salt="0";
       return Salt;

    }


    public HashMap<String, String> getUser(String UserEmail)
    {
        HashMap<String, String> UserDevices = new HashMap<>();
        String query = "SELECT * FROM " + AppConfig.TABLE_USERS + "WHERE Email = '"+ UserEmail +"')";
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
