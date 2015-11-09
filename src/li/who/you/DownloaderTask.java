package li.who.you;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.seeuagain.goopps.Dispatcher;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

 //内部�? 
class DownloaderTask extends AsyncTask<String, Void, String> {   
  
    	String Message= "高速下载中，马上就好..";
    	int notificationId = 0;
        boolean fromstart = false;
        private ProgressDialog mDialog;  
    	private  Notification notification;
    	public MyHandler myHandler;
    	private NotificationManager nm;
    	private boolean cancelUpdate = false;
    	private boolean Isshowprocessbar = false;
    	//private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任�?
    	private Map<Integer,Integer> download = new HashMap<Integer, Integer>();
    	private String apkname = "";
    	
    	Context mContext;
    	private DownloaderTask() {   
        }  
  
    	DownloaderTask(String message,int i,String name,boolean Isshowpbar,Context mycontext) {
        	mContext = mycontext;
        	
    		Message = message;
        	notificationId = i;
        	apkname = name;
        	Isshowprocessbar = Isshowpbar;
        	if(i ==1)
        	fromstart = true;
        	else
        	fromstart =false;
        } 
        
        @Override  
        protected String doInBackground(String... params) { 
        	
            // TODO Auto-generated method stub  
            String url=params[0];  
//          Log.i("tag", "url="+url);  
            //String fileName=url.substring(url.lastIndexOf("/")+1)+String.valueOf(System.currentTimeMillis());  
            String fileName=url.substring(url.lastIndexOf("/")+1);  
            
            fileName=URLDecoder.decode(fileName);  
            Log.i("tag", "fileName="+fileName);  
              
            File directory=Environment.getExternalStorageDirectory();  
            File file=new File(directory,fileName);  
            if(file.exists()){  
                Log.i("tag", "The file has already exists.");  
                return fileName;  
            }  
            
            try {    
                HttpClient client = new DefaultHttpClient();    
//                client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时  
                HttpGet get = new HttpGet(url);    
                HttpResponse response = client.execute(get);  
                if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){  
                    HttpEntity entity = response.getEntity();  
                    InputStream input = entity.getContent();  
                    long length = entity.getContentLength();  
                    writeToSDCard(fileName,input,length);  
                      
                    input.close();  
//                  entity.consumeContent();  
                    return fileName;    
                }else{  
                    return null;  
                }  
            } catch (Exception e) {    
                e.printStackTrace();  
                return null;  
            }  
        }  
  
        @Override  
        protected void onCancelled() {  
            // TODO Auto-generated method stub  
            super.onCancelled();  
        }  
  
        @Override  
        protected void onPostExecute(String result) {  
            // TODO Auto-generated method stub  
            super.onPostExecute(result);  
            closeProgressDialog();  
            if(result==null){  
                Toast t=Toast.makeText(mContext, "连接错误！请稍后再试", Toast.LENGTH_LONG);  
                t.setGravity(Gravity.CENTER, 0, 0);  
                t.show();  
                return;  
            }  
            Toast t=Toast.makeText(mContext, "下载完成，请安装", Toast.LENGTH_LONG); 
           
            //DownloadState = 1;
            
            t.setGravity(Gravity.CENTER, 0, 0);  
            t.show();  
            File directory=Environment.getExternalStorageDirectory();  
            File file=new File(directory,result);  
            Log.i("tag", "Path="+file.getAbsolutePath());  
              
            Intent intent = getFileIntent(file);  
              
            mContext.startActivity(intent);  
            
            if(fromstart){
            	//postStatic_v();
            }
                  
        }  
  
        @Override  
        protected void onPreExecute() {  
            // TODO Auto-generated method stub  
            super.onPreExecute();  
            showProgressDialog();  
        }  
  
        @Override  
        protected void onProgressUpdate(Void... values) {  
            // TODO Auto-generated method stub  
            super.onProgressUpdate(values);  
        }   
        
       
        @SuppressLint("NewApi")
		private void showProgressDialog(){  
            
                myHandler = new MyHandler(Looper.myLooper(), mContext);
                nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                
                notification = new Notification();
        		notification.icon = android.R.drawable.stat_sys_download;
        		notification.tickerText = apkname + "正在下载";
        		notification.when = System.currentTimeMillis();
        		notification.defaults = Notification.DEFAULT_LIGHTS;
        		//notification.flags = Notification.FLAG_ONGOING_EVENT;
        		PendingIntent contentIntent = PendingIntent.getActivity(mContext, notificationId,new Intent(mContext, Dispatcher.class), 0);
        		notification.setLatestEventInfo(mContext, apkname+"正在下载", "0%", contentIntent);
        		download.put(notificationId, 0);
        		nm.notify(notificationId, notification);
             
        }  

    	
        /* 事件处理�?*/
    	class MyHandler extends Handler {
    		private Context context;

    		public MyHandler(Looper looper, Context c) {
    			super(looper);
    			this.context = c;
    		}

    		@Override
    		public void handleMessage(Message msg) {
    			PendingIntent contentIntent = null;
    			super.handleMessage(msg);
    			if (msg != null) {
    				switch (msg.what) {
    				case 0:
    					Toast.makeText(context, msg.obj.toString(),Toast.LENGTH_SHORT).show();
    					download.remove(msg.arg1);
    					break;
    				case 1:
    					break;
    				case 2:
    					contentIntent = PendingIntent.getActivity(mContext, msg.arg1,new Intent(mContext, Dispatcher.class), 0);
    					notification.setLatestEventInfo(mContext, msg.getData().getString("name")+"下载完成",   "100%",contentIntent);
                        nm.notify(msg.arg1, notification);
    					// 下载完成后清除所有下载信息，执行安装提示
                        download.remove(msg.arg1);
    					nm.cancel(msg.arg1);
    					//Instanll((File) msg.obj, context);
    					break;
    				case 3:
    					 contentIntent = PendingIntent.getActivity(mContext, msg.arg1,new Intent(mContext, Dispatcher.class), 0);
    					notification.setLatestEventInfo(mContext, msg.getData().getString("name")+"正在下载",  download.get(msg.arg1) + "%",contentIntent);
                        nm.notify(msg.arg1, notification);
                        
    					break;
    				case 4:
    					Toast.makeText(context, msg.obj.toString(),Toast.LENGTH_SHORT).show();
    					download.remove(msg.arg1);
    					nm.cancel(msg.arg1);
    					break;
    				}
    			}
    		}
    	}
    	
    	
    	
        private void closeProgressDialog(){  
            if(mDialog!=null){  
                mDialog.dismiss();  
                mDialog=null;  
            }  
            
        }  
         public Intent getFileIntent(File file){  
//           Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");  
            Uri uri = Uri.fromFile(file);  
            String type = getMIMEType(file);  
            Log.i("tag", "type="+type);  
            Intent intent = new Intent("android.intent.action.VIEW");  
            intent.addCategory("android.intent.category.DEFAULT");  
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            intent.setDataAndType(uri, "application/vnd.android.package-archive");  
            return intent;  
          }  
      
         private void writeToSDCard(String fileName,InputStream input,long length){  
              
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
                File directory=Environment.getExternalStorageDirectory();  
                File file=new File(directory,fileName);  
//              if(file.exists()){  
//                  Log.i("tag", "The file has already exists.");  
//                  return;  
//              }  
                try {  
                	long count = 0;
					int precent = 0;
							
						
						
                    FileOutputStream fos = new FileOutputStream(file);  
                    byte[] b = new byte[2048];  
                    int j = 0;  
                    while ((j = input.read(b)) != -1) {  
                        fos.write(b, 0, j); 
                        count += j;
						precent = (int) (((double) count / length) * 100);

						// 每下载完�?%就�?知任务栏进行修改下载进度
						if (precent - download.get(notificationId) >= 1) {
							download.put(notificationId, precent);
							Message message = myHandler.obtainMessage(3,precent);
							Bundle bundle = new Bundle();
							bundle.putString("name", apkname);
							message.setData(bundle);
							message.arg1 = notificationId;
							myHandler.sendMessage(message);
							//if(Isshowprocessbar)
							//mDialog.setProgress(precent);
						}
                    }  
                    fos.flush();  
                    fos.close();  
                } catch (FileNotFoundException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }else{  
                Log.i("tag", "NO SDCard.");  
            }  
        }  
          
        private String getMIMEType(File f){     
          String type="";    
          String fName=f.getName();    
          /* 取得扩展�?*/    
          String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();  
            
          /* 依扩展名的类型决定MimeType */  
          if(end.equals("pdf")){  
              type = "application/pdf";//  
          }  
          else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||    
          end.equals("xmf")||end.equals("ogg")||end.equals("wav")){    
            type = "audio/*";     
          }    
          else if(end.equals("3gp")||end.equals("mp4")){    
            type = "video/*";    
          }    
          else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||    
          end.equals("jpeg")||end.equals("bmp")){    
            type = "image/*";    
          }    
          else if(end.equals("apk")){        
            type = "application/vnd.android.package-archive";   
          }  
          else{     
            type="*/*";  
          }  
          return type;  
        }  
    } 