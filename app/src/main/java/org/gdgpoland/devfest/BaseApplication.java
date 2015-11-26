package org.gdgpoland.devfest;

import android.app.Application;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.squareup.picasso.Transformation;

import org.gdgpoland.devfest.utils.ImageManager;

import io.fabric.sdk.android.Fabric;

/**
 * Basic implementation of the Application class
 * @author Arnaud Camus
 */
public class BaseApplication extends Application {

    /**
     * A {@link com.squareup.picasso.Transformation} to crop
     * and round the speakers' pictures.
     */
    public Transformation mPicassoTransformation;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        mPicassoTransformation = new Transformation(){
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap bp = ImageManager.cropBitmapToCircle(source, BaseApplication.this);
                if (bp != source) {
                    source.recycle();
                }
                return bp;
            }

            @Override
            public String key() {
                return "rounded";
            }
        };

        Parse.initialize(this, "JvBfCNOYeCxhfYCVzKlEDOOz4ZJr37GyJTPjfU39", "zlCXteBWUxiDn7qRnNHKNpLRPQigjc38yNa9uLVd");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
