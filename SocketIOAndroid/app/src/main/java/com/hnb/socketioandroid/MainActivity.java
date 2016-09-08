package com.hnb.socketioandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends Activity implements View.OnClickListener
{

    private EditText edtMessage;
    private TextView txtData;
    private Button btnSend;

    private Socket mSocket;
    {
        try
        {
            mSocket = IO.socket("http://socketioserver-89654.onmodulus.net/");
        }
        catch (URISyntaxException e)
        {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mSocket.connect();
        mSocket.on("to android", onNewMessage);
        //mSocket.on("broadcast", onNewMessage);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("to android", onNewMessage);
        //mSocket.off("broadcast", onNewMessage);
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.btnSend)
        {
            attemptSend("Android: " + edtMessage.getText().toString().trim());
        }
    }


    private void initView()
    {
        btnSend = (Button) findViewById(R.id.btnSend);
        edtMessage = (EditText) findViewById(R.id.edtMess);
        txtData = (TextView) findViewById(R.id.txtData);

        btnSend.setOnClickListener(this);
    }


    private void attemptSend(String data)
    {
        mSocket.emit("from android", data);
    }


    Emitter.Listener onNewMessage = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            JSONObject data = (JSONObject) args[0];
            final String x;
            final String c;

            try
            {
                x = data.getString("name");
                c = data.getString("email");
            }
            catch (JSONException e)
            {
                return;
            }


            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    txtData.append( "\n" + x + " " + c);
                }
            });

        }
    };
}
