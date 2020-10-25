package myway.myapplication.di

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import myway.myapplication.base.BaseViewModel
import myway.myapplication.network.model.levelTasks.LevelTasks
import myway.myapplication.network.model.stateTasks.StateTasks
import myway.myapplication.network.model.tasks.TaskPages
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val viewModelModule = module {

    fun provideMutableLiveData() = MutableLiveData<Any>()

    viewModel { BaseViewModel(get(), get(), get()) }

    single { provideMutableLiveData() }
   // single(named("levelTasks")) { provideMutableLiveData() }
    single(named("levelTasks")) { MutableLiveData<LevelTasks>()}
    single(named("stateTasks")) {  MutableLiveData<StateTasks>() }
    single(named("tasks")) { MutableLiveData<ArrayList<TaskPages>>() }
    single(named("TaskTypes")) { MutableLiveData<ArrayList<TaskTypes>>() }
}

val networkModule = module {

    fun provideGson() = Gson()

    single { provideGson() }
}

/*
val sharedPrefModule = module {

    factory { PreferenceHelper.customPrefs(get(), "Qweep") }

    factory { SharedManager(get(), get(), get()) }
}
*/
