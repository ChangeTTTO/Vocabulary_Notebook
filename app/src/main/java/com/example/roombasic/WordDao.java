package com.example.roombasic;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WordDao {
    @Insert
    void  insertWords(Word...words); //...代表多个参数
    @Update
    //根据ID修改一行数据
    void update(Word...words);
    @Delete
    void DeteleWords(Word...words);
    //清空
    @Query("DELETE FROM WORD")
    void deleteAllWords();
    @Query("SELECT * FROM WORD ORDER BY ID DESC")
  //  List<Word> getAllWords();
    /**
     * LiveData这个对象系统自动放到副线程去处理了，不用再建一个线程专门使用。
     */
    LiveData<List<Word>> getAllWordsLive();
    //利用传递过来的参数pattern进行模糊匹配,传递进来的参数前后都需要添加%
    @Query("SELECT * FROM WORD WHERE english_word LIKE:pattern ORDER BY ID DESC")
    LiveData<List<Word>> findWordsWithPattern(String pattern);
}
