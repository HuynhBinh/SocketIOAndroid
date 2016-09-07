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
        {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mSocket.connect();
        mSocket.on("to android", onNewMessage);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.btnSend)
        {
            attemptSend("data from android device");
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
            String data = (String) args[0];
            txtData.setText(data);
        }
    };
}
