package yin.deng.dyjsouputils;

import android.app.Activity;
import android.text.TextUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class JsoupUtils {
    public static  int TIME_OUT_SECONDE=12000;
    public static ExecutorService executorService;
    public static int threadCounts=5;

    public interface OnDataGetListener{
        void onGetData(Document data);
        void onErro(String erro);
    }

    public static void closeExecutorService(){
        if(executorService!=null&&!executorService.isShutdown()){
            executorService.shutdownNow();
            executorService=null;
        }
    }

    public static void closeExecutorServiceAsyc(){
        if(executorService!=null&&!executorService.isShutdown()){
            executorService.shutdown();
            executorService=null;
        }
    }


    //是否开启证书验证，关闭之后可请求https免证书
    public static void isValidateTLSCertificates(boolean isEnable){
      if(!isEnable){
          disableSSLCertCheck();
      }
    }

    public static void disableSSLCertCheck(){
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static void getHtmlDoctment(final Activity context, final String requestUrl, final boolean isGet
            , final List<JsoupParams> heads, final List<JsoupParams> bodys, final OnDataGetListener onDataGetListener){
        if(executorService==null){
            executorService= Executors.newFixedThreadPool(threadCounts);
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(heads)){
                        for(int i=0;i<heads.size();i++){
                            if(MyUtils.isEmpty(heads.get(i).getKey())){
                                continue;
                            }
                            connection.header(heads.get(i).getKey(),heads.get(i).getValues());
                        }
                    }
                    if(!MyUtils.isEmpty(bodys)){
                        for(int i=0;i<bodys.size();i++){
                            if(MyUtils.isEmpty(bodys.get(i).getKey())){
                                continue;
                            }
                            connection.data(bodys.get(i).getKey(),bodys.get(i).getValues());
                        }
                    }
                    final Document document;
                    if(isGet) {
                        document=connection.get();
                    }else {
                        document=connection.post();
                    }
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }


    public static void getHtmlDoctmentNeedParams(final Activity context, final String requestUrl
            , final List<JsoupParams> bodys, final OnDataGetListener onDataGetListener){
        if(executorService==null){
            executorService= Executors.newFixedThreadPool(threadCounts);
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(bodys)){
                        for(int i=0;i<bodys.size();i++){
                            if(MyUtils.isEmpty(bodys.get(i).getKey())){
                                continue;
                            }
                            connection.data(bodys.get(i).getKey(),bodys.get(i).getValues());
                        }
                    }
                    final Document document;
                    document=connection.get();
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    public static void getHtmlDoctmentNoParams(final Activity context, final String requestUrl, final OnDataGetListener onDataGetListener){
        if(executorService==null){
            executorService= Executors.newFixedThreadPool(threadCounts);
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    final Document document;
                    document=connection.get();
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }


    public static void getHtmlDoctmentNeedParams(final Activity context, final String requestUrl, final boolean isGet
            , final List<JsoupParams> bodys, final OnDataGetListener onDataGetListener){
        if(executorService==null){
            executorService= Executors.newFixedThreadPool(threadCounts);
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(bodys)){
                        for(int i=0;i<bodys.size();i++){
                            if(MyUtils.isEmpty(bodys.get(i).getKey())){
                                continue;
                            }
                            connection.data(bodys.get(i).getKey(),bodys.get(i).getValues());
                        }
                    }
                    final Document document;
                    if(isGet) {
                        document=connection.get();
                    }else {
                        document=connection.post();
                    }
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    public static void getHtmlDoctmentNeedHeaders(final Activity context, final String requestUrl, final boolean isGet
            , final List<JsoupParams> heads, final OnDataGetListener onDataGetListener){
        if(executorService==null){
            executorService= Executors.newFixedThreadPool(threadCounts);
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(requestUrl);
                    connection.timeout(TIME_OUT_SECONDE);
                    if(!MyUtils.isEmpty(heads)){
                        for(int i=0;i<heads.size();i++){
                            if(MyUtils.isEmpty(heads.get(i).getKey())){
                                continue;
                            }
                            connection.header(heads.get(i).getKey(),heads.get(i).getValues());
                        }
                    }
                    final Document document;
                    if(isGet) {
                        document=connection.get();
                    }else {
                        document=connection.post();
                    }
                    LogUtils.i("OnDoctmentGet:+\n"+document);
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onGetData(document);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    if(onDataGetListener!=null){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onDataGetListener.onErro(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 标签内通过id和class同时查找元素
     * @param doc
     * @param tag
     * @param className
     * @param id
     */
    public static ParseInfo parseInnerTag(Element doc, String tag, String className, String id){
        String selectStr="";
        if(!TextUtils.isEmpty(tag)){
            selectStr+=tag;
        }else{
            LogUtils.i("标签不能为空");
            return new ParseInfo(true);
        }
        if(!TextUtils.isEmpty(className)){
            selectStr += "[class=" + className + "]";
        }
        if(!TextUtils.isEmpty(id)){
            if(TextUtils.isEmpty(className)){
                selectStr+="[id="+id+"]";
            }else{
                selectStr+=tag+"[id="+id+"]";
            }
        }
        Elements elements=doc.select(selectStr);
        ParseInfo parseInfo=new ParseInfo(elements);
        if(elements==null||elements.size()==0){
            parseInfo.setEmpty(true);
        }
        return parseInfo;
    }


    /**
     * 直接通过Id查找元素
     * @param doc
     * @param id
     */
    public static ParseInfo parseById(Element doc,String id){
        String selectStr="";
        if(!TextUtils.isEmpty(id)){
            selectStr+="#"+id;
        }else{
            LogUtils.i("标签不能为空");
            return new ParseInfo(true);
        }
        Elements elements=doc.select(selectStr);
        ParseInfo parseInfo=new ParseInfo(elements);
        if(elements==null||elements.size()==0){
            parseInfo.setEmpty(true);
        }
        return parseInfo;
    }

    /**
     * 直接通过ClassName查找元素
     * @param doc
     * @param className
     */
    public static ParseInfo parseByClassName(Element doc,String className){
        String selectStr="";
        if(!TextUtils.isEmpty(className)){
            selectStr+="."+className;
        }else{
            LogUtils.i("标签不能为空");
            return new ParseInfo(true);
        }
        Elements elements=doc.select(selectStr);
        ParseInfo parseInfo=new ParseInfo(elements);
        if(elements==null||elements.size()==0){
            parseInfo.setEmpty(true);
        }
        return parseInfo;
    }

    /**
     * 直接通过标签中文本包含的内容查找元素
     * @param doc
     * @param tagName
     */
    public static ParseInfo parseByTagContiansTxt(Element doc,String tagName,String containsStr){
        String selectStr="";
        if(!TextUtils.isEmpty(tagName)){
            selectStr+=tagName;
        }else{
            LogUtils.i("标签不能为空");
            return new ParseInfo(true);
        }
        selectStr=selectStr+":contains("+containsStr+")";
        Elements elements=doc.select(selectStr);
        ParseInfo parseInfo=new ParseInfo(elements);
        if(elements==null||elements.size()==0){
            parseInfo.setEmpty(true);
        }
        return parseInfo;
    }


}
