package com.readinst.dbconnector;

import com.readinst.readinst.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

}
