package com.example.roombasic;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LiveData<List<Word>> AllWordsLive;
    WordDatabase wordDatabase;
    WordDao wordDao;
    Button insert, delete, deleteAll, update;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView=findViewById(R.id.RecyclerView);
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//一维列表所以用这个
        recyclerView.setAdapter(myAdapter);
        //创建一个数据库，第三个参数是存储的时候的数据库名称
        wordDatabase = WordDatabase.getDatabase(this);

        //LiveData

        //数据发生改变就改变textView的值
        myViewModel.getAllWordLive().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                myAdapter.setAllWords(words);
                myAdapter.notifyDataSetChanged();//数据发生改变，通知刷新视图,让onBindViewHolder 方法被调用
            }
        });

        deleteAll = findViewById(R.id.deleteAll);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        insert = findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("hello", "你好");
                Word word2 = new Word("world", "世界");
                myViewModel.insertWords(word, word2);
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myViewModel.deleteAll();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("String", "字符串");
                word.setId(188);
                myViewModel.update();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("String", "字符串");
                word.setId(188);
                myViewModel.deleteWords();
            }
        });
    }

}