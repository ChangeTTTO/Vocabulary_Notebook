package com.example.roombasic;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

//每一次数据库发生变化版本都要跟着变
//如果将 exportSchema 设置为 true，则 Room 会在构建过程中生成包含数据库架构信息的 JSON 文件。
@Database(entities = {Word.class},version = 4,exportSchema = false)
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
                    .addMigrations(MIGRATION_3_4)//保留原有数据进行版本更新
                    .build();
        }
        return INSTANCE;
    }
    public abstract WordDao getWordDao();
     static final Migration MIGRATION_3_4 =new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //更新word表添加bar_data字段不能为空默认值为1
            database.execSQL("ALTER TABLE word ADD COLUMN chinese_invisible INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_2_3 =new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
           database.execSQL("CREATE TABLE word_temp (id INTEGER PRIMARY KEY NOT NULL,english_word TEXT," +
                   "chinese_Meaning TEXT)");
           database.execSQL("INSERT INTO word_temp(id,english_word,chinese_Meaning)" +
                   "SELECT id,english_word,chinese_Meaning FROM word");
           database.execSQL("Drop TABLE word");
           database.execSQL("ALTER TABLE word_temp RENAME to word");
        }
    };

}
