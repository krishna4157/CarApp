package com.vasilkoff.easyvpnfree;

import android.app.Activity;
import android.app.PendingIntent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

//import com.serenegiant.usb.USBMonitor;
//import com.serenegiant.usbcameracommon.UVCCameraHandler;
//import com.serenegiant.widget.CameraViewInterface;

public class SpeechToTextActivityForCamera extends Activity {


//    private static final String ACTION_USB_PERMISSION = "com.example.USB_PERMISSION";
//    private USBMonitor mUSBMonitor;
//    private UVCCameraHandler mCameraHandler;
//    private CameraViewInterface mUVCCameraView;
//    private UsbManager usbManager;
//    private PendingIntent permissionIntent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
////        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
////
////        mUVCCameraView = findViewById(R.id.camera_view);
////        mUVCCameraView.setAspectRatio(UVCCamera.DEFAULT_PREVIEW_WIDTH / (float) UVCCamera.DEFAULT_PREVIEW_HEIGHT);
////
////        mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);
////        mCameraHandler = UVCCameraHandler.createHandler(this, mUVCCameraView, 1,
////                UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.FRAME_FORMAT_MJPEG);
////
////        findViewById(R.id.capture_button).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (mCameraHandler != null && !mCameraHandler.isOpened()) {
////                    mUSBMonitor.requestPermission();
////                } else {
////                    mCameraHandler.captureStill();
////                }
////            }
////        });
//    }
//
////    private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener =
////            new USBMonitor.OnDeviceConnectListener() {
////                @Override
////                public void onAttach(UsbDevice device) {
////                    Log.d("USB", "USB Device attached");
////                    usbManager.requestPermission(device, permissionIntent);
////                }
////
////                @Override
////                public void onDettach(UsbDevice device) {
////                    Log.d("USB", "USB Device detached");
////                }
////
////                @Override
////                public void onConnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
////                    mCameraHandler.open(ctrlBlock);
////                    mCameraHandler.startPreview();
////                }
////
////                @Override
////                public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
////                    if (mCameraHandler != null) {
////                        mCameraHandler.stopPreview();
////                    }
////                }
////
////                @Override
////                public void onCancel(UsbDevice device) {
////                    Log.d("USB", "Permission denied for USB device");
////                }
////            };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mUSBMonitor.register();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mUSBMonitor.unregister();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mCameraHandler.release();
//        mUSBMonitor.destroy();
//    }
}
