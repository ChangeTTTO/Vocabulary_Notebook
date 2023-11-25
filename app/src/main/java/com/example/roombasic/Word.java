package com.example.roombasic;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    //不写@ColumnInfo的话就自动以属性名作为字段名
    @ColumnInfo(name="english_word")
    private String word;
    @ColumnInfo(name="chinese_Meaning")
    private String chineseMeaning;

    public Word( String word, String chineseMeaning) {
        this.id = id;
        this.word = word;
        this.chineseMeaning = chineseMeaning;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public void setChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
    }
}
