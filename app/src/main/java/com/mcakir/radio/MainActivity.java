package com.mcakir.radio;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.mcakir.radio.player.PlaybackStatus;
import com.mcakir.radio.player.RadioManager;
import com.mcakir.radio.util.Shoutcast;
import com.mcakir.radio.util.ShoutcastHelper;
import com.mcakir.radio.util.ShoutcastListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private int Lastposition = 0;
    private ShoutcastListAdapter listAdapter;

    //@BindView(R.id.playTrigger)
    //ImageButton trigger;

    //@BindView(R.id.listview)
    //ListView listView;

    private static GridView gridView;


    //@BindView(R.id.sub_player)
    //View subPlayer;

    RadioManager radioManager;

    String streamURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);

        ButterKnife.bind(this);


        radioManager = RadioManager.with(this);

        listAdapter = new ShoutcastListAdapter(this, ShoutcastHelper.retrieveShoutcasts(this));
        gridView.setAdapter(listAdapter);

    }

    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        radioManager.unbind();

        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();

        radioManager.bind();

    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Subscribe
    public void onEvent(String status){

        switch (status){

            case PlaybackStatus.LOADING:

                // loading

                break;

            case PlaybackStatus.ERROR:

                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();

                break;

        }

        //trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
        //        ? R.drawable.ic_pause_black
         //       : R.drawable.ic_play_arrow_black);

    }

    //@OnClick(R.id.playTrigger)
    //public void onClicked(){
//
//        if(TextUtils.isEmpty(streamURL)) return;
//
//        radioManager.playOrPause(streamURL);
//    }
/*
    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Shoutcast shoutcast = (Shoutcast) parent.getItemAtPosition(position);
        if(shoutcast == null){

            return;

        }

        textView.setText(shoutcast.getName());

        subPlayer.setVisibility(View.VISIBLE);

        streamURL = shoutcast.getUrl();

        radioManager.playOrPause(streamURL);
    }
    */

    @OnItemClick(R.id.gridview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Shoutcast shoutcast = (Shoutcast) parent.getItemAtPosition(position);
        if(shoutcast == null){
            return;
        }

        View gridViewold = gridView.getChildAt(Lastposition);
        if(gridViewold!=null)
            gridViewold.setBackgroundColor(Color.WHITE);

        View gridview = gridView.getChildAt(position);
        if(gridview!=null)
            gridview.setBackgroundColor(Color.RED);

        Lastposition=position;



        //subPlayer.setVisibility(View.VISIBLE);

        streamURL = shoutcast.getUrl();

        radioManager.playOrPause(streamURL);


    }


    public static void setDefaultPlayer(){

        final int defaultPosition = 0;

        gridView.performItemClick(
                gridView.getChildAt(defaultPosition), //gridView.getAdapter().getView(defaultPosition, null, null),
                defaultPosition,
                gridView.getAdapter().getItemId(defaultPosition));


        //gridView.getAdapter().getView(defaultPosition, null, null).performClick();

        //Handler handler = new Handler();
        //handler.post(new Runnable() {
        //    @Override
        //    public void run() {
        //        gridView.performItemClick(
        //                gridView.getChildAt(defaultPosition), //gridView.getAdapter().getView(defaultPosition, null, null),
        //                defaultPosition,
        //                gridView.getAdapter().getItemId(defaultPosition));
        //    }
        //});
    }

}
