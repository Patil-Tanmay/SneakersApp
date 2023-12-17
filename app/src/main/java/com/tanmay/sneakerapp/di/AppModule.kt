package com.tanmay.sneakerapp.di

import android.content.Context
import androidx.room.Room
import com.tanmay.sneakerapp.db.CartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext context: Context):
            Context = context


    @Provides
    @Singleton
    fun provideCartDb(@ApplicationContext context: Context):
            CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "CartDatabase"
        ).build()
    }

}
