package com.demo.habitcheck.di

import com.demo.habitcheck.ui.editnote.EditTaskFragment
import com.demo.habitcheck.ui.notedone.NoteDoneFragment
import com.demo.habitcheck.ui.home.HomeFragment
import com.demo.habitcheck.ui.slideshow.SlideshowFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeNoteDoneFragment(): NoteDoneFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSlideshowFragment(): SlideshowFragment

    @ContributesAndroidInjector
    abstract fun contributeEditTaskFragment(): EditTaskFragment
}
