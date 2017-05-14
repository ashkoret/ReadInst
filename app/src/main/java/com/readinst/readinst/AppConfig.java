package com.readinst.readinst;


/**
 * Created by dan on 02-04-17.
 */

public class AppConfig {
    public static final String connectionString = "jdbc:mysql://sql11.freemysqlhosting.net/sql11163485";
    public static final String db_user = "sql11163485";
    public static final String db_pass = "l68Xw4x5PP";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String TABLE_DEVICES = "ReadInstruments";
    public static final String TABLE_USERS = "ReadInstUsers";
    public static final String TABLE_USER_DEVS = "ReadInstUserDevs";
    public static final String USR_FILE = "current.usr";
    public static final String DEV_FILE = "current.dev";
    public static String UserEmail = "";
    public static Boolean UserExists[] = {false, false};

}
