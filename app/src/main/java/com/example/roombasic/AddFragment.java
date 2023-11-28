package com.example.roombasic;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
private Button buttonSubmit;
private EditText EditTextEnglish,EditTextChinese;
private MyViewModel myViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //作用域是整个Activity
        myViewModel= new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
        //创建 Fragment 时调用
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add, container, false);
    }
        // 在 onCreateView 之后被调用
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        buttonSubmit=requireActivity().findViewById(R.id.buttonSubmit);
        EditTextEnglish=requireActivity().findViewById(R.id.editTextTextEnglish);
        EditTextChinese=requireActivity().findViewById(R.id.editTextTextChinese);
        EditTextEnglish.requestFocus();//获取焦点
        buttonSubmit.setEnabled(false);//按钮初始状态不可按
        InputMethodManager imm  = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(EditTextEnglish,0);//强制显示键盘，参数一，键盘和哪个参数挂钩

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //当文本框里的内容发生改变时
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String chinese = EditTextChinese.getText().toString().trim();
                String english = EditTextEnglish.getText().toString().trim();
                //如果有内容了就能够使用按钮
                buttonSubmit.setEnabled(!english.isEmpty()&& !chinese.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        EditTextChinese.addTextChangedListener(textWatcher);
        EditTextEnglish.addTextChangedListener(textWatcher);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chinese = EditTextChinese.getText().toString().trim();
                String english = EditTextEnglish.getText().toString().trim();
                //点击按钮之后将数据传递进数据库
                Word word = new Word(english,chinese);
                myViewModel.insertWords(word);
                NavController controller = Navigation.findNavController(view);
                controller.navigateUp();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

}