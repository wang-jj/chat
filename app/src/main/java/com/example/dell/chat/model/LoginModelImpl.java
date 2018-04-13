package com.example.dell.chat.model;

import android.os.AsyncTask;

import com.example.dell.chat.bean.User;

import org.litepal.crud.DataSupport;


/**
 * Created by wang on 2018/4/12.
 */

public class LoginModelImpl implements LoginModel {

    private static class Task<T,V,C> extends AsyncTask<T,V,C>{
        private Callback<C> callback;
        private Execute<C> exe;
        @Override
        protected C doInBackground(T... params){
            return exe.doExec();
        }
        @Override
        protected void onPostExecute(C c) {
            super.onPostExecute(c);
            if(callback!=null){
                callback.execute(c);
            }
            else return;
        }
        public Task(Callback callback, Execute exe){
            super();
            this.callback=callback;
            this.exe=exe;
        }
    }

    @Override
    public void FindLastUser(final Callback<User> callback){
        User u=DataSupport.findFirst(User.class);
        callback.execute(u);
    }

    @Override
    public void UpdateUser(final User u){
        Task t=new Task<Void,Void,Void>(null, new Execute<Void>() {
            @Override
            public Void doExec() {
                if (u.isSaved()) {
                    u.save();
                } else {
                    DataSupport.deleteAll(User.class);
                    u.save();
                }
                return null;
            }
        });
        t.execute();
    }

    @Override
    public void CreateUser(final User u){
        UpdateUser(u);
    }
}
