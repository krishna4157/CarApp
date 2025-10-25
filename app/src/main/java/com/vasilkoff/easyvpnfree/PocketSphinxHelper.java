package com.vasilkoff.easyvpnfree;

import android.content.Context;
import android.os.AsyncTask;

//import com.vasilkoff.easyvpnfree.Assets;
//import com.vasilkoff.easyvpnfree.Decoder;
//import com.vasilkoff.easyvpnfree.Hypothesis;
//import com.vasilkoff.easyvpnfree.RecognitionListener;
//import com.vasilkoff.easyvpnfree.SpeechRecognizer;
//import com.vasilkoff.easyvpnfree.SpeechRecognizerSetup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//no error didnt test
//public class PocketSphinxHelper implements RecognitionListener {
//    private static final String KWS_SEARCH = "wakeup"; // Keyword search name
//    private SpeechRecognizer recognizer;
//    private final Context context;
//    private final ResultListener resultListener;
//
//    public interface ResultListener {
//        void onPartialResult(String text);
//        void onFinalResult(String text);
//        void onError(String error);
//    }
//
//    public PocketSphinxHelper(Context context, ResultListener listener) {
//        this.context = context;
//        this.resultListener = listener;
//    }
//
//    public void setupRecognizer() {
//        new AsyncTask<Void, Void, Exception>() {
//            @Override
//            protected Exception doInBackground(Void... params) {
//                try {
//                    // Copy assets (models) to app storage
//                    Assets assets = new Assets(context);
//                    File assetDir = assets.syncAssets();
//
//                    // Configure recognizer with acoustic model + dictionary
//                    recognizer = SpeechRecognizerSetup.defaultSetup()
//                            .setAcousticModel(new File(assetDir, "en-us-ptm")) // Acoustic model
//                            .setDictionary(new File(assetDir, "cmudict-en-us.dict")) // Dictionary
//                            .setRawLogDir(assetDir) // Disable logs for performance
//                            .setKeywordThreshold(1e-7f) // Adjust sensitivity
//                            .getRecognizer();
//
//                    recognizer.addListener(PocketSphinxHelper.this);
//
//                    // Add keyword search (for continuous recognition)
//                    recognizer.addKeyphraseSearch(KWS_SEARCH, "oh mighty computer");
//                } catch (IOException e) {
//                    return e;
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Exception ex) {
//                if (ex != null) {
//                    resultListener.onError("Recognizer init failed: " + ex.getMessage());
//                }
//            }
//        }.execute();
//    }
//
//    public void startListening() {
//        if (recognizer != null) {
//            recognizer.startListening(KWS_SEARCH);
//        }
//    }
//
//    public void stopListening() {
//        if (recognizer != null) {
//            recognizer.stop();
//        }
//    }
//
//    @Override
//    public void onBeginningOfSpeech() {
//
//    }
//
//    @Override
//    public void onEndOfSpeech() {
//
//    }
//
//    // RecognitionListener callbacks
//    @Override
//    public void onPartialResult(Hypothesis hypothesis) {
//        if (hypothesis == null) return;
//        resultListener.onPartialResult(hypothesis.getHypstr());
//    }
//
//    @Override
//    public void onResult(Hypothesis hypothesis) {
//        resultListener.onFinalResult(hypothesis != null ? hypothesis.getHypstr() : "");
//    }
//
//    @Override
//    public void onError(Exception e) {
//        resultListener.onError("Recognition error: " + e.getMessage());
//    }
//
//    @Override
//    public void onTimeout() {
//
//    }
//
//    public void shutdown() {
//        if (recognizer != null) {
//            recognizer.cancel();
//            recognizer.shutdown();
//        }
//    }
//}

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class PocketSphinxHelper implements RecognitionListener {
    private static final String TAG = "PocketSphinxHelper";
    private static final String KWS_SEARCH = "Hello";
    private static final String FREEFORM_SEARCH = "hello";

    private SpeechRecognizer recognizer;
    private final Context context;
    private final ResultListener resultListener;
    private File assetsDir;

    public interface ResultListener {
        void onResult(Hypothesis hypothesis);

        void onPartialResult(String text);
        void onFinalResult(String text);
        void onError(String error);
    }

    public PocketSphinxHelper(Context context, ResultListener listener) {
        this.context = context;
        this.resultListener = listener;
    }

    public void setupRecognizer() {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    // Copy assets to app storage
                    Assets assets = new Assets(context);
                    assetsDir = assets.syncAssets();
                    File modelsDir = new File(assetsDir, "models");
                    Log.i(TAG, "Assets synced to: " + assetsDir.getAbsolutePath());

                    // Configure recognizer
//                    recognizer = SpeechRecognizerSetup.defaultSetup()
//                            .setAcousticModel(new File(assetsDir, "en-us-ptm"))
//                            .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
//                            .setRawLogDir(assetsDir)
//                            .setKeywordThreshold(1e-7f)
//                            .getRecognizer();
                    recognizer = SpeechRecognizerSetup.defaultSetup()
                            .setAcousticModel(new File(modelsDir, "hmm/en-us-semi"))
                            .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
                            .setRawLogDir(assetsDir).setKeywordThreshold(1e-40f)
                            .getRecognizer();
//                    recognizer.addListener(this);
                    recognizer.addListener(PocketSphinxHelper.this);

                    // Add keyword search
                    recognizer.addKeyphraseSearch(KWS_SEARCH, "Hello");

                    // Add freeform search
                    File languageModel = new File(assetsDir, "en-us.lm.bin");
                    recognizer.addNgramSearch(FREEFORM_SEARCH, languageModel);

                    Log.i(TAG, "Recognizer initialized successfully");
                } catch (IOException e) {
                    Log.e(TAG, "Recognizer init failed: " + e.getMessage());
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception ex) {
                if (ex != null) {
                    resultListener.onError("Recognizer init failed: " + ex.getMessage());
                }
            }
        }.execute();
    }

    // New method to process PCM files
//    public void recognizePcmFile(final File pcmFile) {
//        if (recognizer == null) {
//            resultListener.onError("Recognizer not initialized");
//            return;
//        }
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    // Stop any ongoing recognition
//                    if (recognizer.getSearchName().equals(FREEFORM_SEARCH)) {
//                        recognizer.stop();
//                    }
//
//                    // Start freeform recognition
//                    recognizer.startListening(FREEFORM_SEARCH);
//
//                    // Feed audio data in chunks
//                    FileInputStream fis = new FileInputStream(pcmFile);
//                    short[] buffer = new short[1024];
//                    int bytesRead;
//
//                    while ((bytesRead = fis.read()) > 0) {
//                        // Process the raw audio chunk
//                        recognizer.getDecoder().processRaw(buffer, bytesRead, false, false);
//                    }
//                    fis.close();
//
//                    // Finish processing
//                    recognizer.stop();
//
//                } catch (IOException e) {
//                    Log.e(TAG, "File recognition error: " + e.getMessage());
//                    resultListener.onError("File processing failed: " + e.getMessage());
//                }
//                return null;
//            }
//        }.execute();
//    }

    public void recognizePcmFile(File pcmFile) {
        if (recognizer == null) {
            resultListener.onError("Recognizer not initialized");
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    recognizer.startListening(FREEFORM_SEARCH);
                    Decoder decoder = recognizer.getDecoder();

                    FileInputStream fis = new FileInputStream(pcmFile);
                    byte[] byteBuffer = new byte[4096];
                    short[] shortBuffer = new short[2048]; // Half size because 2 bytes per short

                    decoder.startUtt();  // Begin utterance

                    int bytesRead;
                    while ((bytesRead = fis.read(byteBuffer)) != -1) {
                        // Convert byte[] to short[]
                        for (int i = 0; i < bytesRead / 2; i++) {
                            // Little endian
                            shortBuffer[i] = (short) ((byteBuffer[i * 2] & 0xFF) | (byteBuffer[i * 2 + 1] << 8));
                        }

                        recognizer.getDecoder().processRaw(shortBuffer, bytesRead / 2, false, false);
                    }

                    fis.close();

                    decoder.endUtt(); // End utterance

                    final Hypothesis result = recognizer.getDecoder().hyp();
                    if (result != null) {
                        resultListener.onFinalResult(result.getHypstr());
                        Log.i(TAG, "Final result: " + result.getHypstr());
                    } else {
                        resultListener.onFinalResult("");
                    }

                } catch (IOException e) {
                    Log.e(TAG, "PCM file error: " + e.getMessage());
                    resultListener.onError("File error: " + e.getMessage());
                }
                return null;
            }
        }.execute();
    }


    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            resultListener.onPartialResult(text);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            resultListener.onFinalResult(text);
        } else {
            resultListener.onFinalResult("");
        }
    }

    @Override
    public void onError(Exception e) {
        resultListener.onError("Recognition error: " + e.getMessage());
    }

    // Other required methods (can be empty)
    @Override public void onBeginningOfSpeech() {}
    @Override public void onEndOfSpeech() {}
    @Override public void onTimeout() {}

    public void shutdown() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    public void convertWavToPcm(File wavFile, File pcmFile) throws IOException {
        final int WAV_HEADER_SIZE = 44;

        FileInputStream fis = new FileInputStream(wavFile);
        FileOutputStream fos = new FileOutputStream(pcmFile);

        // Skip the 44-byte WAV header
        fis.skip(WAV_HEADER_SIZE);

        byte[] buffer = new byte[1024];
        int bytesRead;

        // Write remaining PCM data to output
        while ((bytesRead = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        fis.close();
        fos.close();
    }
}