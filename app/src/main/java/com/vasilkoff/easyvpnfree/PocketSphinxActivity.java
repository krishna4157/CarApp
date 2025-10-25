/* ====================================================================
 * Copyright (c) 2014 Alpha Cephei Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY ALPHA CEPHEI INC. ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 */

package com.vasilkoff.easyvpnfree;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Optional;



import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class PocketSphinxActivity extends Activity implements
        RecognitionListener {

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "hey Bhoomi";
    private static final String FORECAST_SEARCH = "forecast";
    private static final String DIGITS_SEARCH = "digits";
    private static final String PHONE_SEARCH = "phones";
    private static final String MENU_SEARCH = "menu";

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "hey Bhoomi";

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Prepare the data for UI
        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(DIGITS_SEARCH, R.string.digits_caption);
        captions.put(PHONE_SEARCH, R.string.phone_caption);
        captions.put(FORECAST_SEARCH, R.string.forecast_caption);
        setContentView(R.layout.main);
        ((TextView) findViewById(R.id.caption_text))
                .setText("Preparing the recognizer");

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                File wavFile = new File(Environment.getExternalStorageDirectory(),
////                        "HelloWorld.wav");
////                recognizeFromWavFile(wavFile);
//                switchSearch(MENU_SEARCH);
//            }}, 50000);
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<PocketSphinxActivity> activityReference;
        SetupTask(PocketSphinxActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                PocketSphinxActivity activity = activityReference.get();

//                Assets assets = new Assets(this);
//                Log.e("PocketSphinxActivity", assets.toString());
//                File modelsDir = assets.syncAssets();
//                activityReference.get().setupRecognizer(modelsDir);
                File modelsDir = new File(activity.getFilesDir(), "sphinx-model");

                copyAssetsRecursively(activity.getAssets(), "sync", modelsDir);

                File acousticModel = new File(modelsDir, "en-us-ptm");
                if (!new File(acousticModel, "mdef").exists()) {
                    throw new FileNotFoundException("mdef not found in: " + acousticModel.getAbsolutePath());
                }

                // Call recognizer setup with the extracted model path
                activity.setupRecognizer(modelsDir);
//                if (!modelDir.exists()) {
//                    copyAssetsToInternalStorage(activity.getAssets(), "sync", modelDir);
//                }

                // Setup recognizer with extracted model dir
//                activity.setupRecognizer(modelDir);
            } catch (IOException e) {
                Log.e("PocketSphinxActivity", "Failed to init recognizer", e);
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                ((TextView) activityReference.get().findViewById(R.id.caption_text))
                        .setText("Failed to init recognizer " + result);
            } else {
                activityReference.get().switchSearch(KWS_SEARCH);
            }
        }

        // Helper method to copy assets recursively
        private void copyAssetsRecursively(AssetManager assetManager, String fromAssetPath, File toDir) throws IOException {
            String[] files = assetManager.list(fromAssetPath);
            if (files == null || files.length == 0) {
                // It is a file
                InputStream in = assetManager.open(fromAssetPath);
                File outFile = toDir;
                if (!toDir.getParentFile().exists()) toDir.getParentFile().mkdirs();
                OutputStream out = new FileOutputStream(toDir);
                copyStream(in, out);
                in.close();
                out.close();
            } else {
                // It is a directory
                if (!toDir.exists()) toDir.mkdirs();
                for (String file : files) {
                    copyAssetsRecursively(assetManager, fromAssetPath + "/" + file, new File(toDir, file));
                }
            }
        }

        // Copy stream utility
        private void copyStream(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read); // ✅ Correct

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                new SetupTask(this).execute();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE))
            switchSearch(MENU_SEARCH);
        else if (text.equals(DIGITS_SEARCH))
            switchSearch(DIGITS_SEARCH);
        else if (text.equals(PHONE_SEARCH))
            switchSearch(PHONE_SEARCH);
        else if (text.equals(FORECAST_SEARCH))
            switchSearch(FORECAST_SEARCH);
        else
            ((TextView) findViewById(R.id.result_text)).setText(text);
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KEYPHRASE);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);

        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
        ((TextView) findViewById(R.id.caption_text)).setText(caption);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create grammar-based search for digit recognition
        File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);

        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);

        // Phonetic search
        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
    }

    @Override
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    private InputStream stripWavHeader(File wavFile) throws IOException {
        FileInputStream fis = new FileInputStream(wavFile);
        DataInputStream dis = new DataInputStream(fis);

        // Skip 44-byte WAV header
        if (dis.skip(44) != 44) {
            throw new IOException("Invalid WAV file or header too small");
        }

        return dis;
    }

    /**
     * Skip a standard 44‑byte WAV header and return the raw PCM InputStream.
     */
    private InputStream openPcmStream(File wavFile) throws IOException {
        FileInputStream fis = new FileInputStream(wavFile);
        DataInputStream dis = new DataInputStream(fis);
        // Skip 44‑byte header
        if (dis.skipBytes(44) < 44) {
            throw new IOException("Invalid WAV header");
        }
        return dis;
    }

    /**
     * Feed a WAV file (16kHz, mono, 16‑bit PCM) into PocketSphinx’s Decoder.
     * This runs on a background thread and posts the final hypothesis to the UI.
     */
    private void recognizeFromWavFile(final File wavFile) {
        new Thread(() -> {
            try {

                // 1) Get the raw PCM stream
                InputStream pcmStream = openPcmStream(wavFile);

                // 2) Grab the low‑level Decoder from your SpeechRecognizer
                Decoder decoder = recognizer.getDecoder();
//                decoder.setSearch(KWS_SEARCH);
                // 3) Start a new utterance
                decoder.startUtt();

                // 4) Read chunks of 4096 bytes → 2048 shorts
                byte[] buffer = new byte[4096];
                short[] shortBuf = new short[2048];
                int bytesRead;
                while ((bytesRead = pcmStream.read(buffer)) > 0) {
                    int samples = bytesRead / 2;
                    for (int i = 0; i < samples; i++) {
                        // little‑endian conversion
                        shortBuf[i] = (short) ((buffer[2*i] & 0xff) | (buffer[2*i+1] << 8));
                    }
                    decoder.processRaw(shortBuf, samples, false, false);
                }
                pcmStream.close();

                // 5) End utterance and fetch hypothesis
                decoder.endUtt();
                Hypothesis hyp = decoder.hyp();
                final String result;
                if (hyp != null) {
                    result = hyp.getHypstr();
                } else {
                    result = "";
                }

                // 6) Post result back to UI
                runOnUiThread(() -> {
                    ((TextView) findViewById(R.id.result_text))
                            .setText("WAV → “" + result + "”");
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Recognition failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }


    @Override
    public void onTimeout() {
        switchSearch(MENU_SEARCH);
    }
}

