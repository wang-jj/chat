package com.example.dell.chat.tools;

import android.os.AsyncTask;

import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;

/**
 * Created by wang on 2018/4/13.
 */

//多线程的类，用于model层，Execute指明要多线程执行的方法，Callback指明执行完以后要回调的方法
public class ThreadTask<T,V,C> extends AsyncTask<T,V,C>{
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
    public ThreadTask(Callback callback, Execute exe){
        super();
        this.callback=callback;
        this.exe=exe;
    }
}
