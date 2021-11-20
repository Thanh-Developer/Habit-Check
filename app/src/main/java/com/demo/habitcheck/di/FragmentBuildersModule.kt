package com.demo.habitcheck.di

import com.demo.habitcheck.ui.gallery.GalleryFragment
import com.demo.habitcheck.ui.home.HomeFragment
import com.demo.habitcheck.ui.slideshow.SlideshowFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSlideshowFragment(): SlideshowFragment
}
