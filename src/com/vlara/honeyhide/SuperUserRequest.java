package com.vlara.honeyhide;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SuperUserRequest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superuser);
        Button ask = (Button)findViewById(R.id.Ask);
        ask.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Process process = Runtime.getRuntime().exec("su -c ls");
                            process.waitFor();
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.run();
            }
            
        });
    }
    
}
