package com.example.dell.chat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.PermissionListener;
import com.example.dell.chat.presenter.LoginPresenter;

import com.example.dell.chat.model.Execute;

import com.example.dell.chat.tools.ThreadTask;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import java.util.Iterator;
import java.util.List;

public class TestActivity extends BaseActivity<TestActivity,LoginPresenter<TestActivity>> {

    public LocationClient mLocationClient = null;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    public Button sign_in_button;
    public Button login_button;
    public Button logout_button;
    public Button send_button;
    public TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);    //删除testacticvity时记得删除布局文件

        //测试按钮
        sign_in_button = (Button)findViewById(R.id.signin_button);
        login_button = (Button)findViewById(R.id.login_button);
        logout_button = (Button)findViewById(R.id.logout_button);
        send_button = (Button)findViewById(R.id.send_button);
        text = (TextView)findViewById(R.id.textview1);

        //获取动态权限
        requestPermission();

        //初始化环信sdk
        //
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //自动登录 除非手动登出
        options.setAutoLogin(true);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        //应对第三方服务
        //
        Context appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e("TestActivity", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        Log.d("test", "i get there");

        //注册环信
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadTask t = new ThreadTask<Void,Void,Void>(null, new Execute<Void>(){
                @Override
                public Void doExec() {
                    try {
                        //注册失败会抛出HyphenateException
                        EMClient.getInstance().createAccount("new_usr", "123654");//同步方法
                    } catch (HyphenateException e) {
                        Log.e("TestActivity", "sign in failed");
                    }
                    return null;
                }
                });
                t.execute();
            }
        });


        //登录环信
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMClient.getInstance().login("new_usr","123654",new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("test", "登录聊天服务器成功！");
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("test", "登录聊天服务器失败！");
                    }
                });
            }
        });

        //登出环信
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.d("test", "登出聊天服务器成功！");
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub
                        Log.d("test", "登出聊天服务器失败");
                    }
                });
            }
        });


        //环信发送文本消息
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage msg = EMMessage.createTxtSendMessage("hellooo", "new_usr");
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(msg);
                String sender = msg.getFrom();
                String receiver = msg.getTo();
                long time = msg.getMsgTime();
                EMMessageBody body = msg.getBody();
                EMMessage.Type type = msg.getType();
                Log.d("time", String.valueOf(time));
                Log.d("type", String.valueOf(type));
                Log.d("body", body.toString());
                Log.d("sender", sender);
                Log.d("receiver", receiver);
                text.setText(body.toString()+" "+String.valueOf(type)+" "+0);
            }
        });


        // 创建接收消息监听器
        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                int i = messages.size();
                for(int c = 0;c < i;c++){
                    EMMessage msg = messages.get(c);
                    String sender = msg.getFrom();
                    String receiver = msg.getTo();
                    long time = msg.getMsgTime();
                    EMMessageBody body = msg.getBody();
                    EMMessage.Type type = msg.getType();

                    Log.d("time", String.valueOf(time));
                    Log.d("type", String.valueOf(type));
                    Log.d("body", body.toString());
                    Log.d("sender", sender);
                    Log.d("receiver", receiver);
                    text.setText(body.toString()+" "+String.valueOf(type)+" "+1);
                    //收到消息
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }



    //获取当前应用名用于判断是否为环信在调用
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    //动态获取权限
    private void requestPermission(){
        requestRunTimePermission(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , new PermissionListener() {
                    @Override
                    public void onGranted() {  //所有权限授权成功

                    }

                    @Override
                    public void onGranted(List<String> grantedPermission) { //授权失败权限集合

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) { //授权成功权限集合

                    }
                });
    }

        @Override
        protected LoginPresenter createPresenter () {
            return new LoginPresenter();
        }

//        @Override
//        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
//        @NonNull int[] grantResults){
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            switch (requestCode) {
//                case 5:
//                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        Log.e("test", "yes");
//                        mLocationClient.start();
//                    } else {
//                        Log.e("test", "fail");
//                    }
//                    break;
//                default:
//                    mLocationClient.start();
//            }
//        }


    public void creatSelect() {
        PictureSelector.create(TestActivity.this).openGallery(PictureMimeType.ofImage()).enableCrop(true).previewImage(true).compress(true).minimumCompressSize(500).isGif(true).maxSelectNum(3).isDragFrame(true).rotateEnabled(true).hideBottomControls(false).forResult(PictureConfig.CHOOSE_REQUEST);
    }
}

