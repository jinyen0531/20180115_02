package com.yenyu.a20180115_02;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView img,img2;
    ProgressBar pb;
    TextView tv, tv2, tv3;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
    }

    public void click1(View v) //點擊 抓取網路圖片 並顯示進度條
    {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str_url = "https://www.petmd.com/sites/default/files/petmd-cat-happy-10.jpg";
                URL url;
                try {
                    url = new URL(str_url); //初始化URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //利用網址抓取連結
                    conn.setRequestMethod("GET"); //使用什麼方法做連線
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    final int Totallength = conn.getContentLength(); //抓取連接總長度
                    int length;
                    int sum = 0;

                    while ((length = inputStream.read(buf)) != -1)//直到抓不到顯示-1為止
                    {
                        bos.write(buf, 0, length);
                        sum += length;
                        final int tmp = sum;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(String.valueOf(tmp / Totallength * 100) + "%");
                                pb.setProgress(100 * tmp / Totallength);
                            } //progressbar進度條
                        });
                    }
                    byte[] results = bos.toByteArray();
                    final Bitmap bmp = BitmapFactory.decodeByteArray(results, 0, results.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(bmp);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }.start();
    }

    public void click2(View v) {
        MyTask task = new MyTask();
        task.execute(5);
    }

    class MyTask extends AsyncTask<Integer, Integer, String> { //AsyncTask 不須另開執行緒的方法
        @Override //UI上執行 執行完的結果
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv3.setText(s);
        }

        @Override //UI上執行 進度更新
        protected void onProgressUpdate(Integer... values) {
            //...為陣列 可放單個或多個值 (...一個函式只能一個，且必須放最後面)
            super.onProgressUpdate(values);
            tv2.setText(String.valueOf(values[0]));
        }

        @Override //背景執行 實際執行程式碼
        protected String doInBackground(Integer... integers) {
            int i;
            for (i = 0; i < integers[0]; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("TASK", "doInBackground,i:" + i);
            publishProgress(i);


            return "finish"; //傳值到onPostExecute的String s
        }
    }

    public void click3(View v) {
        MyImageTask imagetask = new MyImageTask();
        imagetask.execute("https://www.petmd.com/sites/default/files/petmd-cat-happy-10.jpg");
    }

    class MyImageTask extends AsyncTask<String, Integer, Bitmap>
    {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img2.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String str_url = strings[0];
            URL url;
            try {
                url = new URL(str_url); //初始化URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //利用網址抓取連結
                conn.setRequestMethod("GET"); //使用什麼方法做連線
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int length;

                while ((length = inputStream.read(buf)) != -1)//直到抓不到顯示-1為止
                {
                    bos.write(buf, 0, length);
                }
                byte[] results = bos.toByteArray();
                final Bitmap bmp = BitmapFactory.decodeByteArray(results, 0, results.length);
                return  bmp;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
