package com.hnb.socketioandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends Activity implements View.OnClickListener
{
    private EditText edtMessage;
    private TextView txtData;
    private Button btnSend;
    private Button btnGetPosts;
    private Button btnGetAddresses;
    private Button btnGetCategories;
    private Button btnGetCats;
    private Button btnInsertCat;
    private Button btnClear;
    private Button btnEcho;


    public static final String FromAndroid_GetPosts = "FromAndroid_GetPosts";
    public static final String FromAndroid_GetAddresses = "FromAndroid_GetAddresses";
    public static final String FromAndroid_GetCategories = "FromAndroid_GetCategories";
    public static final String FromAndroid_InsertCat = "FromAndroid_InsertCat";
    public static final String FromAndroid_GetCats = "FromAndroid_GetCats";
    public static final String FromAndroid_Echo = "FromAndroid_Echo";




    public static final String ToAndroid_GetPosts = "ToAndroid_GetPosts";
    public static final String ToAndroid_GetAddresses = "ToAndroid_GetAddresses";
    public static final String ToAndroid_GetCategories = "ToAndroid_GetCategories";
    public static final String ToAndroid_InsertCat = "ToAndroid_InsertCat";
    public static final String ToAndroid_GetCats = "ToAndroid_GetCats";
    public static final String ToAndroid_Echo = "ToAndroid_Echo";

    private Socket mSocket;

    {
        try
        {

            mSocket = IO.socket("http://socketapi101-90422.onmodulus.net/");
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initSocket();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        terminalSocket();

    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.btnSend)
        {
            attemptSend("from android", edtMessage.getText().toString().trim());
        }

        if (id == R.id.btnPost)
        {
            attemptSend(FromAndroid_GetPosts, "{Android: 'Get Posts'}");
        }

        if (id == R.id.btnCate)
        {
            attemptSend(FromAndroid_GetCategories, "{Android: 'Get Categories'}");
        }

        if (id == R.id.btnAddress)
        {
            attemptSend(FromAndroid_GetAddresses, "{Android: 'Get Addresses'}");
        }

        if(id == R.id.btnInsertCat)
        {
            attemptSend(FromAndroid_InsertCat, "{Android: 'Insert Cat'}");
        }

        if(id == R.id.btnGetCats)
        {
            attemptSend(FromAndroid_GetCats, "get cats");
        }


        if(id == R.id.btnEcho)
        {
            attemptSend(FromAndroid_Echo, edtMessage.getText().toString().trim());
        }


        if(id == R.id.btnClear)
        {
            txtData.setText("");
        }
    }

    private void initSocket()
    {
        mSocket.connect();
        mSocket.on("to android", onNewMessage);
        mSocket.on("broadcast", onNewMessage);
        mSocket.on(ToAndroid_GetAddresses, onNewMessage);
        mSocket.on(ToAndroid_GetCategories, onNewMessage);
        mSocket.on(ToAndroid_GetPosts, onNewMessage);
        mSocket.on(ToAndroid_InsertCat, onNewMessage);
        mSocket.on(ToAndroid_GetCats, onNewMessage);
        mSocket.on(ToAndroid_Echo, onNewMessage);
    }

    private void terminalSocket()
    {
        mSocket.off("to android", onNewMessage);
        mSocket.off("broadcast", onNewMessage);
        mSocket.off(ToAndroid_GetPosts, onNewMessage);
        mSocket.off(ToAndroid_GetCategories, onNewMessage);
        mSocket.off(ToAndroid_GetAddresses, onNewMessage);
        mSocket.off(ToAndroid_InsertCat, onNewMessage);
        mSocket.off(ToAndroid_GetCats, onNewMessage);
        mSocket.off(ToAndroid_Echo, onNewMessage);
        mSocket.disconnect();
    }

    private void initView()
    {
        btnSend = (Button) findViewById(R.id.btnSend);
        btnGetAddresses = (Button) findViewById(R.id.btnAddress);
        btnGetCategories = (Button) findViewById(R.id.btnCate);
        btnGetPosts = (Button) findViewById(R.id.btnPost);
        btnGetCats = (Button) findViewById(R.id.btnGetCats);
        btnInsertCat = (Button) findViewById(R.id.btnInsertCat);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnEcho = (Button) findViewById(R.id.btnEcho);


        edtMessage = (EditText) findViewById(R.id.edtMess);
        txtData = (TextView) findViewById(R.id.txtData);

        btnSend.setOnClickListener(this);
        btnGetPosts.setOnClickListener(this);
        btnGetCategories.setOnClickListener(this);
        btnGetAddresses.setOnClickListener(this);
        btnGetCats.setOnClickListener(this);
        btnInsertCat.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnEcho.setOnClickListener(this);
    }


    private void attemptSend(String event, String data)
    {
        mSocket.emit(event, data);
    }


    Emitter.Listener onNewMessage = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            final String data = (String) args[0];

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    txtData.append(data.toString() + '\n' + '\n');
                }
            });

        }
    };
}
