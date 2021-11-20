package com.demo.habitcheck.di

import com.demo.habitcheck.ui.addnote.AddNoteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class AddNoteActivityModule {
    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class
        ]
    )
    abstract fun contributeAddNoteActivity(): AddNoteActivity
}
