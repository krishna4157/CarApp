package com.vasilkoff.easyvpnfree;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class PocketSphinxService extends Service implements RecognitionListener {

    /* Named searches */
    private static final String KWS_SEARCH = "oh my Ragha";
    private static final String KEYPHRASE = "oh my Ragha";
    private static final String MENU_SEARCH = "menu";

    private SpeechRecognizer recognizer;
    private RecognitionCallback callback;
    private boolean isSetupComplete = false;

    // Binder to communicate with activity
    public class LocalBinder extends Binder {
        PocketSphinxService getService() {
            return PocketSphinxService.this;
        }
    }

    private final IBinder binder = new LocalBinder();



    public interface RecognitionCallback {
        void onPartialResult(String hypothesis);
        void onResult(String hypothesis);
        void onError(String error);
        void onSetupComplete();
        void onTimeout();
        void onSpeechResult(String result);
    }


    public void setRecognitionCallback(RecognitionCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Check permission and setup
        if (ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO")
                == PackageManager.PERMISSION_GRANTED) {
            setupRecognizer();
        }
    }

    private void setupRecognizer() {
        new SetupTask().execute();
        Log.d("PocketSphinxService", "setupRecognizer");
    }

    private class SetupTask extends AsyncTask<Void, Void, Exception> {

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                File modelsDir = new File(getFilesDir(), "sphinx-model");

                // Copy 'sync' folder from assets to internal storage: files/sphinx-model/
                copyAssetsRecursively(getAssets(), "sync", modelsDir);

                // Check if required acoustic model is present
                File acousticModel = new File(modelsDir, "en-us-ptm");
                if (!new File(acousticModel, "mdef").exists()) {
                    throw new FileNotFoundException("mdef not found in: " + acousticModel.getAbsolutePath());
                }

                // Setup recognizer with copied model path
                setupRecognizerImpl(modelsDir);
                Log.d("PocketSphinxService", "Setup Done");

                return null;

            } catch (IOException e) {
                Log.e("PocketSphinxService", "Setup Failed", e);
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                if (callback != null) callback.onError("Setup failed: " + result.getMessage());
            } else {
                isSetupComplete = true;
                if (callback != null) callback.onSetupComplete();
            }
        }

        // Recursive copy logic from assets/sync to internal dir
        private void copyAssetsRecursively(AssetManager assetManager, String fromAssetPath, File toDir) throws IOException {
            String[] files = assetManager.list(fromAssetPath);
            if (files == null || files.length == 0) {
                // Leaf file
                InputStream in = assetManager.open(fromAssetPath);
                if (!toDir.getParentFile().exists()) toDir.getParentFile().mkdirs();
                OutputStream out = new FileOutputStream(toDir);
                copyStream(in, out);
                in.close();
                out.close();
            } else {
                // Directory
                if (!toDir.exists()) toDir.mkdirs();
                for (String file : files) {
                    copyAssetsRecursively(assetManager, fromAssetPath + "/" + file, new File(toDir, file));
                }
            }
        }

        private void copyStream(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }


//    private class SetupTask extends AsyncTask<Void, Void, Exception> {
//        @Override
//        protected Exception doInBackground(Void... params) {
//            try {
//                Assets assets = new Assets(PocketSphinxService.this);
//                File assetDir = assets.syncAssets();
//                setupRecognizerImpl(assetDir);
//                Log.d("PocketSphinxService", "Setop Done");
//
//                return null;
//            } catch (IOException e) {
//                Log.d("PocketSphinxService", "Setup Failed");
//
//                return e;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Exception result) {
//            if (result != null) {
//                if (callback != null) callback.onError("Setup failed: " + result.getMessage());
//            } else {
//                isSetupComplete = true;
//                if (callback != null) callback.onSetupComplete();
//            }
//        }
//    }

    private void setupRecognizerImpl(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .getRecognizer();
        recognizer.addListener(this);
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);
        Log.d("PocketSphinxService", "Key Phrases Added");

    }

    // Public methods for activity to control
    public void startListening() {
        if (recognizer != null) {

            recognizer.startListening(KWS_SEARCH);
            Log.d("PocketSphinxService", "Listening Started");

        }
    }

    public void stopListening() {
        if (recognizer != null) {
            recognizer.stop();
        }
    }

    public void switchSearch(String searchName) {
        if (recognizer != null) {
            recognizer.stop();
            if (searchName.equals(KWS_SEARCH))
                recognizer.startListening(searchName);
            else
                recognizer.startListening(searchName, 10000);
        }
    }


    @Override
    public void onResult(Hypothesis hypothesis) {
//        Toast.makeText(getApplicationContext(), hypothesis.getHypstr(),Toast.LENGTH_SHORT).show();
        if (callback != null && !recognizer.getSearchName().equals(KWS_SEARCH)) callback.onResult(hypothesis != null ? hypothesis.getHypstr() : "");
    }

    @Override
    public void onError(Exception e) {

        Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();

        if (callback != null) callback.onError(e.getMessage());
    }

    @Override
    public void onTimeout() {
        if (callback != null) callback.onTimeout();
        switchSearch(KWS_SEARCH);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Other required methods from RecognitionListener
    @Override public void onBeginningOfSpeech() {}
    @Override public void onEndOfSpeech() {
        if (recognizer != null && !recognizer.getSearchName().equals(KWS_SEARCH)) {
//            switchSearch(MENU_SEARCH);
            stopListening();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null) return;
        String text = hypothesis.getHypstr();
//        Toast.makeText(getApplicationContext(), "1: "+text,Toast.LENGTH_SHORT).show();
        if (text.equals(KEYPHRASE)) switchSearch(MENU_SEARCH);
    }
}