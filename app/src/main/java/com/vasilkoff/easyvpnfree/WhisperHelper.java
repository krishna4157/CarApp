//package com.vasilkoff.easyvpnfree;
//
//import java.io.IOException;
//import java.nio.file.Path;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.util.Locale;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.util.Locale;
//import io.github.ggerganov.whispercpp.WhisperCppJnaLibrary;
//
//
//import android.content.Context;
//import android.util.Log;
//import io.github.ggerganov.whispercpp.WhisperContext;
//import io.github.ggerganov.whispercpp.WhisperCpp;
//import java.io.File;
//
//public class WhisperHelper {
//    private static final String TAG = "WhisperHelper";
//    private final Context context;
//    private final WhisperCpp whisper;
//    private WhisperContext whisperContext;
//
//    public WhisperHelper(Context context) {
//        this.context = context.getApplicationContext();
//        System.loadLibrary("whisper-android");
//        this.whisper = new WhisperCpp();
//    }
//
//    public interface TranscriptionCallback {
//        void onTranscriptionComplete(String text);
//        void onError(String error);
//    }
//
//    public void transcribe(File wavFile, File modelFile, TranscriptionCallback callback) {
//        new Thread(() -> {
//            try {
//                if (!modelFile.exists()) {
//                    callback.onError("Model file not found");
//                    return;
//                }
//
//                whisperContext = whisper.modelDir(modelFile.getAbsolutePath());
//                if (whisperContext == null) {
//                    callback.onError("Failed to initialize whisper context");
//                    return;
//                }
//
//                if (!wavFile.exists()) {
//                    callback.onError("Audio file not found");
//                    whisper.whisper_free(whisperContext);
//                    return;
//                }
//
//                int result = whisper.whisper_full(whisperContext, new WhisperCpp.Params(), wavFile.getAbsolutePath());
//                if (result != 0) {
//                    callback.onError("Transcription failed with code: " + result);
//                    whisper.whisper_free(whisperContext);
//                    return;
//                }
//
//                StringBuilder transcription = new StringBuilder();
//                int segments = whisper.whisper_full_n_segments(whisperContext);
//                for (int i = 0; i < segments; i++) {
//                    transcription.append(whisper.whisper_full_get_segment_text(whisperContext, i));
//                }
//
//                callback.onTranscriptionComplete(transcription.toString());
//                whisper.whisper_free(whisperContext);
//
//            } catch (Exception e) {
//                Log.e(TAG, "Transcription error", e);
//                callback.onError(e.getMessage());
//                if (whisperContext != null) {
//                    whisper.whisper_free(whisperContext);
//                }
//            }
//        }).start();
//    }
//
//    public void release() {
//        if (whisperContext != null) {
//            whisper.whisper_free(whisperContext);
//            whisperContext = null;
//        }
//    }
//}
//
