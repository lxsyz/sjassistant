package com.example.administrator.sjassistant.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syz on 2016/4/11.
 * 过滤密码输入框中的汉字及空格
 */
public class WatcherUtil implements TextWatcher{

    private EditText editText;
    private String type;

    /*
     * @param editText 需检测的输入框
     * @param type password:检测密码输入
     * @param type text: 过滤空格和换行
     */
    public WatcherUtil(EditText editText,String type) {
        this.editText = editText;
        this.type = type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        int editStart = editText.getSelectionStart();
        editText.removeTextChangedListener(this);
        String input = s.toString();
        int index = 0;
        Pattern chinese = Pattern.compile("[\u0391-\uFFE5]+");
        if (!TextUtils.isEmpty(editText.getText())) {
            for (int i = 0;i < input.length();i++) {
                CharSequence target = input.subSequence(i,i+1);
                if (" ".equals(input.subSequence(i,i+1)) || "\n".equals(input.subSequence(i, i+1))) {
                    editStart = editStart - 1;
                    s.delete(i, i + 1);

                }
                if ("password".equals(type)) {
                    Matcher matcher = chinese.matcher(target);
                    if (matcher.matches()) {
                        if (index == 0) {
                            index = 1;
                        } else {
                            index = index + 1;
                        }
                        s.delete(i - (index-1),i+1-(index - 1));
                    }
                }

            }
            editText.setText(s);
            if (s.length() == 0) {
                editText.setSelection(0);
            } else {
                editText.setSelection(s.length());
            }

        }
        editText.addTextChangedListener(this);
    }
}
