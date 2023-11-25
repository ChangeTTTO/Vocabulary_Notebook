package com.example.roombasic;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import lombok.Data;

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
}
