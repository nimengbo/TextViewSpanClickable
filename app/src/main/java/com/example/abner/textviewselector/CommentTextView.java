package com.example.abner.textviewselector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Abner on 15/6/17.
 * QQ 230877476
 * Email nimengbo@gmail.com
 */
public class CommentTextView extends TextView {
    private TextView instance;
    private Context mContext;
    private StyleSpan boldSpan;
    //回复者
    private ClickableSpan reviewSpan;
    //被回复者
    private ClickableSpan replySpan;

    public CommentTextView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CommentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CommentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        instance = this;
        setHighlightColor(getResources().getColor(R.color.transparent));
        setMovementMethod(LinkMovementMethod.getInstance());
        boldSpan = new StyleSpan(Typeface.BOLD);
        reviewSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                CommentModel model1 = (CommentModel) widget.getTag();
                Toast.makeText(mContext, model1.getReviewUName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setTypeface(Typeface.DEFAULT_BOLD);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.blue_666e8a));
            }
        };
        replySpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                CommentModel model1 = (CommentModel) widget.getTag();
                Toast.makeText(mContext, model1.getReplyUName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setTypeface(Typeface.DEFAULT_BOLD);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.blue_666e8a));
            }
        };
    }

    private TextBlankClickListener listener;

    public void setListener(TextBlankClickListener listener) {
        this.listener = listener;
    }

    private int mStart = -1;

    private int mEnd = -1;

    private android.os.Handler handler = new android.os.Handler();
    //计数
    private int leftTime;
    private Runnable countDownRunnable = new Runnable() {
        public void run() {
            leftTime--;
            if (leftTime == -1) {
                // 触发长按事件
                if(listener != null){
                    listener.onLongClick(instance);
                }
            } else {
                //100毫秒执行一次 超过500毫秒就说明触发长按
                handler.postDelayed(this, 100);
            }
        }
    };
    private boolean isMove = false;
    private float lastX;
    private float lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);

        int action = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            lastX = event.getX();
            lastY = event.getY();
            isMove = false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            float distanceX = Math.abs(lastX - event.getX());
            float distanceY = Math.abs(lastY - event.getY());
            if (distanceX > 0.5f || distanceY > 0.5f) {
                isMove = true;
                return result;
            }
        }

        x -= getTotalPaddingLeft();
        y -= getTotalPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);
        CharSequence text = getText();
        if (TextUtils.isEmpty(text) || !(text instanceof Spannable)) {
            return result;
        }
        Spannable buffer = (Spannable) text;
        ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
        if (link.length != 0) {
            if (action == MotionEvent.ACTION_DOWN) {
                mStart = buffer.getSpanStart(link[0]);
                mEnd = buffer.getSpanEnd(link[0]);
                if (mStart >= 0 && mEnd >= mStart) {
                    buffer.setSpan(new BackgroundColorSpan(Color.GRAY), mStart, mEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                if (mStart >= 0 && mEnd >= mStart) {
                    buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), mStart, mEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mStart = -1;
                    mEnd = -1;
                }
            }
            return true;
        } else {

            if (mStart >= 0 && mEnd >= mStart) {
                buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), mStart, mEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mStart = -1;
                mEnd = -1;
            }
            if (action == MotionEvent.ACTION_DOWN) {
                setBackgroundColor(Color.GRAY);
                //开始计数
                leftTime = 5;
                handler.post(countDownRunnable);
                return true;
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                setBackgroundColor(Color.TRANSPARENT);
                //如果没有调用长按 调用点击整个的监听
                if (leftTime > -1) {
                    leftTime = 10;
                    handler.removeCallbacks(countDownRunnable);//移除统计
                    if (listener != null && !isMove) {
                        listener.onBlankClick(this);
                        Log.d("IndexFragment", "onBlankClick");
                    }
                }
            }
            Selection.removeSelection(buffer);
            return false;
        }
    }

    /**
     * 设置回复的可点击文本
     *
     * @param model
     * @return
     */
    public void setReply(CommentModel model) {
        setText("");
        setTag(model);
        String reviewName = model.getReviewUName();
        SpannableStringBuilder stylesBuilder;
        String str;
        if (model.getReplyUid() != 0) {
            String replyName = model.getReplyUName();
            str = reviewName + "回复" + replyName + "：" + model.getReviewContent();
            int reviewStart = str.indexOf(reviewName);
            int replyStart = str.indexOf(replyName);
            stylesBuilder = new SpannableStringBuilder(str);
            stylesBuilder.setSpan(reviewSpan, reviewStart, reviewStart + reviewName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            stylesBuilder.setSpan(replySpan, replyStart, replyStart + replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            str = reviewName + "：" + model.getReviewContent();
            int reviewStart = str.indexOf(reviewName);
            stylesBuilder = new SpannableStringBuilder(str);
            stylesBuilder.setSpan(reviewSpan, reviewStart, reviewStart + reviewName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        append(stylesBuilder);
    }

}
