package com.example.abner.textviewselector;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends Activity {
    private List<String> likeUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PraiseTextView textView = (PraiseTextView) findViewById(R.id.tv_praise_test);

        likeUsers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            likeUsers.add("行不啊" + i);
        }
        textView.setPraiseName(likeUsers);

        List<CommentModel> commentList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            CommentModel model = new CommentModel();
            model.setReplyUid(i - 1);
            model.setReplyUName("被回复者 " + i);
            model.setReviewUName("回复者 " + i);
            model.setReviewContent("回复回复回复回复回复回复回复回复回复回复回复回复回复回复回复回复回复回复回复" + i);
            commentList.add(model);
        }
        CommentTextView tv_comment_test = (CommentTextView) findViewById(R.id.tv_comment_test);
        CommentTextView tv_comment_reply = (CommentTextView) findViewById(R.id.tv_comment_reply);
        TopicTextView tv_topic = (TopicTextView) findViewById(R.id.tv_topic);
        tv_comment_test.setReply(commentList.get(0));
        tv_comment_reply.setReply(commentList.get(1));
        tv_comment_test.setListener(new TextBlankClickListener() {
            @Override
            public void onBlankClick(View v) {
                Toast.makeText(MainActivity.this, "tv_comment_test点击了整个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view) {
                Toast.makeText(MainActivity.this, "tv_comment_test长按了整个", Toast.LENGTH_SHORT).show();
            }
        });
        tv_comment_reply.setListener(new TextBlankClickListener() {
            @Override
            public void onBlankClick(View v) {
                Toast.makeText(MainActivity.this, "tv_comment_reply点击了整个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view) {
                Toast.makeText(MainActivity.this, "tv_comment_reply长按了整个", Toast.LENGTH_SHORT).show();
            }
        });
        tv_topic.setListener(new TextBlankClickListener() {
            @Override
            public void onBlankClick(View v) {
                Toast.makeText(MainActivity.this, "tv_topic点击了整个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view) {

            }
        });
        tv_topic.setTextTopicClickListener(new TextTopicClickListener() {
            @Override
            public void onTopicClick(View view, String topic) {
                Toast.makeText(MainActivity.this, topic, Toast.LENGTH_SHORT).show();
            }
        });
        //先设置监听再设置文字
        tv_topic.setTopics(likeUsers);

    }
}
