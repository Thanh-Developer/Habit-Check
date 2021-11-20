package com.demo.habitcheck.di

import dagger.Module

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class AppModule