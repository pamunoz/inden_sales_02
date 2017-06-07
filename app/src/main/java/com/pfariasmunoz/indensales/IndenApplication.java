package com.pfariasmunoz.indensales;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Pablo Farias on 07-06-17.
 */

public class IndenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Este inicia la libreria de Timber para logging
        // y lo niniciamos con el tipo e log que queremos.
        Timber.plant(new Timber.DebugTree());
    }
}
