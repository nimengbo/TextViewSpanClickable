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
        PraiseTextView textView = (PraiseTextView) findViewById(R.id.hello);

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
        CommentTextView textView1 = (CommentTextView) findViewById(R.id.hello1);
        CommentTextView textView2 = (CommentTextView) findViewById(R.id.hello2);
        TopicTextView textView3 = (TopicTextView) findViewById(R.id.hello3);
        final TextView textView4 = (TextView) findViewById(R.id.hello4);
        textView1.setReply(commentList.get(0));
        textView2.setReply(commentList.get(1));
        textView2.setListener(new TextBlankClickListener() {
            @Override
            public void onBlankClick(View v) {
                Toast.makeText(MainActivity.this, "textview2点击了整个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view) {
                Toast.makeText(MainActivity.this, "textview2长按了整个", Toast.LENGTH_SHORT).show();
            }
        });
        textView3.setPraiseName(likeUsers);
        textView3.setListener(new TextBlankClickListener() {
            @Override
            public void onBlankClick(View v) {
                Toast.makeText(MainActivity.this, "textview3点击了整个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view) {

            }
        });

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView4.setVisibility(View.GONE);
            }
        },1000);
    }
}
