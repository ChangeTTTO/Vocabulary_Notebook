package com.example.roombasic;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Word> allWords = new ArrayList<>();

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    /**
     *当创建ViewHolder时调用
     *
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //与Cell进行一个绑定
        View itemView =layoutInflater.inflate(R.layout.card,parent,false);
        //创建一个ViewHolder
        return new MyViewHolder(itemView);

    }

    /**
     *当自订的ViewHolder和RecyclerView进行绑定时调用
     * 在 RecyclerView 在屏幕上滚动时，新的列表项需要显示在屏幕上时触发的。
     * 这个方法将在数据集中的每个列表项滚动到屏幕内时被调用，用于将数据绑定到相应的 ViewHolder 上，以便更新列表项的视图。
     * myAdapter.notifyDataSetChanged();数据发生改变，通知刷新视图,让onBindViewHolder 方法被调用
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Word word = allWords.get(position);//position作为下标获取word对象
             //将数据库中的数据绑定到组件上
            holder.number.setText(String.valueOf(position + 1));  //当前位置加1，因为是从0开始的
            holder.english.setText(word.getWord());
            holder.chinese.setText(word.getChineseMeaning());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("https://www.youdao.com/m/result?word="+holder.english.getText()+"&lang=en");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    holder.itemView.getContext().startActivity(intent);

                }
            });
    }

    /**
     * 返回列表数据的总的个数
     * @return
     */
    @Override
    public int getItemCount() {
        return allWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView number,english,chinese;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.number);
            english=itemView.findViewById(R.id.english);
            chinese=itemView.findViewById(R.id.chinese);
        }
    }
}
