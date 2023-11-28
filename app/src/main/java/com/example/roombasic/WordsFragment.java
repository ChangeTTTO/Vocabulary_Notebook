package com.example.roombasic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordsFragment extends Fragment {
    private MyViewModel myViewModel;
    private MyAdapter myAdapter1,myAdapter2;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private LiveData<List<Word>> filteredWords;
    private static final String VIEW_TYPE_SHP = "view_type_shp";
    private static final String IS_USING_CARD_VIEW ="is_using_card_view";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WordsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFragment newInstance(String param1, String param2) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        myViewModel=new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        recyclerView = requireView().findViewById(R.id.RecylerView);  //todo getView换成requireActivity()
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        myAdapter1 =new MyAdapter(true,myViewModel);
        myAdapter2=new MyAdapter(false,myViewModel);
       // recyclerView.setAdapter(myAdapter1);
        SharedPreferences shp = requireActivity().getSharedPreferences(VIEW_TYPE_SHP,Context.MODE_PRIVATE);
        //数据存进去，保存视图状态
        boolean viewType = shp.getBoolean(IS_USING_CARD_VIEW,true);
        if (viewType){
            recyclerView.setAdapter(myAdapter1);
        }else {
            recyclerView.setAdapter(myAdapter2);
        }
        filteredWords=myViewModel.getAllWordLive();
        filteredWords.observe(requireActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                //如果没有任何的数据更改，在视图层面进行刷新就可以了
                int temp=myAdapter1.getItemCount();
                myAdapter1.setAllWords(words);
                myAdapter2.setAllWords(words);
                if (temp!=words.size()){
                    myAdapter1.notifyDataSetChanged();
                    myAdapter2.notifyDataSetChanged();//数据发生改变，通知刷新视图,让onBindViewHolder 方法被调用
                }
            }
        });
        floatingActionButton=requireActivity().findViewById(R.id.floatingActionButton);//todo gitView-->require
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击之后进行导航
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(800);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override//当提交内容时
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override//当搜索框文本改变时
            public boolean onQueryTextChange(String s) {
                String pattern = s.trim();
                filteredWords.removeObservers(requireActivity());//移除观察，因为前面已经有一个观察了
                filteredWords=myViewModel.findWordsWIthPattern(pattern);
                filteredWords.observe(requireActivity(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        //如果没有任何的数据更改，在视图层面进行刷新就可以了
                        int temp=myAdapter1.getItemCount();
                        myAdapter1.setAllWords(words);
                        myAdapter2.setAllWords(words);
                        if (temp!=words.size()){
                            myAdapter1.notifyDataSetChanged();
                            myAdapter2.notifyDataSetChanged();//数据发生改变，通知刷新视图,让onBindViewHolder 方法被调用
                        }
                    }
                });
                return false;//如果事件完成不用往下继续了，就返回true，s就是监听到的内容
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.cleanData) {
            // 获取弹出对话框对象
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("清空数据");

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                // 确定执行的语句
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    myViewModel.deleteAll();
                }
            });

            // 取消执行的语句
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 取消执行的语句
                }
            });

            builder.create();
            builder.show();
        } else if(itemId==R.id.SwitchView){
            SharedPreferences shp = requireActivity().getSharedPreferences(VIEW_TYPE_SHP,Context.MODE_PRIVATE);
            //从SharedPreferences中读取一个名为IS_USING_CARD_VIEW的布尔值
            boolean viewType = shp.getBoolean(IS_USING_CARD_VIEW,true);
            SharedPreferences.Editor editor= shp.edit();
            if(viewType){
                recyclerView.setAdapter(myAdapter1);
                editor.putBoolean(IS_USING_CARD_VIEW,true);
            }else {
                recyclerView.setAdapter(myAdapter2);
                editor.putBoolean(IS_USING_CARD_VIEW,false);
            }
            editor.apply();

        }


        return super.onOptionsItemSelected(item);
    }
}