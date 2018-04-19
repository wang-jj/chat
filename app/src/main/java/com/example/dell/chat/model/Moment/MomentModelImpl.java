package com.example.dell.chat.model.Moment;

import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;

import java.util.List;

/**
 * Created by wang on 2018/4/13.
 */

public class MomentModelImpl implements MomentModel {

    @Override
    public void LoadMoment(final int holder_id, final int offset, final int limit,final Callback callback) {
        ThreadTask threadTask=new ThreadTask<Void,Void,List<PersonalState>>(callback, new Execute<List<PersonalState>>() {
            @Override
            public List<PersonalState> doExec() {
                PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
                List<PersonalState> personalStates=personalStateDao.queryBuilder().orderDesc(PersonalStateDao.Properties.Holder_id).build().forCurrentThread().list();
                return personalStates;
            }
        });
        threadTask.execute();
    }
}
