package com.vasilkoff.easyvpnfree;

import static java.lang.Integer.parseInt;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class BootService extends Service {

    private static final String TAG = "MyService";
    MediaPlayer mediaPlayer = new MediaPlayer();
    private AudioManager audioManager;
    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ttt1.wav";
    private TextToSpeech textToSpeech;
    private WindowManager windowManager;
    private ImageView floatingGifView;


    private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Permanent loss of audio focus - stop playback and release resources
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                            }
                            mediaPlayer = null;
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            // Pause playback temporarily - can resume playback when focus regained
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Lower the volume, because another app is also playing audio
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                mediaPlayer.setVolume(0.2f, 0.2f); // Adjust as needed
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            // Regained focus - resume playback
                            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                mediaPlayer.setVolume(1.0f, 1.0f); // Reset volume back to normal

                            }
                            break;
                    }
                }
            };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        textToSpeech = new TextToSpeech((Context) this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (i == TextToSpeech.SUCCESS) {

                            Calendar calendar = Calendar.getInstance();

                            // Format the time as "12:30 PM"
                            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("h:a", Locale.US);

                            String formattedTime = sdf.format(calendar.getTime());
                            String formattedTime2 = sdf2.format(calendar.getTime());
                            String[] s = formattedTime2.split(":");
                            String t = "";
                            if(parseInt(s[0]) < 12 && s[1].equals("AM")) {
                                t = "Good Morning!";
                            }
                            if(parseInt(s[0]) > 12 && parseInt(s[0]) < 19 && s[1].equals("PM")) {
                                t = "Good Evening!";
                            } else {
                                t = "Good Night!";
                            }
                            String a = t+", Krishna!";
                            String b = "Hello Krishna, "+t+"!";
                            String c = "Welcome, Krishna!, "+t+".";
                            String d = "Hey Krishna, "+t+".";

                            String[] array = new String[]{a,b,c,d};
                            String rs = array[(int) (System.currentTimeMillis() % array.length)];
                            //
                            String a1 = "its currently "+formattedTime+".";
                            String b1 = "The time is "+formattedTime+".";
                            String c1 = "The clock reads "+formattedTime+".";

                            String[] array1 = new String[]{a1,b1,c1};
                            String rs1 = array1[(int) (System.currentTimeMillis() % array1.length)];

                            //
                            String a2 = "I hope your day has been great so far.";
                            String b2 = "I trust you've had a wonderful day.";
                            String c2 = "I hope your day has been enjoyable.";
                            String d2 = "I hope your day has treated you well.";
                            String e2 = "I hope you've had a splendid day so far.";

                            String[] array2 = new String[]{a2,b2,c2,d2, e2};
                            String rs2 = array2[(int) (System.currentTimeMillis() % array2.length)];

                            //
                            String a3 = "Buckle up and enjoy the journey ahead.";
                            String b3 = "Fasten your seatbelts and get ready for a fantastic ride!";
                            String c3 = "Strap in and let's make the most of this journey a joyful memory";
                            String d3 = "Secure your seatbelts and let's embark on a memorable journey!";
                            String e3 = "Prepare yourself and enjoy the ride.";

                            String[] array3 = new String[]{a3,b3,c3,d3,e3};
                            String rs3 = array3[(int) (System.currentTimeMillis() % array3.length)];


                            speakText(rs + rs1+ rs2 + rs3);                        }

                    }
                }, 20000);
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Speech started


            }

            @Override
            public void onDone(String utteranceId) {
                // Speech completed
            }

            @Override
            public void onError(String utteranceId) {
                // Error occurred
            }
        });
        super.onCreate();// Optional: Set a touch listener to move the floating GIF around
    }

    private void speakText(String text) {
        // Create HashMap to pass parameters to synthesizeToFile
        textToSpeech.setLanguage(Locale.US);
        textToSpeech.setSpeechRate(1.0f);
        // Configure synthesis parameters using HashMap
//        ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance_id");
        int result = textToSpeech.synthesizeToFile(text, params, filePath);

        if (result == TextToSpeech.SUCCESS) {
            Log.d("MYAPPBOOTSERVICE",filePath);
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    playAudioFile(filePath);
                }
            },10000);

        } else {
            Log.d("MYAPPBOOTSERVICE","FAILED TO SYNTHESIZE AUDIO");        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MYAPPBOOTSERVICE","SERVICE STARTED");

        return START_STICKY;
    }

    private void setMediaPlayer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.setDataSource(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(getApplicationContext(),"STARTED MUSIC NOW : ",Toast.LENGTH_SHORT).show();
                mediaPlayer.prepareAsync();
                MediaPlayer finalMediaPlayer = mediaPlayer;
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()

                {
                    public void onPrepared (MediaPlayer mp){

                        finalMediaPlayer.start();
                    }
                });
            }
        }, 3000);
    }

    private void playAudioFile(String filePath) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
//        mediaPlayer.stop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
        }
        try {

//                change with setDataSource(Context,Uri);
//                mediaPlayer.setDataSource(this, Uri.parse("https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba-online-audio-converter.com_-1.wav"));
            int result = audioManager.requestAudioFocus(audioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    Log.d("CHECK LOG : ","REMOVED AUDIOO FOCUS");
                    audioManager.abandonAudioFocus(audioFocusChangeListener);
                }
            }, 15000);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                setMediaPlayer();

            }
        } catch (IllegalArgumentException e) {
            Log.d("MYAPPBOOTSERVICE",e.getMessage());

            e.printStackTrace();
        } catch (IllegalStateException e) {
            Log.d("MYAPPBOOTSERVICE",e.getMessage());

            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MYAPPBOOTSERVICE","EMPTY");
        return null;
    }
}
