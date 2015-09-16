package com.example.abner.textviewselector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abner on 15/6/17.
 * QQ 230877476
 * Email nimengbo@gmail.com
 * 另一种，每项可点击
 */
public class TopicTextView extends TextView {
    private TopicTextView instance;
    private Context mContext;
    private StyleSpan boldSpan;
    private ForegroundColorSpan colorSpan;

    private TextTopicClickListener textTopicClickListener;

    public void setTextTopicClickListener(TextTopicClickListener textTopicClickListener) {
        this.textTopicClickListener = textTopicClickListener;
    }

    public TopicTextView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public TopicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public TopicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        instance = this;
        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(getResources().getColor(R.color.transparent));
        boldSpan = new StyleSpan(Typeface.BOLD);
        colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.grey_bbbbbb));
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
                if (listener != null) {
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
    private int originalStart = -1;
    private int originalEnd = -1;

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
            if (distanceX > 1.5f || distanceY > 1.5f) {
                isMove = true;
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
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (isMove) {
                    if (mStart >= 0 && mEnd >= mStart) {
                        buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), mStart, mEnd,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mStart = -1;
                        mEnd = -1;
                    }
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
                    }
                }
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (isMove) {
                    setBackgroundColor(Color.TRANSPARENT);
                }
            }
            Selection.removeSelection(buffer);
            return false;
        }
    }


    /**
     * 设置点赞的名字
     *
     * @param topics
     * @return
     */
    public void setTopics(List<String> topics) {
        setText("");
        int length = topics.size();
        for (int i = 0; i < length; i++) {
            String topic;
            if (i == length - 1) {
                topic = "#" + topics.get(i);
            } else {
                topic = "#" + topics.get(i) + "、";
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(topic);
            TopicSpan topicSpan = new TopicSpan(topic, getResources(), textTopicClickListener);
            spannableStringBuilder.setSpan(topicSpan, 0, topic.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            append(spannableStringBuilder);
        }
        String moreStr = "          更多>";
        SpannableString mSpannableString = new SpannableString(moreStr);
        mSpannableString.setSpan(boldSpan
                , 0, moreStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //加粗
        mSpannableString.setSpan(colorSpan
                , 0, moreStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        append(mSpannableString);

    }
}
