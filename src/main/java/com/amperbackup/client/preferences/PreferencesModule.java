package com.amperbackup.client.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.amperbackup.client.account.CurrentAccountProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public AppPreferences appPreferences(Context context,
                                         SharedPreferences sharedPreferences,
                                         CurrentAccountProvider currentAccountProvider) {
        return new AppPreferencesImpl(context, sharedPreferences, currentAccountProvider);
    }
}
