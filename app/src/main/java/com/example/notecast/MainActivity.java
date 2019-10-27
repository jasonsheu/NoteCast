package com.example.notecast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.notecast.PageAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 6;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 9;
    private static final int PERMISSIONS_REQUEST_WRITE_AND_RECORD = 69;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setContentView(R.layout.activity_main_menu);
        /*
        play = (Button) findViewById(R.id.next);
        stop = (Button) findViewById(R.id.prev);
        record = (Button) findViewById(R.id.record);
        record.setEnabled( false );
        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();

        if ( ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.RECORD_AUDIO ) != PackageManager.PERMISSION_GRANTED ) {
            // Permission is not granted
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO },
                        PERMISSIONS_REQUEST_WRITE_AND_RECORD );
        } else if( ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE );
        } else if( ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.RECORD_AUDIO ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO }, PERMISSIONS_REQUEST_RECORD_AUDIO );
        } else {
            initButtonsAndRecorder();
        }
        */

        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.

        tabLayout.addTab(tabLayout.newTab().setText(R.string.Recording));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Saved_Lectures));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.Lecture_Review));


        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Use PagerAdapter to manage page views in fr
        // Use PagerAdapter to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager viewPager = findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        // Setting a listener for click
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initButtonsAndRecorder() {
        record.setEnabled( true );

        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    System.out.println( ise );
                } catch (IOException ioe) {
                    System.out.println( ioe );
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                record.setEnabled( true );
                stop.setEnabled( false );
                play.setEnabled( true );
                Toast.makeText( getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG ).show();
            }
        } );

        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource( outputFile );
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText( getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG ).show();
                } catch( Exception e ) {
                    // make something
                }
            }
        } );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 1 && permissions[0].equals( Manifest.permission.WRITE_EXTERNAL_STORAGE ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(outputFile);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                // make something
                            }
                        }
                    });

                    stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                            myAudioRecorder = null;
                            record.setEnabled(true);
                            stop.setEnabled(false);
                            play.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
                        }
                    });

                    if( ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.RECORD_AUDIO ) == PackageManager.PERMISSION_GRANTED ) {
                        record.setEnabled( true );

                        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                        myAudioRecorder.setOutputFile(outputFile);

                        record.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    myAudioRecorder.prepare();
                                    myAudioRecorder.start();
                                } catch (IllegalStateException ise) {
                                    System.out.println( ise );
                                } catch (IOException ioe) {
                                    System.out.println( ioe );
                                }
                                record.setEnabled(false);
                                stop.setEnabled(true);
                                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    stop.setEnabled( false );
                    play.setEnabled( false );
                }
                return;
            }
            case PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if( grantResults.length == 1 && permissions[ 0 ].equals( Manifest.permission.RECORD_AUDIO ) && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED ) {
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);

                    record.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                myAudioRecorder.prepare();
                                myAudioRecorder.start();
                            } catch (IllegalStateException ise) {
                                System.out.println( ise );
                            } catch (IOException ioe) {
                                System.out.println( ioe );
                            }
                            record.setEnabled(false);
                            stop.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                        }
                    });

                    if( ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ) {
                        record.setEnabled( true );

                        stop.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                myAudioRecorder.stop();
                                myAudioRecorder.release();
                                myAudioRecorder = null;
                                record.setEnabled( true );
                                stop.setEnabled( false );
                                play.setEnabled( true );
                                Toast.makeText( getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG ).show();
                            }
                        } );

                        play.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                MediaPlayer mediaPlayer = new MediaPlayer();
                                try {
                                    mediaPlayer.setDataSource( outputFile );
                                    mediaPlayer.prepare();
                                    mediaPlayer.start();
                                    Toast.makeText( getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG ).show();
                                } catch( Exception e ) {
                                    // make something
                                }
                            }
                        } );
                    }
                } else {
                    record.setEnabled( false );
                }
                return;
            }
            case PERMISSIONS_REQUEST_WRITE_AND_RECORD: {
                if( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED && grantResults[ 1 ] == PackageManager.PERMISSION_GRANTED ) {
                    initButtonsAndRecorder();
                } else {
                    record.setEnabled( false );
                }
                return;
            }
        }
    }
}