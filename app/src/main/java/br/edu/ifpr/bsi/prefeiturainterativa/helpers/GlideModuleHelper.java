package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

import androidx.annotation.NonNull;

@GlideModule
public class GlideModuleHelper extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }
}
