package com.example.abner.textviewselector;

import java.io.Serializable;

/**
 * Created by Abner on 15/1/15.
 * QQ 230877476
 * Email nimengbo@gmail.com
 * 评论列表
 */
public class CommentModel implements Serializable {

    private static final long serialVersionUID = -3063305610315810693L;
    //评论用户ID
    private long reviewUid;
    //评论用户名字
    private String reviewUName;
    //被回复用户ID 如果是0  就不是回复
    private long replyUid;
    //被回复用户名字
    private String replyUName;
    //评论内容
    private String reviewContent;

    public long getReviewUid() {
        return reviewUid;
    }

    public void setReviewUid(long reviewUid) {
        this.reviewUid = reviewUid;
    }

    public String getReviewUName() {
        return reviewUName;
    }

    public void setReviewUName(String reviewUName) {
        this.reviewUName = reviewUName;
    }

    public long getReplyUid() {
        return replyUid;
    }

    public void setReplyUid(long replyUid) {
        this.replyUid = replyUid;
    }

    public String getReplyUName() {
        return replyUName;
    }

    public void setReplyUName(String replyUName) {
        this.replyUName = replyUName;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
