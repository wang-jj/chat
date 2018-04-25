package com.example.dell.chat.model.Moment;

import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;

/**
 * Created by wang on 2018/4/13.
 */

public interface MomentModel {

    void LoadMoment(int holder_id, int offset, int limit, final Callback callback);

    void Publish(PersonalState personalState,final Callback callback);
}
