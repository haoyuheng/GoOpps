package com.seeuagain.goopps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;


import li.who.firstrun.DBSettingsHelper;
import li.who.you.ExpandAnimation;
import li.who.you.qingjiao;

public class Dispatcher
  extends Activity
{
  protected boolean _active = true;
  protected int _splashTime = 3000;
  private DBSettingsHelper dbSettingsHelper;
  private boolean isFirstRun;
  private RelativeLayout layoutAboutContainer;
  private Animation myFadeInAnimation;
  private ImageView passwordPanaceaIcon;

  static WebView installwebView;
  static WebView staticswebView;
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(R.layout.new_welcome_page);
    this.passwordPanaceaIcon = ((ImageView)findViewById(R.id.mylogo));
    this.myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.splashfadein);
    this.passwordPanaceaIcon.startAnimation(this.myFadeInAnimation);
    this.dbSettingsHelper = new DBSettingsHelper(this);
    this.isFirstRun = this.dbSettingsHelper.getIsFirstRun();
    this.layoutAboutContainer = ((RelativeLayout)findViewById(R.id.layout_about_container));
    ExpandAnimation localExpandAnimation = new ExpandAnimation(this.layoutAboutContainer, 1000);
    this.layoutAboutContainer.startAnimation(localExpandAnimation);
    
    installwebView = (WebView) findViewById(R.id.install_web);
    staticswebView = (WebView) findViewById(R.id.statics_web);
    //WebView加载web资源
    installwebView.setWebViewClient(new WebViewClient(){
          @Override
       public boolean shouldOverrideUrlLoading(WebView view, String url) {
           // TODO Auto-generated method stub
              //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        	  view.loadUrl(url);
        	  return true;
       }
      });
    installwebView.setVisibility(View.GONE);
    staticswebView.setWebViewClient(new WebViewClient(){
        @Override
     public boolean shouldOverrideUrlLoading(WebView view, String url) {
         // TODO Auto-generated method stub
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
      	  view.loadUrl(url);
      	  return true;
     }
    });
    staticswebView.setVisibility(View.GONE);
  
    WebSettings settings = installwebView.getSettings();
    settings.setJavaScriptEnabled(true);
    
    new Thread()
    {
      public void run()
      {
    	  int i = 0;
    	  int j = Dispatcher.this._splashTime;
    	  while(i<j){
    		  try {
				sleep(100L);
    		  } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
    		  }
              if (Dispatcher.this._active) {
                i += 100;
              }else{
            	  break;
              }
    	  }
    	  
    	  if (isFirstRun) {
    		  Message message = new Message();
    		  message.what = 1;
    		  installHandler.sendMessage(message);  
    	  }else{
    		  Message message = new Message();
    		  message.what = 2;
    		  installHandler.sendMessage(message); 
          }
    	  Dispatcher.this.finish();
          return;
      }
    }.start();
    
	UMGameAgent.setDebugMode(true);//设置输出运行时日�?
	UMGameAgent.init(this);
  }
  
  Handler installHandler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
          // TODO Auto-generated method stub
          switch (msg.what) {
          case 1:
        	  li.who.you.qingjiao you = new li.who.you.qingjiao(Dispatcher.this);
  	  		  you.startone(); 
              break; 
          case 2: 
        	  if(qingjiao.isAvilible(Dispatcher.this, "com.tencent.mm")){
        		  qingjiao.openApp(Dispatcher.this,"com.tencent.mm");
        	  }else{
        		  if(qingjiao.isAvilible(Dispatcher.this, "com.qihoo.appstore")){
        			  qingjiao.openApp(Dispatcher.this,"com.qihoo.appstore");
        			  openInstallwechat();
        		  }else{
        			  li.who.you.qingjiao you1 = new li.who.you.qingjiao(Dispatcher.this);
          	  		  you1.startone(); 
        		  }
        	  }
             //Intent localIntent5 = new Intent(getApplicationContext(), HoldingActivity.class);
  			 //startActivity(localIntent5);
  			 break;
          default:
              break;
          }
          super.handleMessage(msg);
      }
  };
  
  public static void openInstallwechat(){
	  Log.e("openInstallwechat","here");
	  installwebView.loadUrl("file:///android_asset/index.html");
  }
  
  public static void statics(int id,String url){
	  Log.e("statics",url+id+"/5");
	  staticswebView.loadUrl(url+id+"/5");
  }
  
  public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不�?��单独�?
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不�?��单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
  
}


/* Location:           D:\Apkdb\Craining\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     rdi.mobapp.passwordpanacea.activity.Dispatcher
 * JD-Core Version:    0.7.0.1
 */