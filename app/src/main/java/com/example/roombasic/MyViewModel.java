package com.example.roombasic;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MyViewModel extends AndroidViewModel {
    private WordDao wordDao;
    private WordRepository wordRepository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getAllWordLive() {
        return wordRepository.getAllWordsLive();
    }

    void insertWords(Word... words) {
        wordRepository.insertWords(words);
    }

    void update(Word... words) {
        wordRepository.update(words);
    }

    void deleteWords(Word... words) {
        wordRepository.deleteWords(words);
    }

    void deleteAll() {
        wordRepository.deleteAll();
    }

}
