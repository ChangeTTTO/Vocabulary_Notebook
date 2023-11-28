package com.example.roombasic;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class WordRepository {
    private LiveData<List<Word>> allWordsLive;
    WordDao wordDao;
    public WordRepository(Context context) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao=wordDatabase.getWordDao();
        allWordsLive=wordDao.getAllWordsLive();
    }
    public LiveData<List<Word>>findWordsWithPattern(String pattern){return wordDao.findWordsWithPattern("%"+pattern+"%");}
    public LiveData<List<Word>> getAllWordsLive() {
        return allWordsLive;
    }
    /**
     * Word:输入到异步任务的参数类型
     * Void: 报告进度
     * Void:报告结果
     */
    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }
    void insertWords(Word...words){
        new InsertAsyncTask(wordDao).execute(words);
    }
    void update(Word...words){
        new updateAsyncTask(wordDao).execute(words);
    }
    void deleteWords(Word...words){
        new deleteAsyncTask(wordDao).execute(words);
    }
    void deleteAll(){
        new deleteAllAsyncTask(wordDao).execute();
    }
    static class updateAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public updateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.update(words);
            return null;
        }
    }

    static class deleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public deleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.DeteleWords(words);
            return null;
        }

    }
    static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        public deleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }

}
