package com.example.dell.chat.model.Comment;

import com.example.dell.chat.model.Callback;

/**
 * Created by wang on 2018/5/7.
 */

public interface CommentModel {

    //发评论
    void UpComment(final int momentID, final int user_id, final int holder_id, final String content, final Callback<String> callback);
    //下载评论
    void LoadComment(int momentid,final Callback<String>callback);
}
