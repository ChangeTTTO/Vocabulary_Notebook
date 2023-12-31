package com.example.roombasic;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Word> allWords = new ArrayList<>();
    private MyViewModel myViewModel;
    private boolean useCardView;

    MyAdapter(boolean useCardView,MyViewModel myViewModel){
        this.useCardView=useCardView;
        this.myViewModel=myViewModel;
    }
    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    /**
     *当创建时调用
     * 需要返回一个ViewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //与Cell进行一个绑定,创建一个View
        View itemView =layoutInflater.inflate(R.layout.cell_normal_2,parent,false);
       final MyViewHolder holder =new MyViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.youdao.com/m/result?word="+holder.english.getText()+"&lang=en");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.switchChinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Word word=(Word)holder.itemView.getTag(R.id.word_for_view_holder);
                if (b){
                    //隐藏中文翻译
                    holder.chinese.setVisibility(View.GONE);
                    word.setChineseInvisible(true);
                    myViewModel.update(word);
                }else {
                    holder.chinese.setVisibility(View.VISIBLE);
                    word.setChineseInvisible(false);
                    myViewModel.update(word);
                }
            }
        });
        //返回一个ViewHolder
        return  holder;

    }

    /**
     *当自订的ViewHolder和RecyclerView进行绑定时调用
     * 屏幕上每展示出一个ViewHolder就会调用该方法进行一个数据的查找和绑定
     * 在 RecyclerView 在屏幕上滚动时，新的列表项需要显示在屏幕上时触发的。
     * 这个方法将在数据集中的每个列表项滚动到屏幕内时被调用，用于将数据绑定到相应的 ViewHolder 上，以便更新列表项的视图。
     * myAdapter.notifyDataSetChanged();数据发生改变，通知刷新视图,让onBindViewHolder 方法被调用
     * position就是
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //list的get方法，通过下标获取数据
          final  Word word = allWords.get(position);//position作为下标获取word对象，每次都会增加1
        holder.itemView.setTag(R.id.word_for_view_holder,word);//可以放入任意对象，其他地方就可以通过getTag获取到这个对象
             //将数据库中的数据绑定到组件上
            holder.number.setText(String.valueOf(position + 1));  //当前位置加1，因为是从0开始的
            holder.english.setText(word.getWord());
            holder.chinese.setText(word.getChineseMeaning());
        //每次回收都会相当于点击了onCheckedChanged,所以要添加这段代码，会导致本来已经打开开关了，滑出去再划回来竟然关闭了
       // holder.switchChinese.setOnCheckedChangeListener(null);
        if (word.isChineseInvisible()) {
            //View.GONE不显示也不会占位置。View.invisible不显示但是会占位置。
            holder.chinese.setVisibility(View.GONE);
            holder.switchChinese.setChecked(true);//默认就是打开状态
        }else {
            holder.chinese.setVisibility(View.VISIBLE);
            holder.switchChinese.setChecked(false);
        }
    }

    /**
     * 返回列表数据的总的个数
     * @return
     */
    @Override
    public int getItemCount() {
        return allWords.size();
    }
    /**
     * 展示的一行数据一个视图就是一个ViewHolder,滚动出屏幕就会被回收，在下面显示出来一个新的ViewHolder就会被重复使用
     * 每次重复放到一个新的位置上就会呼叫一次onBindViewHolder获取数据并显示到ViewHolder中
     */

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView number,english,chinese;
        Switch switchChinese;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.number);
            english=itemView.findViewById(R.id.english);
            chinese=itemView.findViewById(R.id.chinese);
            switchChinese=itemView.findViewById(R.id.switchChinese);
        }
    }
}
