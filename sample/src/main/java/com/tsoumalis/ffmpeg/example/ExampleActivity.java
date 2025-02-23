package com.tsoumalis.ffmpeg.example;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.tsoumalis.ffmpeg.ExecuteBinaryResponseHandler;
import com.tsoumalis.ffmpeg.FFmpeg;
import com.tsoumalis.ffmpeg.FFtask;
import timber.log.Timber;

/**
 * Created by Brian on 11-12-17.
 */
public class ExampleActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FFmpeg.getInstance(this).isSupported()) {
            // ffmpeg is supported
            versionFFmpeg();
            //ffmpegTestTaskQuit();
        } else {
            // ffmpeg is not supported
            Timber.e("ffmpeg not supported!");
        }

    }

    private void versionFFmpeg() {
        FFmpeg.getInstance(this).execute(new String[]{"-version"}, new ExecuteBinaryResponseHandler() {
            @Override
            public void onSuccess(String message) {
                Timber.d(message);
            }

            @Override
            public void onProgress(String message) {
                Timber.d(message);
            }
        });

    }

    private void ffmpegTestTaskQuit() {
        String[] command = {"-i", "input.mp4", "output.mov"};

        final FFtask task = FFmpeg.getInstance(this).execute(command, new ExecuteBinaryResponseHandler() {
            @Override
            public void onStart() {
                Timber.d( "on start");
            }

            @Override
            public void onFinish() {
                Timber.d("on finish");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Timber.d("RESTART RENDERING");
                        ffmpegTestTaskQuit();
                    }
                }, 5000);
            }

            @Override
            public void onSuccess(String message) {
                Timber.d(message);
            }

            @Override
            public void onProgress(String message) {
                Timber.d(message);
            }

            @Override
            public void onFailure(String message) {
                Timber.d(message);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Timber.d( "STOPPING THE RENDERING!");
                task.sendQuitSignal();
            }
        }, 8000);
    }
}
