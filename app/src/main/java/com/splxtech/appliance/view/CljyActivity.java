package com.splxtech.appliance.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.splxtech.appliance.R;

/**
 * Created by li300 on 2016/8/17 0017.
 */
public class CljyActivity extends Activity implements View.OnClickListener
{
    private Button buttonBack;
    private WebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cljy);
        buttonBack = (Button)findViewById(R.id.button_cljy_back);
        buttonBack.setText("< 功耗分析");
        buttonBack.setOnClickListener(this);
        buttonBack.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonBack.setTextColor(Color.rgb(220,221,222));
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    buttonBack.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        webView = (WebView)findViewById(R.id.web_clyj_context);
        webView.getSettings().setJavaScriptEnabled(true);
        try{
            webView.loadUrl("http://www.baidu.com");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view)
    {
        finish();
    }


}
