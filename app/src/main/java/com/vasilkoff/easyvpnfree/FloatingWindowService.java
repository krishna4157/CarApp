package com.vasilkoff.easyvpnfree;

import static java.lang.Integer.parseInt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.conscrypt.Conscrypt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import okhttp3.OkHttpClient;

public class FloatingWindowService extends Service {
    // Add this inner class

    public class LocalBinder extends Binder {
        public FloatingWindowService getService() {
            return FloatingWindowService.this;
        }
    }

    private final IBinder binder = new LocalBinder();

    private static final int TOAST_DURATION = 20000; // 20 seconds
    private static final int TOAST_INTERVAL = 3500; // Show toast every 3.5 seconds
    private Handler handler = new Handler();
    private Toast toast;
    private String fact = "";
    private WindowManager windowManager;
    private ImageView floatingImageView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private AudioManager audioManager;
    private ConstraintLayout overlayView1;
    private LinearLayout overlayView;
    private static final int TOUCH_SLOP = 20; // pixels, adjust as needed
    private boolean isDragging = false;
    private int xcor = 1318;
    String filePath;
    private FloatingWindowService.RecognitionCallback callback;

//    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ttt1.wav";
    private TextToSpeech textToSpeech;
    private ApiCallTask apiCallTask;
    // Reference to AsyncTask
    WindowManager.LayoutParams params;
    private static final String API_URL = "https://official-joke-api.appspot.com/jokes/random"; // Replace with your API URL
    //private static final String API_URL = "https://v2.jokeapi.dev/joke/Any?type=single";
    private boolean onTouchDisabled = false;

    public interface RecognitionCallback {
        void onTextSpoken(String result);
    }

    public void setRecognitionCallback(FloatingWindowService.RecognitionCallback callback) {
        this.callback = callback;
    }


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
    private boolean isSpeaking = true;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        filePath = new File(getFilesDir(), "ttt1.wav").getAbsolutePath();

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
                            SimpleDateFormat sdf2 = new SimpleDateFormat("H:a", Locale.US);

                            String formattedTime = sdf.format(calendar.getTime());
                            String formattedTime2 = sdf2.format(calendar.getTime());
                            String[] s = formattedTime2.split(":");
                            String t = "";

//                            if (parseInt(s[0]) < 12) {
//                                t = "Good Morning";
//                            } else if (parseInt(s[0]) > 12 && parseInt(s[0]) < 19 && s[1].equals("PM")) {
//                                t = "Good Evening";
//                            } else if (parseInt(s[0]) == 12 && s[1].equalsIgnoreCase("PM")) {
//                                t = "Good Afternoon";
//                            } else {
//                                t = "Ready for a smooth night ride";
//                            }
//                            String a = t + ", Krishna!";
//                            String b = "Hello Krishna, " + t + "!";
//                            String c = "Welcome, Krishna!, " + t + "!";
//                            String d = "Hey Krishna, " + t + "!";
//
//                            String[] array = new String[]{a, b, c, d};
//                            String rs = array[(int) (System.currentTimeMillis() % array.length)];
//                            //
//                            String a1 = "its currently " + formattedTime + ".";
//                            String b1 = "The time is " + formattedTime + ".";
//                            String c1 = "The clock reads " + formattedTime + ".";
//
//                            String[] array1 = new String[]{a1, b1, c1};
//                            String rs1 = array1[(int) (System.currentTimeMillis() % array1.length)];
//
//                            //
//                            String a2 = "I hope your day has been great so far.";
//                            String b2 = "I trust you've had a wonderful day.";
//                            String c2 = "I hope your day has been enjoyable.";
//                            String d2 = "I hope your day has treated you well.";
//                            String e2 = "I hope you've had a splendid day so far.";
//
//                            String[] array2 = new String[]{a2, b2, c2, d2, e2};
//                            String rs2 = array2[(int) (System.currentTimeMillis() % array2.length)];
//
//                            //
//                            String a3 = "Buckle up and enjoy the journey ahead.";
//                            String b3 = "Fasten your seatbelts and get ready for a fantastic ride!";
//                            String c3 = "Strap in and let's make the most of this journey a joyful memory";
//                            String d3 = "Secure your seatbelts and let's embark on a memorable journey!";
//                            String e3 = "Prepare yourself and enjoy the ride.";
//
//                            String[] array3 = new String[]{a3, b3, c3, d3, e3};
//                            String rs3 = array3[(int) (System.currentTimeMillis() % array3.length)];
//                            fact = rs + rs1 + rs2 + rs3;


                            // Step 1: Decide the greeting based on the current hour and AM/PM
                            String greeting = "";
                            int hour = Integer.parseInt(s[0]);
                            String amPm = s[1];

                            if (hour < 12) {
                                greeting = "Good Morning";
                            } else if (hour == 12 && amPm.equalsIgnoreCase("PM")) {
                                greeting = "Good Afternoon";
                            } else if (hour > 12 && hour < 19 && amPm.equalsIgnoreCase("PM")) {
                                greeting = "Good Evening";
                            } else {
                                greeting = "Ready for a smooth night ride";
                            }

// Step 2: Choose a personalized message variation
                            String[] personalGreetings = new String[]{
                                    greeting + ", Krishna!  How are you feeling today?",
                                    "Hi Krishna! " + greeting + " to you!",
                                    "Hey there Krishna! " + greeting + ". ",
                                    greeting + ", Krishna! Let’s make this ride awesome!",
                                    "Krishna! " + greeting + "! I’m right here with you."
                            };

                            String personalGreeting = personalGreetings[(int) (System.currentTimeMillis() % personalGreetings.length)];

// Step 3: Create a fun time message
                            String[] timeMessages = new String[]{
                                    "It's currently " + formattedTime + ".",
                                    "Just checked — it’s " + formattedTime + " now.",
                                    "Time now is " + formattedTime + ", in case you're wondering.",
                                    "Right now, the clock says " + formattedTime + ".",
                                    "The time is " + formattedTime + ", let’s make it count!"
                            };
                            String timeLine = timeMessages[(int) (System.currentTimeMillis() % timeMessages.length)];

// Step 4: Add a nice, friendly message about the day
                            String[] dayMessages = new String[]{
                                    "Hope your day’s going smooth and easy.",
                                    "Wishing you a peaceful and cheerful day, Krishna.",
                                    "I hope you’re feeling great today!",
                                    "May today bring you good vibes and good tunes",
                                    "Let’s make the most of today, shall we?",
                                    "I hope today’s been kind to you so far."
                            };
                            String dayLine = dayMessages[(int) (System.currentTimeMillis() % dayMessages.length)];

// Step 5: Add an encouraging journey-related message
                            String[] journeyMessages = new String[]{
                                    "Seatbelt on? Let’s roll",
                                    "Let’s enjoy the road together!",
                                    "Here’s to a smooth, safe, and fun drive ahead.",
                                    "The road’s calling. Let’s answer with style.",
                                    "Drive safe, drive smart, and take in the view",
                                    "Let’s make this ride feel like a breeze!"
                            };
                            String journeyLine = journeyMessages[(int) (System.currentTimeMillis() % journeyMessages.length)];

// Final message: Combine all parts into one message
                            fact = personalGreeting + " " + timeLine + " " + dayLine + " " + journeyLine;

                            speakText(fact, 10000, new SpeakTextCallback() {
                                @Override
                                public void onSuccess() {
                                    callback.onTextSpoken("Success");
                                    fact = "";
                                }

                                @Override
                                public void onFailure() {

                                }
                            });
//                            speakText(rs + rs1+ rs2 + rs3, 10000, new SpeakTextCallback() {
//
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onFailure() {
//
//                                }
//                            });                        }
                        }
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
        super.onCreate();


        // Initialize the WindowManager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Create the ImageView for the floating window

        floatingImageView = new ImageView(this);
        isSpeaking = false;
        Glide.with(this)
                .asGif()
                .load(R.drawable.final_girl_smiley)  // Replace with the actual URL or resource ID
                .into(floatingImageView);
        // Set the sample image (replace with your actual image)
//        floatingImageView.setImageResource(R.drawable.ic_launcher_background); // Replace with your image resource

        // Set up layout parameters for the floating window
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                600,
                600,
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT ? WindowManager.LayoutParams.TYPE_SYSTEM_ALERT : Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,  // For API < 23
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Allows interaction with other apps
                PixelFormat.TRANSLUCENT
        );

        layoutParams.gravity = Gravity.TOP | Gravity.START;  // Position of the floating window
        layoutParams.x = 1500;  // Horizontal offset
        layoutParams.y = 200;  // Vertical offset

        // Add the floating image view to the window
        windowManager.addView(floatingImageView, layoutParams);

        //

        // Optional: Set a touch listener to make the window draggable
//        floatingImageView.setOnTouchListener(new View.OnTouchListener() {
//            private int initialX, initialY;
//            private int initial1X, initial1Y;
//
//            private float initialTouchX, initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        int i =1;
////                        xcor = initialX + (int) (event.getRawX() - initialTouchX);
//
//                        if(!onTouchDisabled){
//                            Log.d("LONG PRESS : ", "DOWN");
//                            // Record the initial position when the user starts touching the view
//                            initialX = layoutParams.x;
//                            initialY = layoutParams.y;
//                            if(params != null) {
//                                initial1X = params.x;
//                                initial1Y = params.y;
//                            }
//                            initialTouchX = event.getRawX();
//                            initialTouchY = event.getRawY();
//                            apiCallTask = new ApiCallTask();
//                            String[] s= { "https://official-joke-api.appspot.com/jokes/random",
////                                    "https://geek-jokes.sameerkumar.website/api?format=json",
////                                    "https://v2.jokeapi.dev/joke/Programming",
////                                    "https://icanhazdadjoke.com/j/S7MCtHBQuc",
////                                    "https://v2.jokeapi.dev/joke/Any?type=twopart",
////                                    "https://joke.deno.dev/",
////                                    "https://jokes-api-test.onrender.com/api/jokes/random",
////                                    "https://freeapihub.onrender.com/api/v1/jokes",
////                                    "https://simplejokeapi.azurewebsites.net/joke"
//                            };
//                            String randomString = s[new Random().nextInt(s.length)];
//                            Toast.makeText(getApplicationContext(),randomString,Toast.LENGTH_LONG).show();
//                            AndroidNetworking.get(randomString)
////                            .addPathParameter("pageNumber", "0")
////                            .addQueryParameter("limit", "3")
////                            .addHeaders("token", "1234")
////                            .setTag("test")
//                                    .setPriority(Priority.HIGH)
//                                    .build()
////                                    .getAsJSONArray(new JSONArrayRequestListener() {
////
////                                        @Override
////                                        public void onResponse(JSONArray response) {
////                                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
////                                        }
////
////                                        @Override
////                                        public void onError(ANError anError) {
////                                            Toast.makeText(getApplicationContext(), anError.getErrorDetail(),Toast.LENGTH_LONG).show();
////                                            Log.e("ERROR : ", anError.getErrorDetail());
////                                        }
////                                    });
//                                    .getAsJSONObject(new JSONObjectRequestListener() {
//
//                                        @Override
//                                        public void onResponse(JSONObject response) {
////                                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
//                                                                        onTouchDisabled = true;
//                                            try {
//                                                JSONObject jsonObject = response;
//                                                String setup = jsonObject.getString("setup");
//                                                String punchline = jsonObject.getString("punchline");
////                                        String setup = jsonObject.getString("text");
//
//                                                Log.d("MYAPPBOOTSERVICE : ",setup);
////                                                Log.d("MYAPPBOOTSERVICE : ",result);
//                                                fact = setup;
//                                                speakText("funfact : "+setup, 13000, new SpeakTextCallback() {
//                                                    @Override
//                                                    public void onSuccess() {
//                                                        Log.d("ApiCall", "Setup spoken successfully.");
//                                                        fact = punchline;
//                                                        speakText(punchline, 13000, new SpeakTextCallback() {
//                                                            @Override
//                                                            public void onSuccess() {
//                                                                Log.d("ApiCall", "Punchline spoken successfully.");
//                                                                fact = "";
//
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure() {
//                                                                Log.d("ApiCall", "Failed to speak punchline.");
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure() {
//                                                        Log.d("ApiCall", "Failed to speak setup.");
//                                                    }
//                                                });
//
//
//
//                                            } catch (JSONException e) {
////                    throw new RuntimeException(e);
//                                                speakText("please try again", 10000, new SpeakTextCallback() {
//                                                    @Override
//                                                    public void onSuccess() {
//                                                        Log.d("ApiCall", "Punchline spoken successfully.");
//
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure() {
//                                                        Log.d("ApiCall", "Failed to speak punchline.");
//                                                    }
//                                                });
//                                                showOverlay("ERROR", 5000);
//
//                                                Toast.makeText(getApplicationContext(),"ERROR", Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onError(ANError anError) {
//                                            Toast.makeText(getApplicationContext(), anError.getErrorDetail(),Toast.LENGTH_LONG).show();
//                                            Log.e("ERROR : ", anError.getErrorDetail());
//                                        }
//                                    });
//
////                            apiCallTask.execute(API_URL);
////                            onTouchDisabled = true;
////                            showOverlay("why did the grape man attach a tree to a single window without removing it to the hous holder at the bottom", 1000);
//
//                        } else {
//                            showOverlay("loading funfact", 5000);
////                        Toast.makeText(getApplicationContext(),"Loading.. please wait", Toast.LENGTH_SHORT).show();
//
//                        }
////                        params.x = params.x - (int) (event.getRawX() - initialTouchX);
//
//                        if(windowManager != null && overlayView != null) {
//                            windowManager.updateViewLayout(overlayView, params);
//                        }
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        onTouchDisabled = false;
//                        Log.d("LONG PRESS : ", "MOVE");
//                        apiCallTask.cancel(true);
//                        // Move the floating window as the user drags
//                        layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
//                        params.x = initial1X + (int) (event.getRawX() - initialTouchX);  // X offset from the top-right corner
//
////                        params.x = params.x + (int) (event.getRawX() - initialTouchX);
////                        params.y = params.y + (int) (event.getRawY() - initialTouchY);
//                        windowManager.updateViewLayout(floatingImageView, layoutParams);
//                        if(windowManager != null && overlayView != null) {
////                            windowManager.updateViewLayout(overlayView, params);
//                        }
//
//                        xcor = initialX + (int) (event.getRawX() - initialTouchX);
//                        if(xcor > 1318) {
//                            xcor = 1318;
//                        }
//                        return true;
//                }
//                return false;
//            }
//        });
        floatingImageView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private int initial1X, initial1Y;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDragging = false;
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;
                        if (params != null) {
                            initial1X = params.x;
                            initial1Y = params.y;
                        }
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float deltaX = Math.abs(event.getRawX() - initialTouchX);
                        float deltaY = Math.abs(event.getRawY() - initialTouchY);

                        if (deltaX > TOUCH_SLOP || deltaY > TOUCH_SLOP) {
                            isDragging = true;
                        }

                        if (isDragging) {
                            layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            params.x = initial1X + (int) (event.getRawX() - initialTouchX);
                            windowManager.updateViewLayout(floatingImageView, layoutParams);
                        }

                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isDragging && isInternetAvailable()) {
                            // Considered a click / tap — run your API fetch and joke speak logic
                            try {
                                if(!isSpeaking) {
                                    isSpeaking = true;
//                                    triggerJokeApiCall();
                                    SimpleDateFormat sdf = new SimpleDateFormat("h: mm a", Locale.US);

// 2. Get the current Date
                                    Date now = new Date();

// 3. Format it
                                    String formatted = sdf.format(now);
//                                    callChatGPT("Give me a short, cheerful welcome greeting for Krishna that sounds like a friendly car assistant. share me only the greeting content, without using emojis, include good morning or good evening at the beginning of the greeting as per the current time "+formatted+" in telugu-english text");
                                    callChatGPT("Tell me a funny english joke. share me only the content, without using emojis max 300 characters");

                                }// ✅ put your code in this method
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return true;
                }
                return false;
            }
        });

    }

    private static X509TrustManager defaultTrustManager() throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        throw new IllegalStateException("No X509TrustManager found");
    }


    public void callChatGPT(final String inputText) {
//        new Thread(() -> {
//            try {
//                // API URL
//                URL url = new URL("https://api.openai.com/v1/chat/completions");
//
//                // Open connection
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
//                connection.setRequestProperty("Content-Type", "application/json");
//                // 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
//                connection.setRequestProperty("Authorization", "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");  // ← Replace with your key
//
//                // Prepare request body
//                String requestBody = "{\n" +
//                        "  \"model\": \"gpt-3.5-turbo\",\n" +
//                        "  \"messages\": [\n" +
//                        "    {\"role\": \"user\", \"content\": \"" + inputText + "\"}\n" +
//                        "  ]\n" +
//                        "}";
//
//                // Send request
//                OutputStream os = connection.getOutputStream();
//                os.write(requestBody.getBytes("UTF-8"));
//                os.close();
//
//                // Read response
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder response = new StringBuilder();
//                String line;
//
//                while ((line = br.readLine()) != null) {
//                    response.append(line);
//                }
//
//                br.close();
//
//                // Extract content from JSON response
//                String raw = response.toString();
//                String result = parseResponse(raw);
//
//                // Display in UI (if needed)
//                new Handler(Looper.getMainLooper()).post(() ->
//                        Toast.makeText(getApplicationContext(), "Response: " + result, Toast.LENGTH_LONG).show()
//                );
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
        new Thread(() -> {
            try {
                // 🚀 1. Install Conscrypt as top provider
//                    Security.insertProviderAt(Conscrypt.newProvider(), 1);

                // 🚀 2. Build SSLContext using Conscrypt
//                    X509TrustManager tm = Conscrypt.getDefaultX509TrustManager();
//                    SSLContext sslContext = SSLContext.getInstance("TLS", Conscrypt.newProvider());
//                    sslContext.init(null, new TrustManager[]{ tm }, null);
//                    SSLSocketFactory conscryptFactory = sslContext.getSocketFactory();
//
//                    // 🚀 3. Create OkHttpClient with Conscrypt
//                    OkHttpClient client = new OkHttpClient.Builder()
//                            .sslSocketFactory(conscryptFactory, tm)
//                            .connectionSpecs(Collections.singletonList(ConnectionSpec.RESTRICTED_TLS)) // TLS1.2+ support
//                            .build();

                System.setProperty("java.net.preferIPv6Addresses", "false");
// new code
//                    X509TrustManager tm = Conscrypt.getDefaultX509TrustManager();
//                    SSLContext sslContext = SSLContext.getInstance("TLS", Conscrypt.newProvider());
//                    sslContext.init(null, new TrustManager[]{ tm }, null);
//                    SSLSocketFactory conscryptFactory = sslContext.getSocketFactory();
//                    OkHttpClient client = new OkHttpClient.Builder()
//                            .sslSocketFactory(conscryptFactory, tm)
//                            .build();
//                    AndroidNetworking.initialize(getApplicationContext(), client);
                // new api key
                // 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
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, null, null);
                SSLSocketFactory baseFactory = sc.getSocketFactory();
                X509TrustManager tm = defaultTrustManager();  // your existing method

                // Wrap it to force TLSv1.2
                SSLSocketFactory tls12Factory = new Tls12SocketFactory(baseFactory);

                // Build OkHttpClient
                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(tls12Factory, tm)
                        .build();

                // Use this client for your calls
                AndroidNetworking.initialize(getApplicationContext(), client);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(new TLSSocketFactoryCompat(), defaultTrustManager())
//                .build();
//        AndroidNetworking.initialize(getApplicationContext(), client);
//                    AndroidNetworking.get(randomString)
                // 🚀 4. Use this client for ChatGPT API call via HttpURLConnection or AndroidNetworking
                AndroidNetworking.initialize(getApplicationContext(), client);

                JSONObject body = new JSONObject();
                body.put("model", "tngtech/deepseek-r1t2-chimera:free");
                JSONArray msgs = new JSONArray()
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("content", inputText));
                body.put("messages", msgs);

                AndroidNetworking.post("https://openrouter.ai/api/v1/chat/completions")
                        .addHeaders("Authorization", "Bearer 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")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String reply = response
                                            .getJSONArray("choices")
                                            .getJSONObject(0)
                                            .getJSONObject("message")
                                            .getString("content")
                                            .trim();


                                    Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
                                    isSpeaking = true;
                                    fact = reply;
                                    speakText(reply, reply.split(" ").length * 1500, new SpeakTextCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.d("ApiCall", "Punchline spoken successfully.");
                                            callback.onTextSpoken("Success");
                                            isSpeaking = false;
                                            fact = "";


                                        }

                                        @Override
                                        public void onFailure() {
                                            Log.d("ApiCall", "Failed to speak punchline.");
                                        }
                                    });

                                } catch (JSONException e) {
                                    Log.e("OR_JSON", "Parse error", e);
                                }
                            }
                            @Override
                            public void onError(ANError anError) {
                                Log.e("API_ERROR", "Error detail: "    + anError.getErrorDetail());
                                Log.e("API_ERROR", "Error code: "      + anError.getErrorCode());
                                Log.e("API_ERROR", "Error body: "      + anError.getErrorBody());
                                Log.e("API_ERROR", "Error message: "   + anError.getMessage());
                                if (anError.getCause() != null) {
                                    Log.e("API_ERROR", "Cause: ", anError.getCause());
                                }
                                Toast.makeText(getApplicationContext(),
                                        "Server error: code=" + anError.getErrorCode(),
                                        Toast.LENGTH_LONG
                                ).show();                                }
                        });
//                    Working CHAT GPT but limit exceeded
//                    AndroidNetworking.post("https://api.openai.com/v1/chat/completions")
//                            .addHeaders("Authorization", "Bearer " + "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")
//                            .addJSONObjectBody(new JSONObject() {{
//                                put("model", "gpt-3.5-turbo");
//                                put("messages", new JSONArray().put(new JSONObject()
//                                        .put("role", "user")
//                                        .put("content", inputText)));
//                            }}).build()
//                            .getAsJSONObject(new JSONObjectRequestListener() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    // handle success
//                                    Toast.makeText(getApplicationContext(),"SUCCESS : "+response.toString(), Toast.LENGTH_SHORT).show();
//                                }
//                                @Override
//                                public void onError(ANError anError) {
//                                    // handle error
//                                    Log.e("API_ERROR", "Error detail: "    + anError.getErrorDetail());
//                                    Log.e("API_ERROR", "Error code: "      + anError.getErrorCode());
//                                    Log.e("API_ERROR", "Error body: "      + anError.getErrorBody());
//                                    Log.e("API_ERROR", "Error message: "   + anError.getMessage());
//                                    if (anError.getCause() != null) {
//                                        Log.e("API_ERROR", "Cause: ", anError.getCause());
//                                    }
//                                    Toast.makeText(getApplicationContext(),
//                                            "Server error: code=" + anError.getErrorCode(),
//                                            Toast.LENGTH_LONG
//                                    ).show();
//                                }
//                            });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    private void triggerJokeApiCall() throws Exception {
        apiCallTask = new ApiCallTask();
        String[] s = {
                "https://catfact.ninja/fact", // fact
                "https://official-joke-api.appspot.com/jokes/random", // setup., punchline
                                    "https://geek-jokes.sameerkumar.website/api?format=json", // joke
                                    "https://v2.jokeapi.dev/joke/Any",// joke or setup, delivery
//                                    "https://icanhazdadjoke.com/j/S7MCtHBQuc",
                                    "https://v2.jokeapi.dev/joke/Any?type=twopart", // setup , delivery
//                                    "https://joke.deno.dev/",
//                                    "https://jokes-api-test.onrender.com/api/jokes/random",
//                                    "https://freeapihub.onrender.com/api/v1/jokes",
//                                    "https://simplejokeapi.azurewebsites.net/joke"
        };
        String randomString = s[new Random().nextInt(s.length)];
        Toast.makeText(getApplicationContext(), randomString, Toast.LENGTH_LONG).show();
        System.setProperty("java.net.preferIPv6Addresses", "false");
// new code
        X509TrustManager tm = Conscrypt.getDefaultX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS", Conscrypt.newProvider());
        sslContext.init(null, new TrustManager[]{ tm }, null);
        SSLSocketFactory conscryptFactory = sslContext.getSocketFactory();
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(conscryptFactory, tm)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), client);

//        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(new TLSSocketFactoryCompat(), defaultTrustManager())
//                .build();
//        AndroidNetworking.initialize(getApplicationContext(), client);
        AndroidNetworking.get(randomString)
//                            .addPathParameter("pageNumber", "0")
//                            .addQueryParameter("limit", "3")
//                            .addHeaders("token", "1234")
//                            .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
//                                    .getAsJSONArray(new JSONArrayRequestListener() {
//
//                                        @Override
//                                        public void onResponse(JSONArray response) {
//                                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
//                                        }
//
//                                        @Override
//                                        public void onError(ANError anError) {
//                                            Toast.makeText(getApplicationContext(), anError.getErrorDetail(),Toast.LENGTH_LONG).show();
//                                            Log.e("ERROR : ", anError.getErrorDetail());
//                                        }
//                                    });
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        setupResponseAsPerApi(response, randomString);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_LONG).show();
                        Log.e("ERROR : ", anError.getErrorDetail());

                        Log.e("API_ERROR", "Error detail: " + anError.getErrorDetail());
                        Log.e("API_ERROR", "Error code: " + anError.getErrorCode());
                        Log.e("API_ERROR", "Error body: " + anError.getErrorBody());
                        Log.e("API_ERROR", "Error message: " + anError.getMessage(), anError);
                    }
                });

    }

    private void setupResponseAsPerApi(JSONObject response, String randomString) {

        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
        onTouchDisabled = true;
        String text1 = null;
        String text2 = null;

        try {
            JSONObject jsonObject = response;

            Log.d("MYAPPBOOTSERVICE : ", "JSON RESPONSE : "+jsonObject.toString());

            if (jsonObject.has("setup") && jsonObject.has("punchline")) {
                text1 = jsonObject.optString("setup");
                text2 = jsonObject.optString("punchline");

            } else if (jsonObject.has("setup") && jsonObject.has("delivery")) {
                text1 = jsonObject.optString("setup");
                text2 = jsonObject.optString("delivery");

            } else if (jsonObject.has("fact")) {
                text1 = "CAT FACT : "+jsonObject.optString("fact");

            } else if (jsonObject.has("joke")) {
                text1 = jsonObject.optString("joke");
            }



            Log.d("MYAPPBOOTSERVICE : ", text1);
            fact = text1;
            String finalText = text2;
            speakText("funfact : " + text1, 13000, new SpeakTextCallback() {
                @Override
                public void onSuccess() {
                    Log.d("ApiCall", "Setup spoken successfully.");
                    fact = finalText;
                    if(finalText != null ) {
                        speakText(finalText, finalText.split(" ").length * 1200, new SpeakTextCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d("ApiCall", "Punchline spoken successfully.");
                                isSpeaking = false;
                                fact = "";

                            }

                            @Override
                            public void onFailure() {
                                Log.d("ApiCall", "Failed to speak punchline.");
                            }
                        });
                    } else {
                        isSpeaking = false;
                    }
                }

                @Override
                public void onFailure() {
                    Log.d("ApiCall", "Failed to speak setup.");
                }
            });


        } catch (Exception e) {
//            speakText("please try again", 10000, new SpeakTextCallback() {
//                @Override
//                public void onSuccess() {
//                    Log.d("ApiCall", "Punchline spoken successfully.");
//
//                }
//                @Override
//                public void onFailure() {
//                    Log.d("ApiCall", "Failed to speak punchline.");
//                }
//            });
            showOverlay("ERROR", 5000);
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    private void showOverlay(String message, int duration) {
        removeOverlay();
        // Inflate the overlay layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        overlayView = (LinearLayout) inflater.inflate(R.layout.txt_song_info, null);
        int[] location = new int[2]; // Array to store X and Y coordinates
        floatingImageView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        Toast.makeText(getApplicationContext(), ""+xcor, Toast.LENGTH_SHORT).show();
        // Set the message
        TextView overlayText = overlayView.findViewById(R.id.toast_message);
        overlayText.setText(message);

        // Set up the layout parameters for the overlay
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,  // Use wrap_content to fit content size
                WindowManager.LayoutParams.WRAP_CONTENT,  // Same for height
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,    // Use TYPE_PHONE for Android 4.4.2
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // Set the gravity to top-right corner
        params.gravity = Gravity.END | Gravity.TOP; // Position at the top-right corner
//        params.gravity = Gravity.START | Gravity.TOP; // Position at the top-right corner
//        params.x = 300;  // X offset from the top-right corner

        // Optional: Adjust the x and y offsets if needed
        params.x = 450+ (-xcor + 1300);  // X offset from the top-right corner
        params.y = 25;  // Y offset from the top-right corner

        // Get the WindowManager and add the overlay view
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(overlayView, params);
//        params.x = 0; // X offset
//        params.y = 0; // Y offset

        // Get the WindowManager and add the overlay view
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        windowManager.addView(overlayView, params);
        ConstraintLayout frameLayout = overlayView.findViewById(R.id.custom_toast_container);
//        frameLayout.setX(xcor);
        // Ensure the view is fully attached before starting the animation
        overlayView.post(new Runnable() {
            @Override
            public void run() {
                // Slide-left and zoom-in animations
                ObjectAnimator slideLeft = ObjectAnimator.ofFloat(frameLayout, "translationX", 800f, 0f);
                ObjectAnimator zoomInX = ObjectAnimator.ofFloat(frameLayout, "scaleX", 0f, 1f);  // Scale X from 0.5 to 1
                ObjectAnimator zoomInY = ObjectAnimator.ofFloat(frameLayout, "scaleY", 0f, 1f);  // Scale Y from 0.5 to 1

                // Set a smooth interpolator for sliding and zooming
                slideLeft.setInterpolator(new AccelerateDecelerateInterpolator());
                zoomInX.setInterpolator(new AccelerateDecelerateInterpolator());
                zoomInY.setInterpolator(new AccelerateDecelerateInterpolator());

                // Set durations for the slide and zoom (same duration for both)
                long slideDuration = 500; // Duration for slide and zoom
                slideLeft.setDuration(slideDuration);
                zoomInX.setDuration(slideDuration);
                zoomInY.setDuration(slideDuration);

                // Start slide-left and zoom-in animations simultaneously
                slideLeft.start();
                zoomInX.start();
                zoomInY.start();

                // Once slide and zoom animations complete, apply the bounce
                slideLeft.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Bounce animation after the slide and zoom animations
                        ObjectAnimator bounceX = ObjectAnimator.ofFloat(frameLayout, "translationX", 0f, 0f, 10f, 0f);
                        bounceX.setDuration(250);  // Bounce duration
                        bounceX.setInterpolator(new BounceInterpolator());  // Bounce interpolator for bounce effect
                        bounceX.start();  // Start the bounce animation
                    }
                });

                // Optional: Fade-in effect for smoother appearance (fade-in applied after slide and zoom)
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(frameLayout, "alpha", 0f, 1f);
                fadeIn.setDuration(250);  // Duration for fade-in effect
                fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());  // Smooth fade
                fadeIn.start();  // Start fade-in animation

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeOverlay();
                onTouchDisabled = false;
                isSpeaking = false;
//                        Toast.makeText(getApplicationContext(), Objects.equals(fact, "") ? "STARTED MUSIC NOW : " : fact,Toast.LENGTH_LONG).show();
                Glide.with(getApplicationContext())
                        .asGif()
                        .load(R.drawable.final_girl_smiley)  // Replace with the actual URL or resource ID
                        .into(floatingImageView);

            }
        }, duration); // duration parameter
    }

    private void removeOverlay() {
        if (overlayView != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the floating view when the service is destroyed
        if (floatingImageView != null) {
            windowManager.removeView(floatingImageView);
        }
    }

    //    private void speakText(String text, int delay ) {
//        // Create HashMap to pass parameters to synthesizeToFile
//        textToSpeech.setLanguage(Locale.US);
//        textToSpeech.setSpeechRate(1.0f);
//        // Configure synthesis parameters using HashMap
////        ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance_id");
//        int result = textToSpeech.synthesizeToFile(text, params, filePath);
//
//        if (result == TextToSpeech.SUCCESS) {
//            Log.d("MYAPPBOOTSERVICE",filePath);
//            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    playAudioFile(filePath);
//                }
//            },delay);
//
//        } else {
//            Log.d("MYAPPBOOTSERVICE","FAILED TO SYNTHESIZE AUDIO");        }
//    }
//
    private void setMediaPlayer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.setDataSource(filePath);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Please Wait", Toast.LENGTH_SHORT);
//                    throw new RuntimeException(e);
                }
//                Toast.makeText(getApplicationContext(),"STARTED MUSIC NOW : ",Toast.LENGTH_SHORT).show();
                mediaPlayer.prepareAsync();
                MediaPlayer finalMediaPlayer = mediaPlayer;
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()

                {
                    public void onPrepared (MediaPlayer mp){
                        isSpeaking = true;
//                        Toast.makeText(getApplicationContext(), Objects.equals(fact, "") ? "STARTED MUSIC NOW : " : fact,Toast.LENGTH_LONG).show();
                        Glide.with(getApplicationContext())
                                .asGif()
                                .load(R.drawable.girl_talking)  // Replace with the actual URL or resource ID
                                .into(floatingImageView);
                        if(!Objects.equals(fact, "")){
                            showOverlay(fact, fact.split(" ").length* 900);
                        }

                        finalMediaPlayer.start();
                    }
                });
            }
        }, 3000);
    }

    public boolean isInternetAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    private void playAudioFile(String filePath, SpeakTextCallback callback) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isSpeaking = false;
                Glide.with(getApplicationContext())
                        .asGif()
                        .load(R.drawable.final_girl_smiley)  // Replace with the actual URL or resource ID
                        .into(floatingImageView);
                mediaPlayer.release();
                callback.onSuccess();
                onTouchDisabled = false;
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

    public interface SpeakTextCallback {
        void onSuccess(); // Called after successful text-to-speech
        void onFailure(); // Called when something goes wrong
    }

    // Updated speakText method using callback
    @SuppressLint("StaticFieldLeak")
    public void speakText(String text, int delay, SpeakTextCallback callback) {
        onTouchDisabled = true;
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                String textToSpeak = params[0];
                textToSpeech.setLanguage(Locale.US);
                textToSpeech.setSpeechRate(0.9f);

                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance_id");

                int result = textToSpeech.synthesizeToFile(textToSpeak, paramsMap, filePath);
                return result == TextToSpeech.SUCCESS;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playAudioFile(filePath, callback);
                        }
                    }, delay);
                } else {
                    Log.d("MYAPPBOOTSERVICE", "FAILED TO SYNTHESIZE AUDIO");
                    callback.onFailure(); // Call onFailure when the task fails
                }
            }
        }.execute(text);
    }



    private class ApiCallTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String urlString = params[0];
            String response = "";
            HttpURLConnection urlConnection= null;
//                        String res = "";




            try {
                // If task is cancelled, stop further processing
                if (isCancelled()) {
                    return null; // Return early if the task was cancelled
                }

                // Create URL object
                URL url = new URL(urlString);
                // Open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                // Set request method to GET
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(15000);  // Timeout in milliseconds
                urlConnection.setReadTimeout(15000);     // Timeout in milliseconds

                // Get the response code
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // Successful response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    response = stringBuilder.toString();
                } else {
                    response = "Error: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error: " + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // If the task was cancelled, don't process the result
            if (isCancelled()) {
                return;  // Do nothing if cancelled
            }

            // Update the UI with the result (or error)
            if (result != null) {
                // Log or display the response
//                Toast.makeText(this., "API Response received!", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String setup = jsonObject.getString("setup");
                    String punchline = jsonObject.getString("punchline");
//                                        String setup = jsonObject.getString("text");

                    Log.d("MYAPPBOOTSERVICE : ",setup);
                    Log.d("MYAPPBOOTSERVICE : ",result);
                    fact = setup;
                    speakText("funfact : "+setup, fact.split(" ").length * 800, new SpeakTextCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("ApiCall", "Setup spoken successfully.");
                            fact = punchline;
                            speakText(punchline, 13000, new SpeakTextCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("ApiCall", "Punchline spoken successfully.");
                                    fact = "";
                                    Toast.makeText(getApplicationContext(),"SPOKEN ENTIRE THING SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure() {
                                    Log.d("ApiCall", "Failed to speak punchline.");
                                }
                            });
                        }

                        @Override
                        public void onFailure() {
                            Log.d("ApiCall", "Failed to speak setup.");
                        }
                    });



                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    speakText("please try again", 10000, new SpeakTextCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("ApiCall", "Punchline spoken successfully.");

                        }

                        @Override
                        public void onFailure() {
                            Log.d("ApiCall", "Failed to speak punchline.");
                        }
                    });
                    showOverlay("ERROR", 5000);

                    Toast.makeText(getApplicationContext(),"ERROR", Toast.LENGTH_SHORT).show();
                }

            } else {
                speakText("please try again", 10000, new SpeakTextCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("ApiCall", "Punchline spoken successfully.");
                    }

                    @Override
                    public void onFailure() {
                        Log.d("ApiCall", "Failed to speak punchline.");
                    }
                });
                Toast.makeText(getApplicationContext(),"INVALID", Toast.LENGTH_SHORT).show();
                Log.d("MYAPPBOOTSERVICE : ","ERROR");

                //                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        }

        private void setUpTLS12() throws NoSuchAlgorithmException, KeyManagementException {
            // Create a TrustManager that accepts all certificates
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            // Initialize SSLContext with TLSv1.2
            SSLContext sc = SSLContext.getInstance("TLSv1.1");
            Log.d("MYAPPBOOTSERVICE : ",sc.toString());
            sc.init(null, trustAllCertificates, new java.security.SecureRandom());

            // Set the default SSLSocketFactory and SecureRandom
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true); // Disable hostname verification
        }
    }

    // Method to apply the bouncy animation
    private void startJellyAnimation(View view) {
        // Create a jelly animation effect
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.2f, 1f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500); // Duration for fade-in
        view.startAnimation(fadeIn);
    }


}

