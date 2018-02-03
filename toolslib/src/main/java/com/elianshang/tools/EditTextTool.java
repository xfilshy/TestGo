package com.elianshang.tools;

import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuhanzhi on 15/11/27.
 */
public class EditTextTool {

    public static void setEmojiFilter(EditText et) {
        InputFilter emojiFilter;
        emojiFilter = new InputFilter() {
            Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) {
                    return "";
                }
                return null;
            }
        };
        et.setFilters(new InputFilter[]{emojiFilter});
    }

    /**
     * 监听EditText的内容变化，逻辑是：
     * 1.当所有EditText均有内容输入时，提交表单按钮才可点击；
     * 2.当某EditText有内容时，显示其对应的文本清除按钮，无内容则隐藏清除按钮。
     *
     * @param enableView 提交表单按钮
     * @param editTexts  EditText数组
     * @param clearViews 与EditText对应的清除按钮的数组，如果某EditText不需要有对应的清除按钮，那么与该EditText的数组下标对应的清除按钮即为null
     */
    public static void setOnInputChanged(final View enableView, final EditText[] editTexts, final View[] clearViews) {
        if (enableView == null || editTexts == null) {
            return;
        }
        final int length = editTexts.length;
        for (int i = 0; i < length; i++) {
            final EditText editText = editTexts[i];
            final View clearView;
            if (clearViews != null) {
                clearView = clearViews[i];
            } else {
                clearView = null;
            }

            if (editText != null) {
                if (null != clearView) {
                    clearView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editText != null) {
                                editText.setText(null);
                            }
                        }
                    });

                }
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            if (clearView != null && !TextUtils.isEmpty(editText.getText().toString())) {
                                clearView.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (clearView != null) {
                                clearView.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            if (clearView != null && clearView.getVisibility() == View.VISIBLE) {
                                clearView.setVisibility(View.INVISIBLE);
                            }
                            if (enableView.isEnabled()) {
                                enableView.setEnabled(false);
                                enableView.setClickable(false);
                            }

                        } else {
                            if (clearView != null && clearView.getVisibility() != View.VISIBLE) {
                                clearView.setVisibility(View.VISIBLE);
                            }
                            boolean allInput = true;
                            for (int j = 0; j < length; j++) {
                                final EditText tempEdit = editTexts[j];
                                if (tempEdit == null || tempEdit == editText) {
                                    continue;

                                } else {
                                    if (TextUtils.isEmpty(tempEdit.getText().toString())) {
                                        allInput = false;
                                        break;
                                    }
                                }
                            }
                            if (allInput && !enableView.isEnabled()) {
                                enableView.setEnabled(true);
                                enableView.setClickable(true);
                            }
                        }
                    }
                });

            }
        }
    }

    /**
     * 计算textview中文本的宽度。
     *
     * @param textView
     * @return
     */
    public static float measureTextWidth(TextView textView) {
        return measureTextWidth(textView, textView.getText().toString());
    }

    /**
     * 计算textview中文本的宽度。
     *
     * @param textView 被放入的textview
     * @param text     文本
     * @return
     */
    public static float measureTextWidth(TextView textView, String text) {
        float width = 0.0f;
        if (null != textView && null != textView.getPaint() && !TextUtils.isEmpty(text)) {
            Paint paint = textView.getPaint();
            width = paint.measureText(text);
        }
        return width;
    }

    public static void setSuitableTextView(TextView textView, String text, int width) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        float textSize = textView.getPaint().getTextSize();
        while (measureTextWidth(textView, text) > width) {
            textSize--;
            if (textSize == 0) {
                return;
            }
            textView.getPaint().setTextSize(textSize);
        }
    }
}
