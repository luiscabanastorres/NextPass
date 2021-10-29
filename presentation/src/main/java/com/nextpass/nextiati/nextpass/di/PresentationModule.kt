package com.nextpass.nextiati.nextpass.di

import com.nextpass.nextiati.nextpass.errorparser.ErrorParser
import com.nextpass.nextiati.nextpass.errorparser.ErrorParserImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindErrorParser(impl: ErrorParserImpl): ErrorParser

}