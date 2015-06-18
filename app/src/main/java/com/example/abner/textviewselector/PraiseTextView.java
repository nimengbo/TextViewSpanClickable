package com.example.abner.textviewselector;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abner on 15/6/17.
 * QQ 230877476
 * Email nimengbo@gmail.com
 * 点赞人数显示控件(名字不可点击)
 */
public class PraiseTextView extends TextView {

    private Context mContext;
    private StyleSpan boldSpan;

    public PraiseTextView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public PraiseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public PraiseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView(){
        setHighlightColor(getResources().getColor(R.color.transparent));
        boldSpan = new StyleSpan(Typeface.BOLD);
    }
    /**
     * 设置点赞的名字
     *
     * @param names
     * @return
     */
    public void setPraiseName(List<String> names) {
        setText("");
        String nameStr;
        StringBuilder sBuilder = new StringBuilder();
        for (String name : names) {
            sBuilder.append(name);
            sBuilder.append("、");
        }
        String lengthStr = "等" + names.size() + "人";
        nameStr = sBuilder.substring(0, sBuilder.length() - 1);
        nameStr += lengthStr;
        SpannableString mSpannableString = new SpannableString(nameStr);
        int start = nameStr.indexOf(lengthStr);
        mSpannableString.setSpan(boldSpan
                , 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //加粗
        append(mSpannableString);
    }
}
