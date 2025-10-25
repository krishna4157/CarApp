package com.vasilkoff.easyvpnfree;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.makeText;

import android.annotation.SuppressLint;


import static java.lang.Integer.parseInt;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
//import com.jiangdg.demo.MainActivity;
//import com.jiangdg.demo.SplashActivity;

import org.conscrypt.Conscrypt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.pocketsphinx.Hypothesis;
import okhttp3.OkHttpClient;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//public class SpeechToTextActivityForVPN extends AppCompatActivity implements VpnStatus.StateListener {
//    private static final int VPN_REQUEST_CODE = 0x0F;
//    private String TAG = VPNActivity.class.getSimpleName();
//    private static final int START_VPN_PROFILE = 70;
//    private static OpenVPNService mVPNService;
//    private boolean isBindedService = false;
//    private boolean filterAds = false;
//    private VpnProfile mVpnProfile;
//    private List<VPNActivity.VPNStatusListener> mVPNStatusListener = new ArrayList<>();
//    private static final String WIFI_SSID = "Kitty1";
//    private static final String WIFI_PASSWORD = "123456789";
//    private static final int WIFI_PERMISSION_REQUEST_CODE = 1001;
//    private HorizontalScrollView scrollView;
//    private TextView scrollingText;
//    private Handler handler;
//    private int scrollPos = 0;
//    private final int SCROLL_SPEED = 50;
//    WifiManager wifiManager;
//    private LocationManager locationManager;
//    private TextView speedTextView;
//    private static final int LOCATION_PERMISSION_REQUEST = 100;
//    boolean frameUpdater = true;
//    private ExoPlayer player;
//    private Intent serviceIntent2;
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent intent = new Intent(this, OpenVPNService.class);
//        intent.setAction(OpenVPNService.START_SERVICE);
//        isBindedService = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case START_VPN_PROFILE:
//                    VPNLaunchHelper.startOpenVpn(mVpnProfile, this);
//                    break;
//            }
//        }
//    }
//
//    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName className,
//                                       IBinder service) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            OpenVPNService.LocalBinder binder = (OpenVPNService.LocalBinder) service;
//            mVPNService = binder.getService();
//            if (mVPNService != null) {
////                mVPNService.setContentIntent(getJumpIntent());
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName arg0) {
//            mVPNService = null;
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unbindService(mConnection);
//    }
//
//    public void addVPNStatusListener(VPNActivity.VPNStatusListener listener) {
//        if (!mVPNStatusListener.contains(listener)) {
//            mVPNStatusListener.add(listener);
//        }
//    }
//
//    public void removeVPNStatusListener(VPNActivity.VPNStatusListener listener) {
//        if (!mVPNStatusListener.contains(listener)) {
//            mVPNStatusListener.remove(listener);
//        }
//    }
//
//    public VpnProfile getVpnProfile() {
//        return mVpnProfile;
//    }
//
//    public boolean isRunning() {
//        return VpnStatus.isVPNActive();
//    }
//
//    public boolean loadVpnProfile(String str) {
//        return loadVpnProfile(str.getBytes());
//    }
//
//    public boolean loadVpnProfile(byte[] data) {
//        ConfigParser cp = new ConfigParser();
//        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(data));
//        try {
//            cp.parseConfig(isr);
//            mVpnProfile = cp.convertProfile();
//            if (filterAds) {
//                mVpnProfile.mOverrideDNS = true;
//                mVpnProfile.mDNS1 = "101.132.183.99";
//                mVpnProfile.mDNS2 = "193.112.15.186";
//            }
//
//            ProfileManager.getInstance(this).addProfile(mVpnProfile);
//            return true;
//        } catch (IOException | ConfigParser.ConfigParseError e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 开始连接
//     */
//    public void connectVpn() {
//        Intent intent = VpnService.prepare(this);
//
//        if (intent != null) {
//            VpnStatus.updateStateString("USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
//                    LEVEL_WAITING_FOR_USER_INPUT);
////            VpnStatus.addStateListener(new VpnStatus.StateListener() {
////                @Override
////                public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
////                    Toast.makeText(getApplicationContext(),"Connected now 1 : ", Toast.LENGTH_LONG).show();
////                }
////            });
//            // Start the query
//            try {
//                startActivityForResult(intent, START_VPN_PROFILE);
//
//            } catch (ActivityNotFoundException ane) {
//                // Shame on you Sony! At least one user reported that
//                // an official Sony Xperia Arc S image triggers this exception
//                VpnStatus.logError("Your image does not support the VPNService API, sorry :(");
//            }
//        } else {
//            onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);
//        }
//    }
//
//    /**
//     * 停止VPN
//     */
//    public void stopVpn() {
//        ProfileManager.setConntectedVpnProfileDisconnected(this);
//        if (mVPNService != null && mVPNService.getManagement() != null)
//            mVPNService.getManagement().stopVPN(false);
//    }
//
//    public void setAccountAndPassword(String account, String password) {
//        if (getVpnProfile() == null)
//            throw new IllegalStateException("You need loadVpnProfile!");
//
//        getVpnProfile().mUsername = account;
//        getVpnProfile().mPassword = password;
//    }
//
//    /**
//     * 是否过滤广告
//     *
//     * @param filter
//     */
//    public void filterAds(boolean filter) {
//        this.filterAds = filter;
//    }
//
////    public abstract Intent getJumpIntent();
//
//    @Override
//    public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
//        BindUtils.bindStatus(state, logmessage, localizedResId, level);
//        // 分发到事件监听
////        for (VPNActivity.VPNStatusListener vpnStatusListener : mVPNStatusListener) {
////            switch (level) {
////                case LEVEL_START:
////                    // 开始连接
////                    vpnStatusListener.onConnectStart();
////                    break;
////                case LEVEL_CONNECTED:
////                    // 已连接
////                    vpnStatusListener.onConnected();
////                    Toast.makeText(getApplicationContext(), "Connected : ", Toast.LENGTH_LONG).show();
////                    break;
////                case LEVEL_VPNPAUSED:
////                    // 暂停
////                    vpnStatusListener.onPaused();
////                    break;
////                case LEVEL_NONETWORK:
////                    // 无网络
////                    vpnStatusListener.onNoNetwork();
////                    break;
////                case LEVEL_CONNECTING_SERVER_REPLIED:
////                    // 服务器答应
//////                    vpnStatusListener.onServerReplied();
////                    Toast.makeText(getApplicationContext(), "Connecting : ", Toast.LENGTH_LONG).show();
////
////                    break;
////                case LEVEL_CONNECTING_NO_SERVER_REPLY_YET:
////                    // 服务器不答应
//////                    vpnStatusListener.onServerNoReplied();
////                    break;
////                case LEVEL_NOTCONNECTED:
////                    // 连接关闭
////                    vpnStatusListener.onConnectClose();
////                    break;
////                case LEVEL_AUTH_FAILED:
////                    // 认证失败
////                    vpnStatusListener.onAuthFailed();
////                    break;
////                case LEVEL_WAITING_FOR_USER_INPUT:
////                    // 等待用户输入
////                    Log.d(TAG, "updateState: " + LEVEL_WAITING_FOR_USER_INPUT);
////                    break;
////                case UNKNOWN_LEVEL:
////                    // 未知错误
////                    vpnStatusListener.onUnknown();
////                    break;
////            }
////        }
//    }
//
//    public void onButtonClick1() {
//
//    }
//
//    public void checkAndRequestGPS() {
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "GPS is disabled. Please enable it.", Toast.LENGTH_LONG).show();
//
//            // Open GPS settings
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "GPS is enabled!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            Toast.makeText(this, "Please enable permission for floating window", Toast.LENGTH_SHORT).show();
//
//            //  if (!Settings.canDrawOverlays(this)) {
//            // If permission is not granted, show a toast and prompt user
//            Toast.makeText(this, "Please enable permission for floating window", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//            // startActivitty(intent);
////            } else {
//            Toast.makeText(this, "Please enable permission for floating window", Toast.LENGTH_SHORT).show();
//            // Permission is already granted, proceed
////            }
//        }
////        System.loadLibrary("crypto"); // Use the name of your library without the 'lib' prefix and '.so' suffix
////        System.loadLibrary("openvpn"); // Use the name of your library without the 'lib' prefix and '.so' suffix
////        System.loadLibrary("jbcrypto"); // Use the name of your library without the 'lib' prefix and '.so' suffix
////        System.loadLibrary("opvpnutil"); // Use the name of your library without the 'lib' prefix and '.so' suffix
//        VpnStatus.addStateListener(this);
//        super.onCreate(savedInstanceState);
//
//
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
////                updateBackgroundWallpaper();
//
//            }
//        }, 5000, 60000);
//
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.speech_text);
//
//        TextView marqueeText = findViewById(R.id.wifiText);
//
//        // Enable marquee effect
//
//
////        GifImageView im = findViewById(R.id.gifImageBackground);
//        Calendar calendar = Calendar.getInstance();
//        String formattedTime = new SimpleDateFormat("H", Locale.US).format(calendar.getTime());
//        String ampm = new SimpleDateFormat("a", Locale.US).format(calendar.getTime());
//        Log.d("HELLO : ", formattedTime);
//        if (ampm.equalsIgnoreCase("am") || parseInt(formattedTime) < 18) {
////            im.setImageResource(R.drawable.m_car);
//            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.m_car;
//            Uri videoUri = Uri.parse(videoPath);
//
//            // Set video URI
//            PlayerView playerView = findViewById(R.id.videoBackground);
//            player = new ExoPlayer.Builder(this).build();
//            playerView.setPlayer(player);
//
//            MediaItem mediaItem = MediaItem.fromUri(videoUri);
//            player.setMediaItem(mediaItem);
//            player.prepare();
//            player.setPlayWhenReady(true);
//            player.setRepeatMode(Player.REPEAT_MODE_ALL);
//            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
//            player.pause();
//        } else {
//            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.n_car;
//            Uri videoUri = Uri.parse(videoPath);
//
//            // Set video URI
//            PlayerView playerView = findViewById(R.id.videoBackground);
//
//            player = new ExoPlayer.Builder(this).build();
//            playerView.setPlayer(player);
//
//            MediaItem mediaItem = MediaItem.fromUri(videoUri);
//            player.setMediaItem(mediaItem);
//            player.prepare();
//            player.setPlayWhenReady(true);
//            player.setRepeatMode(Player.REPEAT_MODE_ALL);
//            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
//            player.pause();
////                        im.setImageResource(R.drawable.n_car);
////            try {
////                GifDrawable gifDrawable = (GifDrawable) im.getDrawable();
////                gifDrawable.start();
////
////                final Handler handler = new Handler();
////                handler.post(new Runnable() {
////                    @Override
////                    public void run() {
////                        int currentFrame = gifDrawable.getCurrentFrameIndex();
////                        gifDrawable.seekToFrame(currentFrame+ 1);  // Skip frames to increase speed
////                        handler.postDelayed(this, 500);  // Lower delay = faster playback
////                    }
////                });
////                gifDrawable.stop();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//        }
//
////        getApplicationContext().startService(serviceIntent2);
//
////        startVpn();
//        String chinaData2 = "###############################################################################\n" +
//                "# OpenVPN 2.0 Sample Configuration File\n" +
//                "# for PacketiX VPN / SoftEther VPN Server\n" +
//                "# \n" +
//                "# !!! AUTO-GENERATED BY SOFTETHER VPN SERVER MANAGEMENT TOOL !!!\n" +
//                "# \n" +
//                "# !!! YOU HAVE TO REVIEW IT BEFORE USE AND MODIFY IT AS NECESSARY !!!\n" +
//                "# \n" +
//                "# This configuration file is auto-generated. You might use this config file\n" +
//                "# in order to connect to the PacketiX VPN / SoftEther VPN Server.\n" +
//                "# However, before you try it, you should review the descriptions of the file\n" +
//                "# to determine the necessity to modify to suitable for your real environment.\n" +
//                "# If necessary, you have to modify a little adequately on the file.\n" +
//                "# For example, the IP address or the hostname as a destination VPN Server\n" +
//                "# should be confirmed.\n" +
//                "# \n" +
//                "# Note that to use OpenVPN 2.0, you have to put the certification file of\n" +
//                "# the destination VPN Server on the OpenVPN Client computer when you use this\n" +
//                "# config file. Please refer the below descriptions carefully.\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# Specify the type of the layer of the VPN connection.\n" +
//                "# \n" +
//                "# To connect to the VPN Server as a \"Remote-Access VPN Client PC\",\n" +
//                "#  specify 'dev tun'. (Layer-3 IP Routing Mode)\n" +
//                "#\n" +
//                "# To connect to the VPN Server as a bridging equipment of \"Site-to-Site VPN\",\n" +
//                "#  specify 'dev tap'. (Layer-2 Ethernet Bridgine Mode)\n" +
//                "\n" +
//                "dev tun\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# Specify the underlying protocol beyond the Internet.\n" +
//                "# Note that this setting must be correspond with the listening setting on\n" +
//                "# the VPN Server.\n" +
//                "# \n" +
//                "# Specify either 'proto tcp' or 'proto udp'.\n" +
//                "\n" +
//                "proto tcp\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The destination hostname / IP address, and port number of\n" +
//                "# the target VPN Server.\n" +
//                "# \n" +
//                "# You have to specify as 'remote <HOSTNAME> <PORT>'. You can also\n" +
//                "# specify the IP address instead of the hostname.\n" +
//                "# \n" +
//                "# Note that the auto-generated below hostname are a \"auto-detected\n" +
//                "# IP address\" of the VPN Server. You have to confirm the correctness\n" +
//                "# beforehand.\n" +
//                "# \n" +
//                "# When you want to connect to the VPN Server by using TCP protocol,\n" +
//                "# the port number of the destination TCP port should be same as one of\n" +
//                "# the available TCP listeners on the VPN Server.\n" +
//                "# \n" +
//                "# When you use UDP protocol, the port number must same as the configuration\n" +
//                "# setting of \"OpenVPN Server Compatible Function\" on the VPN Server.\n" +
//                "\n" +
//                "remote 219.100.37.9 443\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The HTTP/HTTPS proxy setting.\n" +
//                "# \n" +
//                "# Only if you have to use the Internet via a proxy, uncomment the below\n" +
//                "# two lines and specify the proxy address and the port number.\n" +
//                "# In the case of using proxy-authentication, refer the OpenVPN manual.\n" +
//                "\n" +
//                ";http-proxy-retry\n" +
//                ";http-proxy [proxy server] [proxy port]\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The encryption and authentication algorithm.\n" +
//                "# \n" +
//                "# Default setting is good. Modify it as you prefer.\n" +
//                "# When you specify an unsupported algorithm, the error will occur.\n" +
//                "# \n" +
//                "# The supported algorithms are as follows:\n" +
//                "#  cipher: [NULL-CIPHER] NULL AES-128-CBC AES-192-CBC AES-256-CBC BF-CBC\n" +
//                "#          CAST-CBC CAST5-CBC DES-CBC DES-EDE-CBC DES-EDE3-CBC DESX-CBC\n" +
//                "#          RC2-40-CBC RC2-64-CBC RC2-CBC\n" +
//                "#  auth:   SHA SHA1 MD5 MD4 RMD160\n" +
//                "\n" +
//                "cipher AES-128-CBC\n" +
//                "auth SHA1\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# Other parameters necessary to connect to the VPN Server.\n" +
//                "# \n" +
//                "# It is not recommended to modify it unless you have a particular need.\n" +
//                "\n" +
//                "resolv-retry infinite\n" +
//                "nobind\n" +
//                "persist-key\n" +
//                "persist-tun\n" +
//                "client\n" +
//                "verb 3\n" +
//                "#auth-user-pass\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The certificate file of the destination VPN Server.\n" +
//                "# \n" +
//                "# The CA certificate file is embedded in the inline format.\n" +
//                "# You can replace this CA contents if necessary.\n" +
//                "# Please note that if the server certificate is not a self-signed, you have to\n" +
//                "# specify the signer's root certificate (CA) here.\n" +
//                "\n" +
//                "<ca>\n" +
//                "-----BEGIN CERTIFICATE-----\n" +
//                "MIIFazCCA1OgAwIBAgIRAIIQz7DSQONZRGPgu2OCiwAwDQYJKoZIhvcNAQELBQAw\n" +
//                "TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh\n" +
//                "cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMTUwNjA0MTEwNDM4\n" +
//                "WhcNMzUwNjA0MTEwNDM4WjBPMQswCQYDVQQGEwJVUzEpMCcGA1UEChMgSW50ZXJu\n" +
//                "ZXQgU2VjdXJpdHkgUmVzZWFyY2ggR3JvdXAxFTATBgNVBAMTDElTUkcgUm9vdCBY\n" +
//                "MTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAK3oJHP0FDfzm54rVygc\n" +
//                "h77ct984kIxuPOZXoHj3dcKi/vVqbvYATyjb3miGbESTtrFj/RQSa78f0uoxmyF+\n" +
//                "0TM8ukj13Xnfs7j/EvEhmkvBioZxaUpmZmyPfjxwv60pIgbz5MDmgK7iS4+3mX6U\n" +
//                "A5/TR5d8mUgjU+g4rk8Kb4Mu0UlXjIB0ttov0DiNewNwIRt18jA8+o+u3dpjq+sW\n" +
//                "T8KOEUt+zwvo/7V3LvSye0rgTBIlDHCNAymg4VMk7BPZ7hm/ELNKjD+Jo2FR3qyH\n" +
//                "B5T0Y3HsLuJvW5iB4YlcNHlsdu87kGJ55tukmi8mxdAQ4Q7e2RCOFvu396j3x+UC\n" +
//                "B5iPNgiV5+I3lg02dZ77DnKxHZu8A/lJBdiB3QW0KtZB6awBdpUKD9jf1b0SHzUv\n" +
//                "KBds0pjBqAlkd25HN7rOrFleaJ1/ctaJxQZBKT5ZPt0m9STJEadao0xAH0ahmbWn\n" +
//                "OlFuhjuefXKnEgV4We0+UXgVCwOPjdAvBbI+e0ocS3MFEvzG6uBQE3xDk3SzynTn\n" +
//                "jh8BCNAw1FtxNrQHusEwMFxIt4I7mKZ9YIqioymCzLq9gwQbooMDQaHWBfEbwrbw\n" +
//                "qHyGO0aoSCqI3Haadr8faqU9GY/rOPNk3sgrDQoo//fb4hVC1CLQJ13hef4Y53CI\n" +
//                "rU7m2Ys6xt0nUW7/vGT1M0NPAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAPBgNV\n" +
//                "HRMBAf8EBTADAQH/MB0GA1UdDgQWBBR5tFnme7bl5AFzgAiIyBpY9umbbjANBgkq\n" +
//                "hkiG9w0BAQsFAAOCAgEAVR9YqbyyqFDQDLHYGmkgJykIrGF1XIpu+ILlaS/V9lZL\n" +
//                "ubhzEFnTIZd+50xx+7LSYK05qAvqFyFWhfFQDlnrzuBZ6brJFe+GnY+EgPbk6ZGQ\n" +
//                "3BebYhtF8GaV0nxvwuo77x/Py9auJ/GpsMiu/X1+mvoiBOv/2X/qkSsisRcOj/KK\n" +
//                "NFtY2PwByVS5uCbMiogziUwthDyC3+6WVwW6LLv3xLfHTjuCvjHIInNzktHCgKQ5\n" +
//                "ORAzI4JMPJ+GslWYHb4phowim57iaztXOoJwTdwJx4nLCgdNbOhdjsnvzqvHu7Ur\n" +
//                "TkXWStAmzOVyyghqpZXjFaH3pO3JLF+l+/+sKAIuvtd7u+Nxe5AW0wdeRlN8NwdC\n" +
//                "jNPElpzVmbUq4JUagEiuTDkHzsxHpFKVK7q4+63SM1N95R1NbdWhscdCb+ZAJzVc\n" +
//                "oyi3B43njTOQ5yOf+1CceWxG1bQVs5ZufpsMljq4Ui0/1lvh+wjChP4kqKOJ2qxq\n" +
//                "4RgqsahDYVvTH9w7jXbyLeiNdd8XM2w9U/t7y0Ff/9yi0GE44Za4rF2LN9d11TPA\n" +
//                "mRGunUHBcnWEvgJBQl9nJEiU0Zsnvgc/ubhPgXRR4Xq37Z0j4r7g1SgEEzwxA57d\n" +
//                "emyPxgcYxn/eR44/KJ4EBs+lVDR3veyJm+kXQ99b21/+jh5Xos1AnX5iItreGCc=\n" +
//                "-----END CERTIFICATE-----\n" +
//                "\n" +
//                "</ca>\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The client certificate file (dummy).\n" +
//                "# \n" +
//                "# In some implementations of OpenVPN Client software\n" +
//                "# (for example: OpenVPN Client for iOS),\n" +
//                "# a pair of client certificate and private key must be included on the\n" +
//                "# configuration file due to the limitation of the client.\n" +
//                "# So this sample configuration file has a dummy pair of client certificate\n" +
//                "# and private key as follows.\n" +
//                "\n" +
//                "<cert>\n" +
//                "-----BEGIN CERTIFICATE-----\n" +
//                "MIICxjCCAa4CAQAwDQYJKoZIhvcNAQEFBQAwKTEaMBgGA1UEAxMRVlBOR2F0ZUNs\n" +
//                "aWVudENlcnQxCzAJBgNVBAYTAkpQMB4XDTEzMDIxMTAzNDk0OVoXDTM3MDExOTAz\n" +
//                "MTQwN1owKTEaMBgGA1UEAxMRVlBOR2F0ZUNsaWVudENlcnQxCzAJBgNVBAYTAkpQ\n" +
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5h2lgQQYUjwoKYJbzVZA\n" +
//                "5VcIGd5otPc/qZRMt0KItCFA0s9RwReNVa9fDRFLRBhcITOlv3FBcW3E8h1Us7RD\n" +
//                "4W8GmJe8zapJnLsD39OSMRCzZJnczW4OCH1PZRZWKqDtjlNca9AF8a65jTmlDxCQ\n" +
//                "CjntLIWk5OLLVkFt9/tScc1GDtci55ofhaNAYMPiH7V8+1g66pGHXAoWK6AQVH67\n" +
//                "XCKJnGB5nlQ+HsMYPV/O49Ld91ZN/2tHkcaLLyNtywxVPRSsRh480jju0fcCsv6h\n" +
//                "p/0yXnTB//mWutBGpdUlIbwiITbAmrsbYnjigRvnPqX1RNJUbi9Fp6C2c/HIFJGD\n" +
//                "ywIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQChO5hgcw/4oWfoEFLu9kBa1B//kxH8\n" +
//                "hQkChVNn8BRC7Y0URQitPl3DKEed9URBDdg2KOAz77bb6ENPiliD+a38UJHIRMqe\n" +
//                "UBHhllOHIzvDhHFbaovALBQceeBzdkQxsKQESKmQmR832950UCovoyRB61UyAV7h\n" +
//                "+mZhYPGRKXKSJI6s0Egg/Cri+Cwk4bjJfrb5hVse11yh4D9MHhwSfCOH+0z4hPUT\n" +
//                "Fku7dGavURO5SVxMn/sL6En5D+oSeXkadHpDs+Airym2YHh15h0+jPSOoR6yiVp/\n" +
//                "6zZeZkrN43kuS73KpKDFjfFPh8t4r1gOIjttkNcQqBccusnplQ7HJpsk\n" +
//                "-----END CERTIFICATE-----\n" +
//                "\n" +
//                "</cert>\n" +
//                "\n" +
//                "<key>\n" +
//                "-----BEGIN RSA PRIVATE KEY-----\n" +
//                "MIIEpAIBAAKCAQEA5h2lgQQYUjwoKYJbzVZA5VcIGd5otPc/qZRMt0KItCFA0s9R\n" +
//                "wReNVa9fDRFLRBhcITOlv3FBcW3E8h1Us7RD4W8GmJe8zapJnLsD39OSMRCzZJnc\n" +
//                "zW4OCH1PZRZWKqDtjlNca9AF8a65jTmlDxCQCjntLIWk5OLLVkFt9/tScc1GDtci\n" +
//                "55ofhaNAYMPiH7V8+1g66pGHXAoWK6AQVH67XCKJnGB5nlQ+HsMYPV/O49Ld91ZN\n" +
//                "/2tHkcaLLyNtywxVPRSsRh480jju0fcCsv6hp/0yXnTB//mWutBGpdUlIbwiITbA\n" +
//                "mrsbYnjigRvnPqX1RNJUbi9Fp6C2c/HIFJGDywIDAQABAoIBAERV7X5AvxA8uRiK\n" +
//                "k8SIpsD0dX1pJOMIwakUVyvc4EfN0DhKRNb4rYoSiEGTLyzLpyBc/A28Dlkm5eOY\n" +
//                "fjzXfYkGtYi/Ftxkg3O9vcrMQ4+6i+uGHaIL2rL+s4MrfO8v1xv6+Wky33EEGCou\n" +
//                "QiwVGRFQXnRoQ62NBCFbUNLhmXwdj1akZzLU4p5R4zA3QhdxwEIatVLt0+7owLQ3\n" +
//                "lP8sfXhppPOXjTqMD4QkYwzPAa8/zF7acn4kryrUP7Q6PAfd0zEVqNy9ZCZ9ffho\n" +
//                "zXedFj486IFoc5gnTp2N6jsnVj4LCGIhlVHlYGozKKFqJcQVGsHCqq1oz2zjW6LS\n" +
//                "oRYIHgECgYEA8zZrkCwNYSXJuODJ3m/hOLVxcxgJuwXoiErWd0E42vPanjjVMhnt\n" +
//                "KY5l8qGMJ6FhK9LYx2qCrf/E0XtUAZ2wVq3ORTyGnsMWre9tLYs55X+ZN10Tc75z\n" +
//                "4hacbU0hqKN1HiDmsMRY3/2NaZHoy7MKnwJJBaG48l9CCTlVwMHocIECgYEA8jby\n" +
//                "dGjxTH+6XHWNizb5SRbZxAnyEeJeRwTMh0gGzwGPpH/sZYGzyu0SySXWCnZh3Rgq\n" +
//                "5uLlNxtrXrljZlyi2nQdQgsq2YrWUs0+zgU+22uQsZpSAftmhVrtvet6MjVjbByY\n" +
//                "DADciEVUdJYIXk+qnFUJyeroLIkTj7WYKZ6RjksCgYBoCFIwRDeg42oK89RFmnOr\n" +
//                "LymNAq4+2oMhsWlVb4ejWIWeAk9nc+GXUfrXszRhS01mUnU5r5ygUvRcarV/T3U7\n" +
//                "TnMZ+I7Y4DgWRIDd51znhxIBtYV5j/C/t85HjqOkH+8b6RTkbchaX3mau7fpUfds\n" +
//                "Fq0nhIq42fhEO8srfYYwgQKBgQCyhi1N/8taRwpk+3/IDEzQwjbfdzUkWWSDk9Xs\n" +
//                "H/pkuRHWfTMP3flWqEYgW/LW40peW2HDq5imdV8+AgZxe/XMbaji9Lgwf1RY005n\n" +
//                "KxaZQz7yqHupWlLGF68DPHxkZVVSagDnV/sztWX6SFsCqFVnxIXifXGC4cW5Nm9g\n" +
//                "va8q4QKBgQCEhLVeUfdwKvkZ94g/GFz731Z2hrdVhgMZaU/u6t0V95+YezPNCQZB\n" +
//                "wmE9Mmlbq1emDeROivjCfoGhR3kZXW1pTKlLh6ZMUQUOpptdXva8XxfoqQwa3enA\n" +
//                "M7muBbF0XN7VO80iJPv+PmIZdEIAkpwKfi201YB+BafCIuGxIF50Vg==\n" +
//                "-----END RSA PRIVATE KEY-----\n" +
//                "\n" +
//                "</key>\n" +
//                "\n";
//
//        String usaData = "###############################################################################\n" +
//                "# OpenVPN 2.0 Sample Configuration File\n" +
//                "# for PacketiX VPN / SoftEther VPN Server\n" +
//                "# \n" +
//                "# !!! AUTO-GENERATED BY SOFTETHER VPN SERVER MANAGEMENT TOOL !!!\n" +
//                "# \n" +
//                "# !!! YOU HAVE TO REVIEW IT BEFORE USE AND MODIFY IT AS NECESSARY !!!\n" +
//                "# \n" +
//                "# This configuration file is auto-generated. You might use this config file\n" +
//                "# in order to connect to the PacketiX VPN / SoftEther VPN Server.\n" +
//                "# However, before you try it, you should review the descriptions of the file\n" +
//                "# to determine the necessity to modify to suitable for your real environment.\n" +
//                "# If necessary, you have to modify a little adequately on the file.\n" +
//                "# For example, the IP address or the hostname as a destination VPN Server\n" +
//                "# should be confirmed.\n" +
//                "# \n" +
//                "# Note that to use OpenVPN 2.0, you have to put the certification file of\n" +
//                "# the destination VPN Server on the OpenVPN Client computer when you use this\n" +
//                "# config file. Please refer the below descriptions carefully.\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# Specify the type of the layer of the VPN connection.\n" +
//                "# \n" +
//                "# To connect to the VPN Server as a \"Remote-Access VPN Client PC\",\n" +
//                "#  specify 'dev tun'. (Layer-3 IP Routing Mode)\n" +
//                "#\n" +
//                "# To connect to the VPN Server as a bridging equipment of \"Site-to-Site VPN\",\n" +
//                "#  specify 'dev tap'. (Layer-2 Ethernet Bridgine Mode)\n" +
//                "\n" +
//                "dev tun\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# Specify the underlying protocol beyond the Internet.\n" +
//                "# Note that this setting must be correspond with the listening setting on\n" +
//                "# the VPN Server.\n" +
//                "# \n" +
//                "# Specify either 'proto tcp' or 'proto udp'.\n" +
//                "\n" +
//                "proto tcp\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The destination hostname / IP address, and port number of\n" +
//                "# the target VPN Server.\n" +
//                "# \n" +
//                "# You have to specify as 'remote <HOSTNAME> <PORT>'. You can also\n" +
//                "# specify the IP address instead of the hostname.\n" +
//                "# \n" +
//                "# Note that the auto-generated below hostname are a \"auto-detected\n" +
//                "# IP address\" of the VPN Server. You have to confirm the correctness\n" +
//                "# beforehand.\n" +
//                "# \n" +
//                "# When you want to connect to the VPN Server by using TCP protocol,\n" +
//                "# the port number of the destination TCP port should be same as one of\n" +
//                "# the available TCP listeners on the VPN Server.\n" +
//                "# \n" +
//                "# When you use UDP protocol, the port number must same as the configuration\n" +
//                "# setting of \"OpenVPN Server Compatible Function\" on the VPN Server.\n" +
//                "\n" +
//                "remote 209.177.112.240 1816\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The HTTP/HTTPS proxy setting.\n" +
//                "# \n" +
//                "# Only if you have to use the Internet via a proxy, uncomment the below\n" +
//                "# two lines and specify the proxy address and the port number.\n" +
//                "# In the case of using proxy-authentication, refer the OpenVPN manual.\n" +
//                "\n" +
//                ";http-proxy-retry\n" +
//                ";http-proxy [proxy server] [proxy port]\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The encryption and authentication algorithm.\n" +
//                "# \n" +
//                "# Default setting is good. Modify it as you prefer.\n" +
//                "# When you specify an unsupported algorithm, the error will occur.\n" +
//                "# \n" +
//                "# The supported algorithms are as follows:\n" +
//                "#  cipher: [NULL-CIPHER] NULL AES-128-CBC AES-192-CBC AES-256-CBC BF-CBC\n" +
//                "#          CAST-CBC CAST5-CBC DES-CBC DES-EDE-CBC DES-EDE3-CBC DESX-CBC\n" +
//                "#          RC2-40-CBC RC2-64-CBC RC2-CBC\n" +
//                "#  auth:   SHA SHA1 MD5 MD4 RMD160\n" +
//                "\n" +
//                "cipher AES-128-CBC\n" +
//                "auth SHA1\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# Other parameters necessary to connect to the VPN Server.\n" +
//                "# \n" +
//                "# It is not recommended to modify it unless you have a particular need.\n" +
//                "\n" +
//                "resolv-retry infinite\n" +
//                "nobind\n" +
//                "persist-key\n" +
//                "persist-tun\n" +
//                "client\n" +
//                "verb 3\n" +
//                "#auth-user-pass\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The certificate file of the destination VPN Server.\n" +
//                "# \n" +
//                "# The CA certificate file is embedded in the inline format.\n" +
//                "# You can replace this CA contents if necessary.\n" +
//                "# Please note that if the server certificate is not a self-signed, you have to\n" +
//                "# specify the signer's root certificate (CA) here.\n" +
//                "\n" +
//                "<ca>\n" +
//                "-----BEGIN CERTIFICATE-----\n" +
//                "MIIFazCCA1OgAwIBAgIRAIIQz7DSQONZRGPgu2OCiwAwDQYJKoZIhvcNAQELBQAw\n" +
//                "TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh\n" +
//                "cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMTUwNjA0MTEwNDM4\n" +
//                "WhcNMzUwNjA0MTEwNDM4WjBPMQswCQYDVQQGEwJVUzEpMCcGA1UEChMgSW50ZXJu\n" +
//                "ZXQgU2VjdXJpdHkgUmVzZWFyY2ggR3JvdXAxFTATBgNVBAMTDElTUkcgUm9vdCBY\n" +
//                "MTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAK3oJHP0FDfzm54rVygc\n" +
//                "h77ct984kIxuPOZXoHj3dcKi/vVqbvYATyjb3miGbESTtrFj/RQSa78f0uoxmyF+\n" +
//                "0TM8ukj13Xnfs7j/EvEhmkvBioZxaUpmZmyPfjxwv60pIgbz5MDmgK7iS4+3mX6U\n" +
//                "A5/TR5d8mUgjU+g4rk8Kb4Mu0UlXjIB0ttov0DiNewNwIRt18jA8+o+u3dpjq+sW\n" +
//                "T8KOEUt+zwvo/7V3LvSye0rgTBIlDHCNAymg4VMk7BPZ7hm/ELNKjD+Jo2FR3qyH\n" +
//                "B5T0Y3HsLuJvW5iB4YlcNHlsdu87kGJ55tukmi8mxdAQ4Q7e2RCOFvu396j3x+UC\n" +
//                "B5iPNgiV5+I3lg02dZ77DnKxHZu8A/lJBdiB3QW0KtZB6awBdpUKD9jf1b0SHzUv\n" +
//                "KBds0pjBqAlkd25HN7rOrFleaJ1/ctaJxQZBKT5ZPt0m9STJEadao0xAH0ahmbWn\n" +
//                "OlFuhjuefXKnEgV4We0+UXgVCwOPjdAvBbI+e0ocS3MFEvzG6uBQE3xDk3SzynTn\n" +
//                "jh8BCNAw1FtxNrQHusEwMFxIt4I7mKZ9YIqioymCzLq9gwQbooMDQaHWBfEbwrbw\n" +
//                "qHyGO0aoSCqI3Haadr8faqU9GY/rOPNk3sgrDQoo//fb4hVC1CLQJ13hef4Y53CI\n" +
//                "rU7m2Ys6xt0nUW7/vGT1M0NPAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAPBgNV\n" +
//                "HRMBAf8EBTADAQH/MB0GA1UdDgQWBBR5tFnme7bl5AFzgAiIyBpY9umbbjANBgkq\n" +
//                "hkiG9w0BAQsFAAOCAgEAVR9YqbyyqFDQDLHYGmkgJykIrGF1XIpu+ILlaS/V9lZL\n" +
//                "ubhzEFnTIZd+50xx+7LSYK05qAvqFyFWhfFQDlnrzuBZ6brJFe+GnY+EgPbk6ZGQ\n" +
//                "3BebYhtF8GaV0nxvwuo77x/Py9auJ/GpsMiu/X1+mvoiBOv/2X/qkSsisRcOj/KK\n" +
//                "NFtY2PwByVS5uCbMiogziUwthDyC3+6WVwW6LLv3xLfHTjuCvjHIInNzktHCgKQ5\n" +
//                "ORAzI4JMPJ+GslWYHb4phowim57iaztXOoJwTdwJx4nLCgdNbOhdjsnvzqvHu7Ur\n" +
//                "TkXWStAmzOVyyghqpZXjFaH3pO3JLF+l+/+sKAIuvtd7u+Nxe5AW0wdeRlN8NwdC\n" +
//                "jNPElpzVmbUq4JUagEiuTDkHzsxHpFKVK7q4+63SM1N95R1NbdWhscdCb+ZAJzVc\n" +
//                "oyi3B43njTOQ5yOf+1CceWxG1bQVs5ZufpsMljq4Ui0/1lvh+wjChP4kqKOJ2qxq\n" +
//                "4RgqsahDYVvTH9w7jXbyLeiNdd8XM2w9U/t7y0Ff/9yi0GE44Za4rF2LN9d11TPA\n" +
//                "mRGunUHBcnWEvgJBQl9nJEiU0Zsnvgc/ubhPgXRR4Xq37Z0j4r7g1SgEEzwxA57d\n" +
//                "emyPxgcYxn/eR44/KJ4EBs+lVDR3veyJm+kXQ99b21/+jh5Xos1AnX5iItreGCc=\n" +
//                "-----END CERTIFICATE-----\n" +
//                "\n" +
//                "</ca>\n" +
//                "\n" +
//                "\n" +
//                "###############################################################################\n" +
//                "# The client certificate file (dummy).\n" +
//                "# \n" +
//                "# In some implementations of OpenVPN Client software\n" +
//                "# (for example: OpenVPN Client for iOS),\n" +
//                "# a pair of client certificate and private key must be included on the\n" +
//                "# configuration file due to the limitation of the client.\n" +
//                "# So this sample configuration file has a dummy pair of client certificate\n" +
//                "# and private key as follows.\n" +
//                "\n" +
//                "<cert>\n" +
//                "-----BEGIN CERTIFICATE-----\n" +
//                "MIICxjCCAa4CAQAwDQYJKoZIhvcNAQEFBQAwKTEaMBgGA1UEAxMRVlBOR2F0ZUNs\n" +
//                "aWVudENlcnQxCzAJBgNVBAYTAkpQMB4XDTEzMDIxMTAzNDk0OVoXDTM3MDExOTAz\n" +
//                "MTQwN1owKTEaMBgGA1UEAxMRVlBOR2F0ZUNsaWVudENlcnQxCzAJBgNVBAYTAkpQ\n" +
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5h2lgQQYUjwoKYJbzVZA\n" +
//                "5VcIGd5otPc/qZRMt0KItCFA0s9RwReNVa9fDRFLRBhcITOlv3FBcW3E8h1Us7RD\n" +
//                "4W8GmJe8zapJnLsD39OSMRCzZJnczW4OCH1PZRZWKqDtjlNca9AF8a65jTmlDxCQ\n" +
//                "CjntLIWk5OLLVkFt9/tScc1GDtci55ofhaNAYMPiH7V8+1g66pGHXAoWK6AQVH67\n" +
//                "XCKJnGB5nlQ+HsMYPV/O49Ld91ZN/2tHkcaLLyNtywxVPRSsRh480jju0fcCsv6h\n" +
//                "p/0yXnTB//mWutBGpdUlIbwiITbAmrsbYnjigRvnPqX1RNJUbi9Fp6C2c/HIFJGD\n" +
//                "ywIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQChO5hgcw/4oWfoEFLu9kBa1B//kxH8\n" +
//                "hQkChVNn8BRC7Y0URQitPl3DKEed9URBDdg2KOAz77bb6ENPiliD+a38UJHIRMqe\n" +
//                "UBHhllOHIzvDhHFbaovALBQceeBzdkQxsKQESKmQmR832950UCovoyRB61UyAV7h\n" +
//                "+mZhYPGRKXKSJI6s0Egg/Cri+Cwk4bjJfrb5hVse11yh4D9MHhwSfCOH+0z4hPUT\n" +
//                "Fku7dGavURO5SVxMn/sL6En5D+oSeXkadHpDs+Airym2YHh15h0+jPSOoR6yiVp/\n" +
//                "6zZeZkrN43kuS73KpKDFjfFPh8t4r1gOIjttkNcQqBccusnplQ7HJpsk\n" +
//                "-----END CERTIFICATE-----\n" +
//                "\n" +
//                "</cert>\n" +
//                "\n" +
//                "<key>\n" +
//                "-----BEGIN RSA PRIVATE KEY-----\n" +
//                "MIIEpAIBAAKCAQEA5h2lgQQYUjwoKYJbzVZA5VcIGd5otPc/qZRMt0KItCFA0s9R\n" +
//                "wReNVa9fDRFLRBhcITOlv3FBcW3E8h1Us7RD4W8GmJe8zapJnLsD39OSMRCzZJnc\n" +
//                "zW4OCH1PZRZWKqDtjlNca9AF8a65jTmlDxCQCjntLIWk5OLLVkFt9/tScc1GDtci\n" +
//                "55ofhaNAYMPiH7V8+1g66pGHXAoWK6AQVH67XCKJnGB5nlQ+HsMYPV/O49Ld91ZN\n" +
//                "/2tHkcaLLyNtywxVPRSsRh480jju0fcCsv6hp/0yXnTB//mWutBGpdUlIbwiITbA\n" +
//                "mrsbYnjigRvnPqX1RNJUbi9Fp6C2c/HIFJGDywIDAQABAoIBAERV7X5AvxA8uRiK\n" +
//                "k8SIpsD0dX1pJOMIwakUVyvc4EfN0DhKRNb4rYoSiEGTLyzLpyBc/A28Dlkm5eOY\n" +
//                "fjzXfYkGtYi/Ftxkg3O9vcrMQ4+6i+uGHaIL2rL+s4MrfO8v1xv6+Wky33EEGCou\n" +
//                "QiwVGRFQXnRoQ62NBCFbUNLhmXwdj1akZzLU4p5R4zA3QhdxwEIatVLt0+7owLQ3\n" +
//                "lP8sfXhppPOXjTqMD4QkYwzPAa8/zF7acn4kryrUP7Q6PAfd0zEVqNy9ZCZ9ffho\n" +
//                "zXedFj486IFoc5gnTp2N6jsnVj4LCGIhlVHlYGozKKFqJcQVGsHCqq1oz2zjW6LS\n" +
//                "oRYIHgECgYEA8zZrkCwNYSXJuODJ3m/hOLVxcxgJuwXoiErWd0E42vPanjjVMhnt\n" +
//                "KY5l8qGMJ6FhK9LYx2qCrf/E0XtUAZ2wVq3ORTyGnsMWre9tLYs55X+ZN10Tc75z\n" +
//                "4hacbU0hqKN1HiDmsMRY3/2NaZHoy7MKnwJJBaG48l9CCTlVwMHocIECgYEA8jby\n" +
//                "dGjxTH+6XHWNizb5SRbZxAnyEeJeRwTMh0gGzwGPpH/sZYGzyu0SySXWCnZh3Rgq\n" +
//                "5uLlNxtrXrljZlyi2nQdQgsq2YrWUs0+zgU+22uQsZpSAftmhVrtvet6MjVjbByY\n" +
//                "DADciEVUdJYIXk+qnFUJyeroLIkTj7WYKZ6RjksCgYBoCFIwRDeg42oK89RFmnOr\n" +
//                "LymNAq4+2oMhsWlVb4ejWIWeAk9nc+GXUfrXszRhS01mUnU5r5ygUvRcarV/T3U7\n" +
//                "TnMZ+I7Y4DgWRIDd51znhxIBtYV5j/C/t85HjqOkH+8b6RTkbchaX3mau7fpUfds\n" +
//                "Fq0nhIq42fhEO8srfYYwgQKBgQCyhi1N/8taRwpk+3/IDEzQwjbfdzUkWWSDk9Xs\n" +
//                "H/pkuRHWfTMP3flWqEYgW/LW40peW2HDq5imdV8+AgZxe/XMbaji9Lgwf1RY005n\n" +
//                "KxaZQz7yqHupWlLGF68DPHxkZVVSagDnV/sztWX6SFsCqFVnxIXifXGC4cW5Nm9g\n" +
//                "va8q4QKBgQCEhLVeUfdwKvkZ94g/GFz731Z2hrdVhgMZaU/u6t0V95+YezPNCQZB\n" +
//                "wmE9Mmlbq1emDeROivjCfoGhR3kZXW1pTKlLh6ZMUQUOpptdXva8XxfoqQwa3enA\n" +
//                "M7muBbF0XN7VO80iJPv+PmIZdEIAkpwKfi201YB+BafCIuGxIF50Vg==\n" +
//                "-----END RSA PRIVATE KEY-----\n" +
//                "\n" +
//                "</key>\n" +
//                "\n";
//        loadVpnProfile(usaData);
//
//        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), "CLICK", Toast.LENGTH_LONG).show();
//
//                getVpnProfile().mName = "vpn2";
//
//                //                getVpnProfile().mServerName = "us178.vpnbook.com";
////                getVpnProfile().mIPv4Address = "us178.vpnbook.com";
////                getVpnProfile().m
//                setAccountAndPassword("vpn", "vpn");
////                Toast.makeText(getApplicationContext(),"jiji",Toast.LENGTH_LONG).show();
//                //                setAccountAndPassword("vpnbook", "hu86c9k");
////                connectVpn();
//
////                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scroll_text);
////                        marqueeText.startAnimation(animation);
//                // Enable marquee effect
//                scrollView = findViewById(R.id.scrollView);
//                scrollingText = findViewById(R.id.wifiText);
//
//                handler = new Handler();
//                startTextScroll();
//
//            }
//
//        }, 20000);
//
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        ImageView floatingImageView = findViewById(R.id.ImageBackgroundView);
//
////        connectToWifi("Kitty", "123456789");
//        Glide.with(getApplicationContext()).load(R.drawable.dmin7)
//                .override(800, 600)  // Resize to avoid memory issues
//                        .into(floatingImageView);
//
//        ImageView imageView = findViewById(R.id.wifiImageButton);
//        imageView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                onButtonClick(view);
//            }
//        });
//        ImageView imageView1 = findViewById(R.id.cameraButton);
//        imageView1.setOnClickListener(this::onButtonClick1);
////        GifDrawable gifDrawable = (GifDrawable) im.getDrawable();
////        gifDrawable.stop();
//// Remove tint by clearing color filter
////        startSpeedTracking();
//        checkAndRequestGPS();
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        LocationListener locationListener = new LocationListener() {
//            private Handler handler = new Handler();
//            private Runnable frameUpdateRunnable;
//            private boolean isUpdatingFrames = false; // To prevent multiple handlers
//
//            @Override
//            public void onLocationChanged(Location location) {
//                if (location.hasSpeed()) {
//                    float speed = location.getSpeed(); // Speed in m/s
//                    float speedKmh = speed * 3.6f; // Convert to km/h
//
////                    Toast.makeText(getApplicationContext(),
////                            "Speed: " + speedKmh + " km/h",
////                            Toast.LENGTH_SHORT).show();
//
////                    TextView tv = findViewById(R.id.speedText);
////                    tv.setText("Speed: " + speedKmh + " km/h");
//
//                    if (speedKmh <= 6.0f) {
//                        frameUpdater = false;
//                        stopGifAnimation();
////                        if(speedKmh == 0 ) {
////                            player.pause();
////                        } else {
////                            player.play();
////                            player.setPlaybackParameters(new PlaybackParameters(speedKmh >= 40 ? 4.5f : speedKmh < 39 && speedKmh > 20 ? 2.5f : 1.0f));  // 1.5x speed
////
////                        }
//                        player.pause();
//
//                    } else {
//                        frameUpdater = true;
//                        player.play();
//                        player.setPlaybackParameters(new PlaybackParameters(speedKmh >= 40 && speedKmh < 60? 3.0f : speedKmh < 39 && speedKmh > 20 ? 2.0f : speedKmh >= 60 ? 4.0f : 0.5f));  // 1.5x speed
//
////                        startGifAnimation(speed);
//                        stopGifAnimation();
//                    }
//
//                } else {
//                    frameUpdater = false;
//                    stopGifAnimation();
//                    player.pause();
//                }
//            }
//
//            private void startGifAnimation(float speed) {
//
////                if (gifDrawable.isRunning()) return; // Avoid restarting if already running
//
////                gifDrawable.start();
//                if (!isUpdatingFrames) {
//                    isUpdatingFrames = true;
//
////                    frameUpdateRunnable = new Runnable() {
////                        @Override
////                        public void run() {
////                            if (speed * 3.6f >= 40.0f) {
////                                frameUpdateRunnable = new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        int currentFrame = gifDrawable.getCurrentFrameIndex();
////                                        int totalFrames = gifDrawable.getNumberOfFrames();
////
////                                        // Advance frame cyclically
////                                        gifDrawable.seekToFrame((currentFrame + 2));
////
////                                        // Dynamically adjust speed, ensure minimum delay of 100ms
//////                            int loadSpeed = Math.max(400 - (int) speed * 3, 100);
////                                        handler.postDelayed(this, 250);
////                                    }
////                                };
////                            }
////                        }
////
////                        ;
////                    },;
////                    if(speed * 3.6f >= 40.0f) {
////                        handler.post(frameUpdateRunnable);
////                    }
//                }
//            }
//
//            private void stopGifAnimation() {
////                gifDrawable.stop();
//                if (isUpdatingFrames) {
////                    handler.removeCallbacks(frameUpdateRunnable);
//                    isUpdatingFrames = false;
//                }
//
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//            @Override
//            public void onProviderEnabled(String provider) {}
//            @Override
//            public void onProviderDisabled(String provider) {}
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        } else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
//            }
//        // Find VideoView by ID
////        VideoView videoView = findViewById(R.id.videoBackground);
//
//        // Construct the video URI for raw resource
////        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.m_car;
////        Uri videoUri = Uri.parse(videoPath);
////
////        // Set video URI
////        PlayerView playerView = findViewById(R.id.videoBackground);
////        player = new ExoPlayer.Builder(this).build();
////        playerView.setPlayer(player);
////
////        MediaItem mediaItem = MediaItem.fromUri(videoUri);
////        player.setMediaItem(mediaItem);
////        player.prepare();
////        player.setPlayWhenReady(true);
////        player.setRepeatMode(Player.REPEAT_MODE_ALL);
////        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
////        player.pause();
//        // Adjust playback speed (works on all versions)
////        player.setPlaybackParameters(new PlaybackParameters(4.5f));  // 1.5x speed
//    }
//
//    private void onButtonClick1(View view) {
//        if(serviceIntent2 != null) {
//            stopService(serviceIntent2);
//            serviceIntent2 = null;
//        } else {
//            serviceIntent2 = new Intent(getApplicationContext(), FloatingWindowService.class);
//            getApplicationContext().startService(serviceIntent2);
//        }
//    }
//
//    ;
//
//    private void startTextScroll() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scrollPos -= 2;  // Move left by 2 pixels (adjust for smoothness)
//                scrollingText.setTranslationX(scrollPos);
//
//                if (Math.abs(scrollPos) > scrollingText.getWidth()) {
//                    scrollPos = scrollingText.getWidth();  // Reset position
//                }
//
//                handler.postDelayed(this, SCROLL_SPEED);
//            }
//        }, SCROLL_SPEED);
//    }
//
//    @SuppressLint("MissingPermission")
//    private void startSpeedTracking() {
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//                if (location.hasSpeed()) {
//                    float speedMps = location.getSpeed();  // Speed in meters/second
//                    float speedKph = speedMps * 3.6f;  // Convert to km/h
////                    speedTextView.setText("Speed: " + speedKph + " km/h");
//                Toast.makeText(getApplicationContext(),"Speed: " + speedKph + " km/h", Toast.LENGTH_LONG).show();
//                } else {
////                    speedTextView.setText("Speed: N/A (Waiting for GPS)");
//                    Toast.makeText(getApplicationContext(),"Speed: N/A (Waiting for GPS)", Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//            @Override
//            public void onProviderEnabled(@NonNull String provider) {}
//
//            @Override
//            public void onProviderDisabled(@NonNull String provider) {}
//        };
//
//        // Request location updates (every 2 seconds, with 1 meter change)
////        if(locationManager != null) {
////            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
////
////        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
//    }
//
////    private void connectToWifi(String ssid, String password) {
////        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
////
////        if (wifiManager == null) {
////            Toast.makeText(this, "Wi-Fi not supported", Toast.LENGTH_LONG).show();
////            return;
////        }
////
////        if (!wifiManager.isWifiEnabled()) {
////            wifiManager.setWifiEnabled(true);
////        }
////
////        WifiConfiguration wifiConfig = new WifiConfiguration();
////        wifiConfig.SSID = "\"" + ssid + "\"";
////        wifiConfig.preSharedKey = "\"" + password + "\"";
////
////        // Configure WPA2-PSK security
////        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
////        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
////        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
////        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
////        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
////        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
////
////        // Remove existing network if it exists
////        int existingNetworkId = getExistingNetworkId(wifiManager, ssid);
////        if (existingNetworkId != -1) {
////            wifiManager.removeNetwork(existingNetworkId);
////            wifiManager.saveConfiguration();
////        }
////
////        // Add and connect to the new network
////        int netId = wifiManager.addNetwork(wifiConfig);
////        if (netId == -1) {
////            Toast.makeText(this, "Failed to add network", Toast.LENGTH_LONG).show();
////            return;
////        }
////
////        wifiManager.disconnect();
////        wifiManager.enableNetwork(netId, true);
////        wifiManager.reconnect();
////
////        Toast.makeText(this, "Connecting to " + ssid, Toast.LENGTH_LONG).show();
////    }
//
//    // for android 8.0.0
//
//    private void connectToWifi(String ssid, String password) {
//        TextView text = findViewById(R.id.wifiText);
//        text.setText("");
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if (wifiManager == null) {
//            Toast.makeText(this, "Wi-Fi not supported", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//            wifiManager.disconnect();
//        }
//
//        WifiConfiguration wifiConfig = new WifiConfiguration();
//        wifiConfig.SSID = "\"" + ssid + "\"";
//        wifiConfig.preSharedKey = "\"" + password + "\"";
//
//        // Set WPA2-PSK security settings
//        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//
//        // Remove existing network if it exists
//        int existingNetworkId = getExistingNetworkId(wifiManager, ssid);
//        if (existingNetworkId != -1) {
//            wifiManager.removeNetwork(existingNetworkId);
//        }
//        if(wifiManager != null){
//            wifiManager.disconnect();
//        }
//        // Add and connect to the new network
//        int netId = wifiManager.addNetwork(wifiConfig);
//        if (netId == -1) {
//
//            Toast.makeText(this, "Failed to add network", Toast.LENGTH_LONG).show();
////            connectToWifi(ssid, password);
//            TextView tv = findViewById(R.id.wifiText);
//            tv.setText("Turn on hotspot with name 'kitty' and password '123456789' and click on wifi button to enjoy funfacts");
//            return;
//        }
//
//        wifiManager.disconnect();
//        wifiManager.enableNetwork(netId, true);
//        wifiManager.reconnect();
//
//        Toast.makeText(this, "Connecting to " + ssid, Toast.LENGTH_LONG).show();
//        text.setText("Connected to " + ssid);
//        ImageView imageView = findViewById(R.id.wifiImageButton);
//
//// Remove tint by clearing color filter
////        imageView.setImageTintList(null);
////        imageView.setint
////        ImageViewCompat.setImageTintList(imageView, null);
//            imageView.setColorFilter(Color.TRANSPARENT);
////        imageView.clearColorFilter();
//    }
//
//    // Check for Wi-Fi related permissions
//    private boolean checkPermissions() {
//        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    // Request necessary permissions at runtime
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, new String[]{
//                android.Manifest.permission.ACCESS_WIFI_STATE,
//                android.Manifest.permission.CHANGE_WIFI_STATE,
//                android.Manifest.permission.ACCESS_FINE_LOCATION // Needed from Android 8.0+
//        }, WIFI_PERMISSION_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == WIFI_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                connectToWifi(WIFI_SSID, WIFI_PASSWORD);
//            } else {
//                Toast.makeText(this, "Wi-Fi permissions are required!", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private int getExistingNetworkId(WifiManager wifiManager, String ssid) {
////        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            for (WifiConfiguration config : wifiManager.getConfiguredNetworks()) {
//                if (config.SSID != null && config.SSID.equals("\"" + ssid + "\"")) {
//                    return config.networkId;
//                }
//            }
////        }
//        return -1;
//    }
//
//    private void scanForWifi() {
//        Toast.makeText(this, "Enabling Wi-Fi start", Toast.LENGTH_SHORT).show();
//
//        if (!wifiManager.isWifiEnabled()) {
//            Toast.makeText(this, "Enabling Wi-Fi...", Toast.LENGTH_SHORT).show();
//            wifiManager.setWifiEnabled(true);
//        }
//
//        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        boolean success = wifiManager.startScan();
//
//        if (!success) {
//            Toast.makeText(this, "Wi-Fi scan failed. Try again.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            unregisterReceiver(this);
//
//            boolean found = false;
////            Toast.makeText(getApplicationContext(),"HELLO",Toast.LENGTH_LONG).show();
//
////            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();
//
//                for (ScanResult scanResult : results) {
////                    Toast.makeText(getApplicationContext(), scanResult.SSID.toString()).show();
////                    Toast.makeText(getApplicationContext(),scanResult.SSID,Toast.LENGTH_LONG).show();
//                    if (scanResult.SSID.equals("Kitty1")) {
//                        found = true;
//                        break;
//                    }
//                }
//
//                if (found) {
//                    connectToWifi(WIFI_SSID, WIFI_PASSWORD);
//                    Toast.makeText(context, "Wi-Fi 'Kitty1' is available!", Toast.LENGTH_LONG).show();
//                } else {
//                    TextView text = findViewById(R.id.wifiText);
//                    text.setText("Turn on hotspot with name 'Kitty1' and password '123456789' and click on wifi button to enjoy funfacts");
//
//                    Toast.makeText(context, "Wi-Fi 'Kitty1' not found!", Toast.LENGTH_LONG).show();
//                }
////            }
//        }
//    };
//
//    public void onButtonClick(View view) {
//        if (checkPermissions()) {
//            scanForWifi();
//            connectToWifi(WIFI_SSID, WIFI_PASSWORD);
//        } else {
//            requestPermissions();
//        }
//    }
//
//    public void closeIntent() {
//        getApplicationContext().startService(serviceIntent2);
//
//    }
//
//    public void onOpenCameraPage(View view) {
//        Intent intent = new Intent(this, SplashActivity.class);
//        Intent intent1 = new Intent(this, FloatingWindowService.class);
//        stopService(intent1);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }
//
////    private void startVpn() {
////        // Create the VPN configuration
////        Intent vpnIntent = VpnService.prepare(this);
////        if (vpnIntent != null) {
////            startActivityForResult(vpnIntent, VPN_REQUEST_CODE); // Request permission to use VPN
////        } else {
////            // Already granted permission, create VPN connection directly
////            connectToVpn();
////        }
////    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == VPN_REQUEST_CODE) {
////            if (resultCode == RESULT_OK) {
////                // Permission granted, now connect to the VPN
////                connectToVpn();
////            } else {
////                // Permission denied
////                Toast.makeText(this, "VPN permission denied", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
//
////    private void connectToVpn() {
////        Intent intent = new Intent(Intent.ACTION_MAIN);
////        intent.setClassName("com.android.settings", "com.android.settings.Settings$VpnSettingsActivity");
////        startActivity(intent);
//    // Start the VPN service
////        Intent intent = new Intent(this, CustomVPNClass.class);
////        startService(intent);
////        Intent intent = new Intent();
////        intent.setClassName("com.android.settings", "com.android.settings.VpnSettings");
////        startActivity(intent);    }
////    }
//
//}
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SpeechToTextActivityForVPN extends AppCompatActivity implements PocketSphinxService.RecognitionCallback, FloatingWindowService.RecognitionCallback {
    private static final int VPN_REQUEST_CODE = 0x0F;
    private static final int START_VPN_PROFILE = 70;
    private boolean isBindedService = false;
    private static final String WIFI_SSID = "Kitty1";
    private static final String WIFI_PASSWORD = "123456789";
    private static final int WIFI_PERMISSION_REQUEST_CODE = 1001;
    private HorizontalScrollView scrollView;
    private TextView scrollingText;
    private Handler handler;
    private TextView numberPlateText;
    private int scrollPos = 0;
    private final int SCROLL_SPEED = 50;
    WifiManager wifiManager;
    private LocationManager locationManager;
    private TextView speedTextView;
    private static final int LOCATION_PERMISSION_REQUEST = 100;
    boolean frameUpdater = true;
    private ExoPlayer player;
    private Intent serviceIntent2;
    private FloatingWindowService mFloatingService;
    private PocketSphinxService mSphinxService;
    private boolean isListening = false;

    private boolean isFloatingBound = false;
    private boolean isSphinxBound = false;
    ImageView imageView1;
    private ServiceConnection floatingConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FloatingWindowService.LocalBinder binder = (FloatingWindowService.LocalBinder) service;
            mFloatingService = binder.getService();
            mFloatingService.setRecognitionCallback(SpeechToTextActivityForVPN.this);
            isFloatingBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isFloatingBound = false;
        }
    };

    private final ServiceConnection sphinxConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PocketSphinxService.LocalBinder binder = (PocketSphinxService.LocalBinder) service;
            mSphinxService = binder.getService();
            mSphinxService.setRecognitionCallback(SpeechToTextActivityForVPN.this);
            isSphinxBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isSphinxBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, FloatingWindowService.class), floatingConnection, BIND_AUTO_CREATE);
        bindService(new Intent(this, PocketSphinxService.class), sphinxConnection, BIND_AUTO_CREATE);
        // Bind to the service
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case START_VPN_PROFILE:
//                    VPNLaunchHelper.startOpenVpn(mVpnProfile, this);
                    break;
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


//    @Override
//    public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
//        BindUtils.bindStatus(state, logmessage, localizedResId, level);
//    }

    public void checkAndRequestGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is disabled. Please enable it.", Toast.LENGTH_LONG).show();

            // Open GPS settings
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            Toast.makeText(this, "GPS is enabled!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        checkMicrophone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{"android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE" },
                    123
            );
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{"android.permission.READ_EXTERNAL_STORAGE"},
                    123
            );
        }
//
        if (ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.RECORD_AUDIO"}, 1);
        } else {
            executeAdbCommands();
            startSphinxService();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Security.insertProviderAt(Conscrypt.newProvider(), 1);
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Toast.makeText(this, "Please enable permission for floating window", Toast.LENGTH_SHORT).show();

            //  if (!Settings.canDrawOverlays(this)) {
            // If permission is not granted, show a toast and prompt user
            Toast.makeText(this, "Please enable permission for floating window", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            // startActivitty(intent);
//            } else {
            Toast.makeText(this, "Please enable permission for floating window", Toast.LENGTH_SHORT).show();
            // Permission is already granted, proceed
//            }
        }
//        System.loadLibrary("crypto"); // Use the name of your library without the 'lib' prefix and '.so' suffix
//        System.loadLibrary("openvpn"); // Use the name of your library without the 'lib' prefix and '.so' suffix
//        System.loadLibrary("jbcrypto"); // Use the name of your library without the 'lib' prefix and '.so' suffix
//        System.loadLibrary("opvpnutil"); // Use the name of your library without the 'lib' prefix and '.so' suffix

        super.onCreate(savedInstanceState);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
//                updateBackgroundWallpaper();

            }
        }, 5000, 60000);

        setContentView(R.layout.speech_text);

        TextView textView = findViewById(R.id.speedText);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Michelin_Bold.ttf");
//        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/snowstorm_b.otf");

        textView.setTypeface(customFont);
//        textView.setTextColor(Color.argb(50, 255, 0, 0));

        TextView marqueeText = findViewById(R.id.wifiText);
        numberPlateText = findViewById(R.id.numberPlate);

        // Enable marquee effect
        Calendar calendar = Calendar.getInstance();
        String formattedTime = new SimpleDateFormat("H", Locale.US).format(calendar.getTime());
        String ampm = new SimpleDateFormat("a", Locale.US).format(calendar.getTime());
        Log.d("HELLO : ", ampm);
        if (ampm.equalsIgnoreCase("am") || parseInt(formattedTime) < 18) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.m_car;
            Uri videoUri = Uri.parse(videoPath);

            // st number plate
            // Convert dp to pixels (very important!)
            // Convert dp to pixels
            int marginLeftPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, -366, getResources().getDisplayMetrics());
            int marginTopPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());

// Get layout params and set margins
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) numberPlateText.getLayoutParams();
            params.setMargins(marginLeftPx, marginTopPx, 0, 0);

// Apply the updated params
            numberPlateText.setLayoutParams(params);
            numberPlateText.setRotation(0f);
////

// Get the layout params and cast them

            // Set video URI
            PlayerView playerView = findViewById(R.id.videoBackground);
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            if (player != null && player.getPlaybackState() == Player.STATE_READY) {
                try {
                    Toast.makeText(getApplicationContext(), "PAUSED", Toast.LENGTH_SHORT).show();
                    player.pause(); // or player.play();
                } catch (IllegalStateException e) {
                    Log.e(TAG, "ExoPlayer pause/play failed", e);
                }
            }
        } else {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.n_car;
            Uri videoUri = Uri.parse(videoPath);

            int marginLeftPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, -409, getResources().getDisplayMetrics());
            int marginTopPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 46, getResources().getDisplayMetrics());
            Toast.makeText(getApplicationContext(), "PAUSED 999999", Toast.LENGTH_SHORT).show();
// Get layout params and set margins
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) numberPlateText.getLayoutParams();
            params.setMargins(marginLeftPx, marginTopPx, 0, 0);

// Apply the updated params
            numberPlateText.setLayoutParams(params);
            numberPlateText.setRotation(4f);

            // Set video URI
            PlayerView playerView = findViewById(R.id.videoBackground);

            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            if (player != null && player.getPlaybackState() == Player.STATE_READY) {
                try {
                    player.pause(); // or player.play();

                    Toast.makeText(getApplicationContext(), "PAUSED", Toast.LENGTH_SHORT).show();

                } catch (IllegalStateException e) {
                    Log.e(TAG, "ExoPlayer pause/play failed", e);
                }
            }
        }


        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "CLICK", Toast.LENGTH_LONG).show();
                scrollView = findViewById(R.id.scrollView);
                scrollingText = findViewById(R.id.wifiText);
                handler = new Handler();
                startTextScroll();
            }

        }, 20000);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ImageView floatingImageView = findViewById(R.id.ImageBackgroundView);

//        connectToWifi("Kitty", "123456789");
        Glide.with(getApplicationContext()).load(R.drawable.dmin7)
                .override(800, 600)  // Resize to avoid memory issues
                .into(floatingImageView);

        ImageView imageView = findViewById(R.id.wifiImageButton);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onButtonClick(view);
            }
        });
        imageView1 = findViewById(R.id.cameraButton);
        imageView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!isListening){
                    onButtonClick1(view);
                } else {
                    imageView1.setImageResource(R.drawable.mic);
                    ImageViewCompat.setImageTintList(imageView1, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white)));
                    mSphinxService.stopListening();
                    isListening = false;
                }
//                onButtonClick1(view);
            }
        });
        checkAndRequestGPS();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            private Handler handler = new Handler();
            private Runnable frameUpdateRunnable;
            private boolean isUpdatingFrames = false; // To prevent multiple handlers

            @Override
            public void onLocationChanged(Location location) {
                if (location.hasSpeed()) {
                    float speed = location.getSpeed(); // Speed in m/s
                    float speedKmh = speed * 3.6f; // Convert to km/h

//                    Toast.makeText(getApplicationContext(),
//                            "Speed: " + speedKmh + " km/h",
//                            Toast.LENGTH_SHORT).show();

                    TextView tv = findViewById(R.id.speedText);
//                    tv.setText("Speed: " + speedKmh + " km/h");
                    if (speedKmh <= 8) {
                        tv.setText("0");
                    } else {
                        tv.setText(String.valueOf(((int) speedKmh)));

                    }
//                        tv.setText("100");
                    if (speedKmh <= 30) {
                        tv.setTextColor(Color.parseColor("#804CAF50")); // 101-200 speed range
                    } else if (speedKmh <= 60) {
                        tv.setTextColor(Color.parseColor("#80039BE5")); // 101-200 speed range
                    } else if (speedKmh <= 100) {
                        tv.setTextColor(Color.parseColor("#80FFAB00")); // 101-200 speed range
                    } else {
                        tv.setTextColor(Color.parseColor("#80FF5722")); // 101-200 speed range
                    }

                    if (speedKmh <= 6.0f) {
                        frameUpdater = false;
                        stopGifAnimation();
                        if (player != null && player.getPlaybackState() == Player.STATE_READY) {
                            try {
                                player.pause(); // or player.play();
                                Toast.makeText(getApplicationContext(), "PAUSED 3", Toast.LENGTH_SHORT).show();

                            } catch (IllegalStateException e) {
                                Log.e(TAG, "ExoPlayer pause/play failed", e);
                            }
                        }
                    } else {
                        frameUpdater = true;
//                        Toast.makeText(getApplicationContext(), "PLAYED", Toast.LENGTH_SHORT).show();
                        player.play();
                        player.setPlaybackParameters(new PlaybackParameters(speedKmh >= 40 && speedKmh < 60 ? 3.0f : speedKmh < 39 && speedKmh > 20 ? 2.0f : speedKmh >= 60 ? 4.0f : 0.5f));  // 1.5x speed
                        stopGifAnimation();
                    }
                } else {
                    frameUpdater = false;
                    stopGifAnimation();
                    if (player != null && player.getPlaybackState() == Player.STATE_READY) {
                        try {
                            player.pause(); // or player.play();
//                            Toast.makeText(getApplicationContext(), "PAUSED 4", Toast.LENGTH_SHORT).show();

                        } catch (IllegalStateException e) {
                            Log.e(TAG, "ExoPlayer pause/play failed", e);
                        }
                    }
                }
            }


            private void startGifAnimation(float speed) {

//                if (gifDrawable.isRunning()) return; // Avoid restarting if already running

//                gifDrawable.start();
                if (!isUpdatingFrames) {
                    frameUpdateRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (speed * 3.6f >= 40.0f) {
                                frameUpdateRunnable = new Runnable() {
                                    @Override
                                    public void run() {
//                                        int currentFrame = gifDrawable.getCurrentFrameIndex();
//                                        int totalFrames = gifDrawable.getNumberOfFrames();

                                        // Advance frame cyclically
//                                        gifDrawable.seekToFrame((currentFrame + 2));

                                        // Dynamically adjust speed, ensure minimum delay of 100ms
//                            int loadSpeed = Math.max(400 - (int) speed * 3, 100);
                                        handler.postDelayed(this, 250);
                                    }
                                };
                            }
                        }

                        ;
                    };
                    if (speed * 3.6f >= 40.0f) {
                        handler.post(frameUpdateRunnable);
                    }

                    isUpdatingFrames = true;
                }
            }

            private void stopGifAnimation() {
//                gifDrawable.stop();
                if (isUpdatingFrames) {
//                    handler.removeCallbacks(frameUpdateRunnable);
                    isUpdatingFrames = false;
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        frameUpdater = true;
//        Toast.makeText(getApplicationContext(),"PLAYED 123", Toast.LENGTH_SHORT).show();

//        player.setPlaybackParameters(new PlaybackParameters(0.5f));  // 1.5x speed
//        stopGifAnimation();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        }
//        serviceIntent2 = new Intent(getApplicationContext(), FloatingWindowService.class);
//        getApplicationContext().startService(serviceIntent2);
//        Intent floatingIntent = new Intent(this, FloatingWindowService.class);
//        startService(floatingIntent);
//

        Log.d("HELLO", "onButtonClick1");

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(),"STARTED Listening", Toast.LENGTH_SHORT).show();
//                onButtonClick2();
//            }
//        }, 40000);

    }

    private void startSphinxService() {
        if (isSphinxBound && mSphinxService != null) {
            imageView1.setImageResource(R.drawable.mic);
            ImageViewCompat.setImageTintList(imageView1, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_200)));
            isListening = true;

            mSphinxService.startListening();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mSphinxService.stopListening();
//                }
//            }, 10000);
        }
    }

    public void stopListening() {
        imageView1.setImageResource(R.drawable.mic);
        ImageViewCompat.setImageTintList(imageView1, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white)));
        mSphinxService.stopListening();
        isListening = false;
    }

    private void onButtonClick2() {
//        if (serviceIntent2 != null) {
//            stopService(serviceIntent2);
//            serviceIntent2 = null;
//        } else {
//            serviceIntent2 = new Intent(getApplicationContext(), FloatingWindowService.class);
//            getApplicationContext().startService(serviceIntent2);
//        }
//        triggerMicrophone();
//        Log.d("HELLO", "onButtonClick1");
//        executeAdbCommands();
//        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
//
//        startSphinxService();
//        Intent i = new Intent(getApplicationContext(),PocketSphinxActivity.class);
//        startActivity(i);
//        onStartListening(view);

//    callChatGPT("Who are you");
        executeAdbCommands();
        Toast.makeText(getApplicationContext(), "Started Listening", Toast.LENGTH_SHORT).show();
        startSphinxService();
    }


    private void onButtonClick1(View view) {
//        if (serviceIntent2 != null) {
//            stopService(serviceIntent2);
//            serviceIntent2 = null;
//        } else {
//            serviceIntent2 = new Intent(getApplicationContext(), FloatingWindowService.class);
//            getApplicationContext().startService(serviceIntent2);
//        }
//        triggerMicrophone();
//        Log.d("HELLO", "onButtonClick1");
//        executeAdbCommands();
//        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
//
//        startSphinxService();
//        Intent i = new Intent(getApplicationContext(),PocketSphinxActivity.class);
//        startActivity(i);
//        onStartListening(view);

//    callChatGPT("Who are you");
        executeAdbCommands();
        Toast.makeText(getApplicationContext(), "Started Listening", Toast.LENGTH_SHORT).show();
        startSphinxService();
    }





    // Implement callback methods
    @Override
    public void onPartialResult(String hypothesis) {
        Log.d("HELLO", hypothesis);
    }


    @Override
    public void onResult(String hypothesis) {
        Toast.makeText(getApplicationContext(), hypothesis,Toast.LENGTH_SHORT).show();
        Log.d("HELLO", hypothesis);
        if(mFloatingService != null && isFloatingBound && !hypothesis.equals("oh my Ragha")) {
//
            if(hypothesis.contains("time")){
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
                String formattedTime = sdf.format(calendar.getTime());
                mSphinxService.stopListening();
                isListening = false;
                ImageViewCompat.setImageTintList(imageView1, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
//                ImageViewCompat.setImageTintList(imageView1, null);
                imageView1.setImageResource(R.drawable.ic_reload);
                mFloatingService.speakText("Time is "+formattedTime, 9000, new FloatingWindowService.SpeakTextCallback() {
                @Override
                public void onSuccess() {
                    Log.d("SpeechToTextActivity", "SUCCESS");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            executeAdbCommands();
                            startSphinxService();
                        }
                    }, 5000);
                }

                @Override
                public void onFailure() {
                    Log.d("SpeechToTextActivity", "FAILURE");
                }
            });
            } else if(!(hypothesis.equals("") || hypothesis == null) && mFloatingService.isInternetAvailable()) {
                mSphinxService.stopListening();
                isListening = false;
                ImageViewCompat.setImageTintList(imageView1, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
//                ImageViewCompat.setImageTintList(imageView1, null);
                imageView1.setImageResource(R.drawable.ic_reload);
                mFloatingService.callChatGPT(hypothesis + ". share me only the content, without using emojis max 300 characters");
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopListening();
                        imageView1.setImageResource(R.drawable.mic);
                        onButtonClick2();

                    }
                }, 3000);
            }
            } else {
            Log.e("SpeechText","Service not bound");
        }

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
                                .put("content", "Hi there!"));
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
                                } catch (JSONException e) {
                                    Log.e("OR_JSON", "Parse error", e);
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("API_ERROR", "Error detail: " + anError.getErrorDetail());
                                Log.e("API_ERROR", "Error code: " + anError.getErrorCode());
                                Log.e("API_ERROR", "Error body: " + anError.getErrorBody());
                                Log.e("API_ERROR", "Error message: " + anError.getMessage());
                                if (anError.getCause() != null) {
                                    Log.e("API_ERROR", "Cause: ", anError.getCause());
                                }
                                Toast.makeText(getApplicationContext(),
                                        "Server error: code=" + anError.getErrorCode(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
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

    private static X509TrustManager defaultTrustManager() throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        throw new IllegalStateException("No X509TrustManager found");
    }

    private String parseResponse(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray choices = obj.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content").trim();
        } catch (Exception e) {
            return "Error parsing response";
        }
    }


    private static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    //    public void triggerMicrophone() {
//        final String TAG = "AudioProbe";
//
//        // 1) Probe sample rate & buffer
//        int[] rates = {8000, 11025, 16000};
//        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//        int chosenRate = -1, bufSize = -1;
//        for (int rate : rates) {
//            int size = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
//            if (size > 0) {
//                chosenRate = rate;
//                bufSize = size;
//                break;
//            }
//        }
//        if (chosenRate < 0) {
//            Toast.makeText(this, "No supported sample rate!", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Log.i(TAG, "Using sampleRate=" + chosenRate + "Hz, bufferSize=" + bufSize);
//
//        // 2) Cycle audio sources
//        int[] sources = {
//                MediaRecorder.AudioSource.MIC,
//                MediaRecorder.AudioSource.DEFAULT,
//                MediaRecorder.AudioSource.CAMCORDER,
//                MediaRecorder.AudioSource.VOICE_COMMUNICATION,
//                MediaRecorder.AudioSource.VOICE_RECOGNITION,
//                MediaRecorder.AudioSource.VOICE_CALL
//        };
//        AudioRecord audioRecord = null;
//        for (int src : sources) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            AudioRecord ar = new AudioRecord(
//                    src, chosenRate, channelConfig, audioFormat, bufSize);
//            if (ar.getState() == AudioRecord.STATE_INITIALIZED) {
//                Log.i(TAG, "Initialized with source=" + src);
//                audioRecord = ar;
//                break;
//            }
//            ar.release();
//        }
//        if (audioRecord == null) {
//            Toast.makeText(this,
//                    "Failed to init any AudioRecord source on this unit.",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        // 3) Start recording
//        audioRecord.startRecording();
//        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
//            Toast.makeText(this, "startRecording() failed!", Toast.LENGTH_LONG).show();
//            audioRecord.release();
//            return;
//        }
//        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
//
//        // 4) Read & release
//        int finalBufSize = bufSize;
//        AudioRecord finalAudioRecord = audioRecord;
//        new Thread(() -> {
//            byte[] buffer = new byte[finalBufSize];
//            int total = 0;
//            for (int i = 0; i < 20; i++) {
//                int r = finalAudioRecord.read(buffer, 0, buffer.length);
//                if (r < 0) break;
//                total += r;
//            }
//            finalAudioRecord.stop();
//            finalAudioRecord.release();
//            Log.i(TAG, "Total bytes read: " + total);
//        }).start();
//    }

    private static final int[] AUDIO_SOURCES = {
            MediaRecorder.AudioSource.DEFAULT,           // 0
            MediaRecorder.AudioSource.MIC,               // 1
            MediaRecorder.AudioSource.VOICE_CALL,        // 4
            MediaRecorder.AudioSource.CAMCORDER,         // 5
            MediaRecorder.AudioSource.VOICE_RECOGNITION, // 6
            MediaRecorder.AudioSource.VOICE_COMMUNICATION// 7
            // (on newer APIs you could also try UNPROCESSED = 9, HOTWORD = 1998, etc.)
    };

    public void probeAllSources() {
        final String TAG = "AudioProbe";
        int sampleRate = 16000;
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSize = AudioRecord.getMinBufferSize(
                sampleRate, channelConfig, audioFormat);

        if (bufferSize <= 0) {
            Log.e(TAG, "Invalid buffer size: " + bufferSize);
            return;
        }

        for (int source : AUDIO_SOURCES) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            AudioRecord ar = new AudioRecord(
                    source, sampleRate, channelConfig, audioFormat, bufferSize);

            if (ar.getState() == AudioRecord.STATE_INITIALIZED) {
                Log.i(TAG, "✔ Source " + source + " initialized OK");
                // you can break here if you just need one working source,
                // or you can continue to list all available ones:
                ar.release();
            } else {
                Log.w(TAG, "✘ Source " + source + " failed: state="
                        + ar.getState());
                ar.release();
            }
        }
    }

    // 2
//    public void triggerMicrophone() {
//        final String TAG = "KiaMicHandler";
//        final int SYSTEM_SAMPLE_RATE = 16000;
//        final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//        final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//        final int[] AUDIO_SOURCES = {6, 7, 1, 5};  // VOICE_RECOGNITION, VOICE_COMMUNICATION, MIC, CAMCORDER
//
//        // Execute ADB commands to disable voice service
//        executeAdbCommands();
//
//        // Audio recording setup
//        int bufferSize = AudioRecord.getMinBufferSize(SYSTEM_SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//        if (bufferSize <= 0) bufferSize = 2048;
//
//        AudioRecord recorder = null;
//        int workingSource = -1;
//
//        // Find working audio source
//        for (int source : AUDIO_SOURCES) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            recorder = new AudioRecord(
//                    source,
//                    SYSTEM_SAMPLE_RATE,
//                    CHANNEL_CONFIG,
//                    AUDIO_FORMAT,
//                    bufferSize * 2
//            );
//
//            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
//                workingSource = source;
//                Log.i(TAG, "Using audio source: " + source);
//                break;
//            } else {
//                Log.w(TAG, "Source " + source + " failed: " + recorder.getState());
//                recorder.release();
//                recorder = null;
//            }
//        }
//
//        if (recorder == null) {
//            Log.e(TAG, "No working audio source found");
//            showToast("Microphone unavailable. Disable 'Hey Kia' manually.");
//            return;
//        }
//
//        try {
//            // Start recording
//            recorder.startRecording();
//            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
//                throw new Exception("Failed to start recording");
//            }
//
//            // Capture audio
//            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
//            final long TIMEOUT_MS = 5000;
//            final long startTime = SystemClock.elapsedRealtime();
//            int totalBytes = 0;
//
//            while ((SystemClock.elapsedRealtime() - startTime) < TIMEOUT_MS) {
//                int bytesRead = recorder.read(buffer, bufferSize);
//                if (bytesRead <= 0) {
//                    Log.e(TAG, "Audio read error: " + bytesRead);
//                    break;
//                }
//                totalBytes += bytesRead;
//
//                // Process audio chunk
//                processAudioChunk(buffer.array(), bytesRead);
//                buffer.clear();
//            }
//
//            Log.i(TAG, "Captured " + totalBytes + " bytes");
//        } catch (Exception e) {
//            Log.e(TAG, "Recording failed: " + e.getMessage());
//        } finally {
//            // Cleanup
//            if (recorder != null) {
//                if (recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
//                    recorder.stop();
//                }
//                recorder.release();
//            }
//        }
//    }
//
//    private void executeAdbCommands() {
//        new Thread(() -> {
//            try {
//                // 1. Disable voice handler service
//                runAdbCommand("pm disable com.mobis.voicehandler");
//
//                // 2. Force stop the package
//                runAdbCommand("am force-stop com.mobis.voicehandler");
//
//                // 3. Reset audio policy (non-root alternative)
//                runAdbCommand("media audio kill");
//
//                // 4. Wait for processes to terminate
//                SystemClock.sleep(800);
//
//            } catch (Exception e) {
//                Log.e("ADB", "Command execution failed", e);
//            }
//        }).start();
//    }
//
//    private void runAdbCommand(String command) {
//        try {
//            Process process = Runtime.getRuntime().exec(new String[]{
//                    "sh", "-c",
//                    "adb shell '" + command + "'"
//            });
//
//            // Wait for command to execute
//            process.waitFor();
//
//            // Read output
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream())
//            );
//            StringBuilder output = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }
//
//            Log.d("ADB", "Command: " + command + "\nOutput: " + output);
//
//        } catch (Exception e) {
//            Log.e("ADB", "Error executing: " + command, e);
//        }
//    }
//
//    private void processAudioChunk(byte[] audioData, int length) {
//        // Implement your speech-to-text processing here
//        // Example: send to PocketSphinx or cloud-based STT service
//    }

    public void triggerMicrophone() {

        final String TAG = "KiaMicHandler";
        final int SYSTEM_SAMPLE_RATE = 16000;
        final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
        final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int[] AUDIO_SOURCES = {6, 7, 1, 5};
        final int RECORD_DURATION = 5000; // 5 seconds
//                executeAdbCommands();

        new Thread(() -> {
            // 1. Get SD card directory
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.exists() || !sdcardDir.canWrite()) {
                Log.e(TAG, "SD card not available or writable");
                showToast("SD card not available");
                return;
            }

            // 2. Create output file on SD card root
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
            String filename = "kia_recording_" + sdf.format(new Date()) + ".pcm";
            File outputFile = new File(sdcardDir, filename);

            // 3. Audio setup
            int bufferSize = AudioRecord.getMinBufferSize(SYSTEM_SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT);
            if (bufferSize <= 0) bufferSize = 4096;

            AudioRecord recorder = null;
            int workingSource = -1;

            // 4. Find working source
            for (int source : AUDIO_SOURCES) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                recorder = new AudioRecord(
                        source,
                        SYSTEM_SAMPLE_RATE,
                        CHANNEL_CONFIG,
                        AUDIO_FORMAT,
                        bufferSize * 2
                );

                if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    workingSource = source;
                    Log.i(TAG, "Acquired source: " + source);
                    break;
                }
                safeReleaseRecorder(recorder);
                recorder = null;
            }

            if (recorder == null) {
                Log.e(TAG, "No working audio source found");
                return;
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                recorder.startRecording();

                // 5. Verify recording state
                final long startTime = SystemClock.elapsedRealtime();
                while (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                    if (SystemClock.elapsedRealtime() - startTime > 2000) {
                        throw new IllegalStateException("Recording start timeout");
                    }
                    SystemClock.sleep(50);
                }

                Log.i(TAG, "Recording started. Saving to: " + outputFile.getAbsolutePath());

                // 6. Capture loop (5 seconds)
                byte[] buffer = new byte[bufferSize];
                long bytesWritten = 0;

                while (SystemClock.elapsedRealtime() - startTime < RECORD_DURATION) {
                    int bytesRead = recorder.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        fos.write(buffer, 0, bytesRead);
                        bytesWritten += bytesRead;
                    } else {
                        handleReadError(bytesRead);
                    }
                }



//                here i get pcm file
                // outputfile or outputFile.getAbsoluteFile() check againn MAIN
                File outputFile1 = new File(sdcardDir, "HelloWorld.wav");
                File outputFile2 = new File(sdcardDir, "HelloWorld1.pcm");
                // 7. Add WAV header to make file playable
                addWavHeader(outputFile, bytesWritten, SYSTEM_SAMPLE_RATE);
                Log.i(TAG, "Recording complete. Saved " + bytesWritten + " bytes");
                showToast("Audio saved to SD card: " + filename);

            } catch (Exception e) {
                Log.e(TAG, "Recording failed: " + e.getMessage());
                showToast("Error: " + e.getMessage());
            } finally {
                // 8. Cleanup
                safeCloseStream(fos);
                safeReleaseRecorder(recorder);
            }
        }).start();
    }
    // Convert WAV to audio stream compatible with RecognizerIntent
//    private void recognizeWithSystemRecognizer(File wavFile) {
//        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
//            showToast("System recognizer not available");
//            return;
//        }
//
//        try {
//            // Create temporary raw PCM file (system recognizer requires specific format)
//            File pcmFile = convertWavToRawPcm(wavFile);
//            Uri audioUri = Uri.fromFile(pcmFile);
//
//            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//
//            // This is the correct way to supply audio
//            intent.putExtra(RecognizerIntent.EXTRA_AUDIO_URI, audioUri.toString());
//
//            startActivityForResult(intent, 123);
//        } catch (Exception e) {
//            Log.e(TAG, "Recognition failed: " + e.getMessage());
//        }
//    }

    // Convert WAV to raw PCM (required by system recognizer)
    private File convertWavToRawPcm(File wavFile) throws IOException {
        File pcmFile = new File(getCacheDir(), "temp_audio.raw");

        try (FileInputStream fis = new FileInputStream(wavFile);
             FileOutputStream fos = new FileOutputStream(pcmFile)) {

            // Skip WAV header (44 bytes)
            long skipped = fis.skip(44);
            if (skipped != 44) throw new IOException("Invalid WAV header");

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return pcmFile;
    }

// Helper methods -------------------------------------------------

    private void addWavHeader(File pcmFile, long audioLength, int sampleRate) throws IOException {
        // Convert PCM to WAV by adding header
        File wavFile = new File(pcmFile.getParent(),
                pcmFile.getName().replace(".pcm", ".wav"));

        try (FileInputStream fis = new FileInputStream(pcmFile);
             FileOutputStream fos = new FileOutputStream(wavFile)) {

            // WAV header parameters
            final int channelCount = 1; // Mono
            final int bitsPerSample = 16;
            final long byteRate = sampleRate * channelCount * bitsPerSample/8;

            // Write WAV header
            byte[] header = new byte[44];
            header[0] = 'R';  header[1] = 'I';  header[2] = 'F';  header[3] = 'F';
            header[4] = (byte) (audioLength + 36 & 0xff);
            header[5] = (byte) ((audioLength + 36 >> 8) & 0xff);
            header[6] = (byte) ((audioLength + 36 >> 16) & 0xff);
            header[7] = (byte) ((audioLength + 36 >> 24) & 0xff);
            header[8] = 'W';  header[9] = 'A';  header[10] = 'V';  header[11] = 'E';
            header[12] = 'f'; header[13] = 'm'; header[14] = 't'; header[15] = ' ';
            header[16] = 16;  // Subchunk size
            header[20] = 1;   // Audio format (PCM)
            header[22] = (byte) channelCount;
            header[24] = (byte) (sampleRate & 0xff);
            header[25] = (byte) ((sampleRate >> 8) & 0xff);
            header[26] = (byte) ((sampleRate >> 16) & 0xff);
            header[27] = (byte) ((sampleRate >> 24) & 0xff);
            header[28] = (byte) (byteRate & 0xff);
            header[29] = (byte) ((byteRate >> 8) & 0xff);
            header[30] = (byte) ((byteRate >> 16) & 0xff);
            header[31] = (byte) ((byteRate >> 24) & 0xff);
            header[32] = (byte) (channelCount * bitsPerSample / 8); // Block align
            header[34] = (byte) bitsPerSample;
            header[36] = 'd'; header[37] = 'a'; header[38] = 't'; header[39] = 'a';
            header[40] = (byte) (audioLength & 0xff);
            header[41] = (byte) ((audioLength >> 8) & 0xff);
            header[42] = (byte) ((audioLength >> 16) & 0xff);
            header[43] = (byte) ((audioLength >> 24) & 0xff);

            fos.write(header);

            // Copy PCM data
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        // Delete the temporary PCM file
        pcmFile.delete();
    }

    private void safeReleaseRecorder(AudioRecord recorder) {
        if (recorder != null) {
            try {
                if (recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    recorder.stop();
                }
                recorder.release();
            } catch (IllegalStateException e) {
                Log.e("RecorderRelease", "Error: " + e.getMessage());
            }
        }
    }

    private void safeCloseStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e("StreamClose", "Error: " + e.getMessage());
            }
        }
    }

    private void handleReadError(int errorCode) {
        switch (errorCode) {
            case AudioRecord.ERROR_INVALID_OPERATION:
                Log.e("AudioRead", "Invalid operation");
                break;
            case AudioRecord.ERROR_BAD_VALUE:
                Log.e("AudioRead", "Invalid buffer");
                break;
            case AudioRecord.ERROR_DEAD_OBJECT:
                Log.e("AudioRead", "Audio hardware unavailable");
                break;
            default:
                Log.w("AudioRead", "Error code: " + errorCode);
        }
    }


    // 1:26 Working
//    public void triggerMicrophone() {
//        final String TAG = "KiaMicHandler";
//        final int SYSTEM_SAMPLE_RATE = 16000;
//        final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//        final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//        final int[] AUDIO_SOURCES = {6, 7, 1, 5};  // Prioritized sources
//
//        new Thread(() -> {
//            // 1. Execute non-root ADB commands to free microphone
//            executeAdbCommands();
//
//            // 2. Create audio dump directory to avoid permission errors
//            createAudioDumpDirectory();
//
//            // 3. Audio setup with timeout
//            int bufferSize = getValidBufferSize(SYSTEM_SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//            AudioRecord recorder = null;
//            int workingSource = -1;
//
//            final long startTime = SystemClock.elapsedRealtime();
//            final long TIMEOUT_MS = 7000;  // Increased timeout
//
//            while (SystemClock.elapsedRealtime() - startTime < TIMEOUT_MS) {
//                for (int source : AUDIO_SOURCES) {
//                    try {
//                        recorder = createAudioRecord(source, SYSTEM_SAMPLE_RATE,
//                                CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
//
//                        if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
//                            workingSource = source;
//                            Log.i(TAG, "Acquired source: " + source);
//                            break;
//                        }
//                        safeReleaseRecorder(recorder);
//                    } catch (Exception e) {
//                        Log.w(TAG, "Source " + source + " error: " + e.getMessage());
//                    }
//                }
//                if (workingSource != -1) break;
//                SystemClock.sleep(150);  // Increased delay between attempts
//            }
//
//            // 4. Handle acquisition failure
//            if (workingSource == -1) {
//                Log.e(TAG, "Microphone acquisition failed after " + TIMEOUT_MS + "ms");
//                showManualDisableInstructions();
//                return;
//            }
//
//            // 5. Start recording with anti-lockup measures
//            try {
//                recorder.startRecording();
//
//                // Verify recording state with timeout
//                final long RECORD_TIMEOUT = 2000;
//                final long recordStart = SystemClock.elapsedRealtime();
//                while (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
//                    if (SystemClock.elapsedRealtime() - recordStart > RECORD_TIMEOUT) {
//                        throw new IllegalStateException("Recording start timeout");
//                    }
//                    SystemClock.sleep(50);
//                }
//
//                Log.i(TAG, "Recording started successfully");
//
//                // 6. Capture audio with thread lockup prevention
//                ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
//                final long CAPTURE_DURATION = 30000;  // 30 seconds max
//
//                while (SystemClock.elapsedRealtime() - recordStart < CAPTURE_DURATION) {
//                    int bytesRead = recorder.read(buffer, bufferSize);
//
//                    if (bytesRead > 0) {
//                        processAudioChunk(buffer.array(), bytesRead);
//                        buffer.clear();
//                    } else if (bytesRead == AudioRecord.ERROR_INVALID_OPERATION) {
//                        handleLockupScenario(recorder);
//                        break;
//                    } else {
//                        Log.w(TAG, "Read error: " + bytesRead);
//                    }
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "Recording failed: " + e.getMessage());
//            } finally {
//                safeReleaseRecorder(recorder);
//            }
//        }).start();
//    }
//
//// Helper methods -------------------------------------------------
//
    private void executeAdbCommands() {
        try {
            runShellCommand("pm disable com.mobis.voicehandler");
            runShellCommand("am force-stop com.mobis.voicehandler");
            runShellCommand("media audio kill");
            runShellCommand("service call audio 39 i32 0");  // Reset audio driver
            SystemClock.sleep(1000);  // Longer delay for system reset
        } catch (Exception e) {
            Log.w("ADB", "Non-root commands failed: " + e.getMessage());
        }
    }
//
//    private void createAudioDumpDirectory() {
//        // Create directory to avoid "Permission denied" errors
//        File audioDumpDir = new File("/data/audiodump");
//        if (!audioDumpDir.exists()) {
//            try {
//                runShellCommand("mkdir -p /data/audiodump");
//                runShellCommand("chmod 777 /data/audiodump");
//            } catch (Exception e) {
//                Log.w("AudioDump", "Could not create dump directory: " + e.getMessage());
//            }
//        }
//    }
//
//    private int getValidBufferSize(int sampleRate, int channelConfig, int audioFormat) {
//        int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
//        if (bufferSize <= 0) {
//            // Fallback sizes based on common configurations
//            bufferSize = 4096;  // Increased buffer size for stability
//        }
//        return bufferSize * 2;  // Double buffer for safety
//    }
//
//    private AudioRecord createAudioRecord(int source, int sampleRate,
//                                          int channelConfig, int audioFormat, int bufferSize) {
//        // Workaround for channel mask issues
//        int actualChannelConfig = channelConfig;
//        if (source == 6) {  // VOICE_RECOGNITION
//            actualChannelConfig = AudioFormat.CHANNEL_IN_MONO;
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return null;
//        }
//        return new AudioRecord(
//                source,
//                sampleRate,
//                actualChannelConfig,
//                audioFormat,
//                bufferSize
//        );
//    }
//
//    private void handleLockupScenario(AudioRecord recorder) {
//        Log.w(TAG, "Thread lockup detected - resetting audio");
//
//        // 1. Immediately release current recorder
//        safeReleaseRecorder(recorder);
//
//        // 2. Reset audio subsystem
//        executeAdbCommands();
//
//        // 3. Add delay for hardware to reset
//        SystemClock.sleep(500);
//    }
//
//    private void safeReleaseRecorder(AudioRecord recorder) {
//        if (recorder != null) {
//            try {
//                if (recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
//                    recorder.stop();
//                }
//                recorder.release();
//            } catch (IllegalStateException e) {
//                Log.e("RecorderRelease", "Error: " + e.getMessage());
//            }
//        }
//    }
//
    private void runShellCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            process.waitFor();

            // Read output to prevent buffer deadlocks
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            while (reader.readLine() != null) { /* Discard output */ }
        } catch (Exception e) {
            Log.e("ShellCmd", "Failed: " + command, e);
        }
    }
//
//    private void showManualDisableInstructions() {
//        runOnUiThread(() -> {
//            new AlertDialog.Builder(this)
//                    .setTitle("Microphone Access Required")
//                    .setMessage("Please manually disable 'Hey Kia':\n\n" +
//                            "1. Go to Settings > Voice Recognition\n" +
//                            "2. Turn OFF 'Wake Word Detection'\n" +
//                            "3. Disable 'Voice Command'\n" +
//                            "4. Restart the infotainment system")
//                    .setPositiveButton("OK", null)
//                    .show();
//        });
//    }
//
//    private void processAudioChunk(byte[] audioData, int length) {
//        // Implement your speech recognition here
//        // Example using PCM data:
//        recognizeSpeech(audioData, length);
//    }
//
//    private void recognizeSpeech(byte[] audioData, int length) {
//        Log.e("CHECK AUDIO : ", Arrays.toString(audioData));
//        // This would interface with your speech recognition engine
//        // For example, with PocketSphinx:
////        if (pocketSphinx != null) {
////            pocketSphinx.startRecognition();
////            pocketSphinx.processRaw(audioData, length);
////
////            // Check for results periodically
////            Hypothesis hypothesis = pocketSphinx.getHypothesis();
////            if (hypothesis != null) {
////                String text = hypothesis.getHypstr();
////                runOnUiThread(() -> updateSpeechResult(text));
////            }
////        }
//    }
//
//    private void updateSpeechResult(String text) {
//        // Update UI with recognized text
////        textView.setText(text);
//        Log.e("CHECK", text);
//    }
    private void showToast(final String message) {
        runOnUiThread(() -> Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show());
    }

//    public void triggerMicrophone() {
//        final String TAG = "AudioProbe";
//
//        int sampleRate = 16000;
//        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//        probeAllSources();
//        int bufferSize = AudioRecord.getMinBufferSize(
//                sampleRate, channelConfig, audioFormat);
//        // Or hardcode: int bufferSize = 2048;
//
//        if (bufferSize <= 0) {
//            Log.e(TAG, "Invalid buffer size: " + bufferSize);
//            return;
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
////        AudioRecord audioRecord = new AudioRecord(
////                MediaRecorder.AudioSource.VOICE_COMMUNICATION,
////                sampleRate, channelConfig, audioFormat, bufferSize);
//
//        AudioRecord audioRecord = new AudioRecord(
//                MediaRecorder.AudioSource.VOICE_COMMUNICATION,
//                16000, AudioFormat.CHANNEL_IN_STEREO,
//                AudioFormat.ENCODING_PCM_16BIT, 2048);
//
//        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
//        Log.e(TAG, "AudioRecord init failed: state="
//                + audioRecord.getState());
//        return;
//    }
//
//    audioRecord.startRecording();
//    if (audioRecord.getRecordingState()
//            != AudioRecord.RECORDSTATE_RECORDING) {
//        Log.e(TAG, "startRecording() failed");
//        audioRecord.release();
//        return;
//    }
//    Log.i(TAG, "Recording started at " + sampleRate
//            + "Hz, stereo, buffer=" + bufferSize);
//
//    // Read a few frames to verify:
//    new Thread(() -> {
//        byte[] buf = new byte[bufferSize];
//        int total = 0;
//        for (int i = 0; i < 20; i++) {
//            int read = audioRecord.read(buf, 0, buf.length);
//            if (read < 0) break;
//            total += read;
//        }
//        audioRecord.stop();
//        audioRecord.release();
//        Log.i(TAG, "Total bytes read: " + total);
//    }).start();
//}

    ;

    private void startTextScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollPos -= 2;  // Move left by 2 pixels (adjust for smoothness)
                scrollingText.setTranslationX(scrollPos);

                if (Math.abs(scrollPos) > scrollingText.getWidth()) {
                    scrollPos = scrollingText.getWidth();  // Reset position
                }

                handler.postDelayed(this, SCROLL_SPEED);
            }
        }, SCROLL_SPEED);
    }

    @SuppressLint("MissingPermission")
    private void startSpeedTracking() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location.hasSpeed()) {
                    float speedMps = location.getSpeed();  // Speed in meters/second
                    float speedKph = speedMps * 3.6f;  // Convert to km/h
                    Log.d("TAG", String.valueOf(speedKph));
//                    speedTextView.setText("Speed: " + speedKph + " km/h");
//                    Toast.makeText(getApplicationContext(),"Speed: " + speedKph + " km/h", Toast.LENGTH_LONG).show();
                } else {
//                    speedTextView.setText("Speed: N/A (Waiting for GPS)");
                    Toast.makeText(getApplicationContext(),"Speed: N/A (Waiting for GPS)", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {}
        };

        // Request location updates (every 2 seconds, with 1 meter change)
//        if(locationManager != null) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
//
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    private void connectToWifi(String ssid, String password) {
        TextView text = findViewById(R.id.wifiText);
        text.setText("");
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            Toast.makeText(this, "Wi-Fi not supported", Toast.LENGTH_LONG).show();
            return;
        }

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            wifiManager.disconnect();
        }

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + ssid + "\"";
        wifiConfig.preSharedKey = "\"" + password + "\"";

        // Set WPA2-PSK security settings
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        // Remove existing network if it exists
        int existingNetworkId = getExistingNetworkId(wifiManager, ssid);
        if (existingNetworkId != -1) {
            wifiManager.removeNetwork(existingNetworkId);
        }
        if(wifiManager != null){
            wifiManager.disconnect();
        }
        // Add and connect to the new network
        int netId = wifiManager.addNetwork(wifiConfig);
        if (netId == -1) {

            Toast.makeText(this, "Failed to add network", Toast.LENGTH_LONG).show();
//            connectToWifi(ssid, password);
            TextView tv = findViewById(R.id.wifiText);
            tv.setText("Turn on hotspot with name 'kitty' and password '123456789' and click on wifi button to enjoy funfacts");
            return;
        }

        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        Toast.makeText(this, "Connecting to " + ssid, Toast.LENGTH_LONG).show();
        text.setText("Connected to " + ssid);
        ImageView imageView = findViewById(R.id.wifiImageButton);
        imageView.setColorFilter(Color.TRANSPARENT);
    }

    // Check for Wi-Fi related permissions
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Request necessary permissions at runtime
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION // Needed from Android 8.0+
        }, WIFI_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WIFI_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectToWifi(WIFI_SSID, WIFI_PASSWORD);
            } else {
                Toast.makeText(this, "Wi-Fi permissions are required!", Toast.LENGTH_LONG).show();
            }
        }
    }


    @SuppressLint("MissingPermission")
    private int getExistingNetworkId(WifiManager wifiManager, String ssid) {
        for (WifiConfiguration config : wifiManager.getConfiguredNetworks()) {
            if (config.SSID != null && config.SSID.equals("\"" + ssid + "\"")) {
                return config.networkId;
            }
        }
        return -1;
    }

    private void scanForWifi() {
        Toast.makeText(this, "Enabling Wi-Fi start", Toast.LENGTH_SHORT).show();

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Enabling Wi-Fi...", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        boolean success = wifiManager.startScan();

        if (!success) {
            Toast.makeText(this, "Wi-Fi scan failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);

            boolean found = false;

            @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();

            for (ScanResult scanResult : results) {
                if (scanResult.SSID.equals("Kitty1")) {
                    found = true;
                    break;
                }
            }

            if (found) {
                connectToWifi(WIFI_SSID, WIFI_PASSWORD);
                Toast.makeText(context, "Wi-Fi 'Kitty1' is available!", Toast.LENGTH_LONG).show();
            } else {
                TextView text = findViewById(R.id.wifiText);
                text.setText("Turn on hotspot with name 'Kitty1' and password '123456789' and click on wifi button to enjoy funfacts");

                Toast.makeText(context, "Wi-Fi 'Kitty1' not found!", Toast.LENGTH_LONG).show();
            }
        }
    };

    public void onButtonClick(View view) {
        if (checkPermissions()) {
            scanForWifi();
            connectToWifi(WIFI_SSID, WIFI_PASSWORD);
        } else {
            requestPermissions();
        }
    }

    public void closeIntent() {
        getApplicationContext().startService(serviceIntent2);
    }

    public void onOpenCameraPage(View view) {
//        executeAdbCommands();
//        onStartListening(view);

//        Intent intent = new Intent(this, SplashActivity.class);
        Intent i = new Intent(getApplicationContext(),PocketSphinxActivity.class);
        startActivity(i);
//        finish();
    }

    @Override
    public void onError(String error) {
        Log.e("PocketSphinx", "Error: " + error);
    }

    @Override
    public void onSetupComplete() {
        Toast.makeText(getApplicationContext(),"SETUP COMPLETE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onSpeechResult(String result) {
        Toast.makeText(getApplicationContext(),"RESULT : "+result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTextSpoken(String result) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopListening();
                imageView1.setImageResource(R.drawable.mic);
                onButtonClick2();

            }
        }, 10000);
        Toast.makeText(getApplicationContext(),"RESULT 123 : "+result, Toast.LENGTH_SHORT).show();
    }
}
