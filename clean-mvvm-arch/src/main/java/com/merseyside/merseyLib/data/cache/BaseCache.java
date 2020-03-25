package com.merseyside.merseyLib.data.cache;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.CallSuper;
import com.merseyside.merseyLib.data.cache.serializer.Serializer;
import com.merseyside.merseyLib.domain.executor.ThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.exceptions.UndeliverableException;

import java.io.File;
import java.lang.reflect.ParameterizedType;

public abstract class BaseCache<T> {

    private final String TAG = "BaseCache";

    private final File cacheDir;
    private final Serializer serializer;
    private final CacheFileManager fileManager;
    private final ThreadExecutor threadExecutor;


    private Class<T> persistentClass;

    public BaseCache(Context context, Serializer serializer,
              CacheFileManager fileManager, ThreadExecutor executor) {
        if (context == null || serializer == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        Context context1 = context.getApplicationContext();
        this.cacheDir = context1.getCacheDir();
        this.serializer = serializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;

        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public abstract String getCacheFilename();

    public abstract String getCacheDirName();

    public Observable<T> get() {
        return Observable.create(emitter -> {
            final File userEntityFile = this.buildFile();
            final String fileContent = this.fileManager.readFileContent(userEntityFile);
            final T entity = this.serializer.deserialize(fileContent, persistentClass);

            try {
                if (entity != null) {
                    emitter.onNext(entity);
                    emitter.onComplete();
                } else {
                    emitter.onError(new Exception());
                }
            } catch (UndeliverableException ignored) {}

        });
    }

    @CallSuper
    public boolean put(T entity) {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, buildFile()));
        if (entity != null) {
            final File cacheFile = this.buildFile();
            final String jsonString = this.serializer.serialize(entity, persistentClass);
            this.executeAsynchronously(new CacheWriter(this.fileManager, cacheFile, jsonString));
            return true;

        }
        return false;
    }

    public boolean isCached() {
        final File userEntityFile = this.buildFile();
        return this.fileManager.exists(userEntityFile);
    }

    public String getFullPath() {
        File file = buildFile();
        return file.getPath();
    }

    private File buildFile() {
        String dir = "";
        if (!TextUtils.isEmpty(getCacheDirName())) {
            File file = new File(this.cacheDir.getPath() + File.separator + getCacheDirName());
            if (!file.exists())
                file.mkdirs();
            dir = File.separator + getCacheDirName();
        }
        String fileNameBuilder = this.cacheDir.getPath() +
            dir +
            File.separator +
            getCacheFilename();

        return new File(fileNameBuilder);
    }

    void evict() {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, buildFile()));
    }

    private void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    private static class CacheWriter implements Runnable {
        private final CacheFileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        CacheWriter(CacheFileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }

    /**
     * {@link Runnable} class for evicting all the cached files
     */
    private static class CacheEvictor implements Runnable {
        private final CacheFileManager fileManager;
        private final File cacheFile;

        CacheEvictor(CacheFileManager fileManager, File cacheFile) {
            this.fileManager = fileManager;
            this.cacheFile = cacheFile;
        }

        @Override public void run() {
            this.fileManager.deleteFile(this.cacheFile);
        }
    }
}
