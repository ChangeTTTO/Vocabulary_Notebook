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
    MyAdapter myAdapter1;
    MyAdapter myAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView=findViewById(R.id.RecyclerView);
        myAdapter1 = new MyAdapter(false,myViewModel);
        myAdapter2 = new MyAdapter(true,myViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//一维列表所以用这个
        recyclerView.setAdapter(myAdapter1);
        //创建一个数据库，第三个参数是存储的时候的数据库名称
        wordDatabase = WordDatabase.getDatabase(this);

        //LiveData

        //数据发生改变就改变textView的值
        myViewModel.getAllWordLive().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                //获取myAdapter1中的数据长度
                //todo 看看这个函数是什么意思
                int temp = myAdapter1.getItemCount();
                myAdapter1.setAllWords(words);
                myAdapter2.setAllWords(words);
                //如果长度不相同，那就不刷新列表，因为在Adapter中已经有过刷新了，改变这个设置，可以让动作变得更平滑
                if(temp!=words.size()){
                    myAdapter1.notifyDataSetChanged();//数据发生改变，通知刷新视图,让onBindViewHolder 方法被调用
                    myAdapter2.notifyDataSetChanged();
                }
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