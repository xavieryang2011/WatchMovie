package cn.xavier.movie.db;

/**
 * Created by yangxh on 17/7/30.
 */

public class DbConstant {

    public static String DB_NAME="ZHColumn";
    public static int DB_VERSION=1;
    public static String DB_TABLE_READ="read";
    public static String DB_TABLE_READ_ID="column_id";
    public static String CREATE_TABLE_READ="create table "+DB_TABLE_READ+"("+DB_TABLE_READ_ID+" text)";
}
