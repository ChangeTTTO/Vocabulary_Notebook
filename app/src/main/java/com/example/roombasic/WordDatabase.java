package com.example.roombasic;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
//每一次数据库发生变化版本都要跟着变
//如果将 exportSchema 设置为 true，则 Room 会在构建过程中生成包含数据库架构信息的 JSON 文件。
@Database(entities = {Word.class},version = 1,exportSchema = false)
public abstract class  WordDatabase extends RoomDatabase {
    /**
     * 进行懒汉式线程安全单例化，因为数据库的实例化是比较耗费资源的
     */
    private static WordDatabase INSTANCE;
    public static synchronized WordDatabase getDatabase(Context context){
        if (INSTANCE==null){
            /**
             * 这里getApplicationContext()是为了配合View-Model继承Andriod-View-Model中的哪个Application
             */
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),WordDatabase.class,"WordDataBase")
                    .build();
        }
        return INSTANCE;
    }
    public abstract WordDao getWordDao();
}
