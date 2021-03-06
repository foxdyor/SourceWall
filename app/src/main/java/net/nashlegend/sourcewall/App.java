package net.nashlegend.sourcewall;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;

import net.nashlegend.sourcewall.db.BaseDB;
import net.nashlegend.sourcewall.db.gen.DaoMaster;
import net.nashlegend.sourcewall.db.gen.DaoSession;
import net.nashlegend.sourcewall.util.ImageUtils;

/**
 * Created by NashLegend on 2014/9/24 0024
 */
public class App extends Application {

    private static App application;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        LeakCanary.install(this);
        initImageLoader(this);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(ImageUtils.defaultImageOptions)
                .memoryCacheSizePercentage(33)
                .threadPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Application getApp() {
        return application;
    }

    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(application, BaseDB.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getDaoMaster().newSession();
        }
        return daoSession;
    }

    public static int getVersionInt() {
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getApp().getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
