package com.demo.habitcheck.di

import androidx.lifecycle.ViewModel
import com.demo.habitcheck.ui.editnote.EditTaskViewModel
import com.demo.habitcheck.ui.notedone.NoteDoneViewModel
import com.demo.habitcheck.ui.home.HomeViewModel
import com.demo.habitcheck.ui.slideshow.SlideshowViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun homeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SlideshowViewModel::class)
    abstract fun slideshowViewModel(slideshowViewModel: SlideshowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteDoneViewModel::class)
    abstract fun galleryViewModel(noteDoneViewModel: NoteDoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditTaskViewModel::class)
    abstract fun editTaskViewModel(editTaskViewModel: EditTaskViewModel): ViewModel
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
