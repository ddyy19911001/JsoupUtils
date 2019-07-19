package yin.deng.jsouputils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import yin.deng.dyjsouputils.JsoupUtils;
import yin.deng.dyjsouputils.LogUtils;
import yin.deng.dyjsouputils.MyUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JsoupUtils.getHtmlDoctmentNoParams(this, "https://www.iqshw.com", new JsoupUtils.OnDataGetListener() {
            @Override
            public void onGetData(Document data) {
                LogUtils.d("data:\n"+data.toString());
                Toast.makeText(MainActivity.this,"获取内容成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErro(String erro) {
                LogUtils.e("data:\n内容获取失败");
                Toast.makeText(MainActivity.this,"获取内容成功",Toast.LENGTH_LONG).show();
            }
        });
    }
}
