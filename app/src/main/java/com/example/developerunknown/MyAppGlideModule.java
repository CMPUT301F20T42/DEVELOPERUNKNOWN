package com.example.developerunknown;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.io.InputStream;

@GlideModule

/**
 * Image library module to handle application of photos
 */
public final class MyAppGlideModule extends AppGlideModule {

    @Override
    /**
     * // Register FirebaseImageLoader to handle StorageReference
     * @param context application environment
     * @param glide static interface for requests
     * @param registry manages registration for glide
     */
    public void registerComponents(Context context, Glide glide, Registry registry) {


        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }


}