/*
 * VideoDialogPlugin
 * MIT License. See http://opensource.org/licenses/alphabetical for full text.
 *
 * Based on the amazing work by macdonst: https://github.com/macdonst/VideoPlayer
 * Copyright (c) 2013, Steffen Müller, Müller & Sohn Digitalmanufaktur GmbH
 *
 */

package com.mllrsohn.videodialog;

import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;

import java.io.*;
import android.net.Uri;
import android.view.WindowManager.LayoutParams;

import java.io.IOException;

public class VideoDialogPlugin extends CordovaPlugin {
    private static final String ASSETS = "file:///android_asset/";
    private Dialog dialog;
    private VideoView mVideoView;
    private boolean loopVideo = false;
    private String path = "";
    private Uri uri;
    public CallbackContext cbContext;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        JSONObject params;

        try {
            if (action.equals("play")) {
                // If the VideoDiaolog is already open then throw an error
                if (dialog != null && dialog.isShowing()) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "VideoDialog is already open"));
                    return false;
                } else {
                    params = args.getJSONObject(0);
                    playVideo(params, callbackContext);
                    return true;
                }

            } else {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, "VideoDialog Action is not implemented"));
                return false;
            }
        } catch (JSONException e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION, "UTF-8 error."));
        }
        return false;
    }

    private void playVideo(JSONObject params, final CallbackContext callbackContext) throws JSONException {

        loopVideo = params.optBoolean("loop", true);
        path = params.optString("url");

        uri = Uri.parse(path);

        if(path.contains(ASSETS)) {
            try {
                String filepath = path.replace(ASSETS, "");
                String filename = filepath.substring(filepath.lastIndexOf("/")+1, filepath.length());
                File fp = new File(this.cordova.getActivity().getFilesDir() + "/" + filename);

                if (!fp.exists()) {
                    this.copy(filepath, filename);
                }
                uri = Uri.parse("file://" + this.cordova.getActivity().getFilesDir() + "/" + filename);
            } catch (IOException e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION));
            }

        }

        // Create dialog in new thread
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {

                // Set Basic Dialog
                dialog = new Dialog((Context) cordova.getActivity(), android.R.style.Theme_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                // Layout View
                RelativeLayout main = new RelativeLayout((Context) cordova.getActivity());
                main.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));


                // Video View
                mVideoView = new VideoView((Context) cordova.getActivity());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                mVideoView.setLayoutParams(lp);

                mVideoView.setVideoPath(uri.toString());
                mVideoView.start();
                main.addView(mVideoView);

                dialog.setContentView(main);
                dialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
                dialog.show();

                // Close on touch
                mVideoView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "stopped"));
                        dialog.dismiss();
                        return true;
                    }
                });

                // Set Looping
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(loopVideo);
                    }
                });

                // On Completion
                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaplayer) {
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Done"));
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    /**
     * Closes the dialog
     */
    private void closeDialog() {
        if (dialog != null) {
            //this.webview.stopLoading();
            dialog.dismiss();
        }
    }

    private void copy(String fileFrom, String fileTo) throws IOException {
        // get file to be copied from assets
        InputStream in = this.cordova.getActivity().getAssets().open(fileFrom);
        // get file where copied too, in internal storage.
        // must be MODE_WORLD_READABLE or Android can't play it
        FileOutputStream out = this.cordova.getActivity().openFileOutput(fileTo, Context.MODE_WORLD_READABLE);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        in.close();
        out.close();
    }
}

