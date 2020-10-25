package myway.myapplication.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.snatap.myway.utils.network.Errors
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import myway.myapplication.R
import myway.myapplication.app.Constants
import myway.myapplication.network.ApiInterface
import myway.myapplication.network.ErrorResp
import myway.myapplication.network.RetrofitClient
import myway.myapplication.network.model.tasks.TaskPages
import myway.myapplication.utils.extensions.loge
import myway.myapplication.utils.extensions.logi
import myway.myapplication.utils.extensions.toast
import myway.myapplication.utils.pereferences.SharedManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import retrofit2.HttpException

open class BaseViewModel(
    private val gson: Gson,
    private val context: Context,
    val sharedManager: SharedManager
) : ViewModel(), KoinComponent {

    @LayoutRes
    var parentLayoutId: Int = 0

    @LayoutRes
    var navLayoutId: Int = 0

    val data: MutableLiveData<Any> by inject()
    val shared: MutableLiveData<Any> by inject(named("sharedLive"))
    val error: MutableLiveData<ErrorResp> by inject(named("errorLive"))
    val taskPages: MutableLiveData<ArrayList<TaskPages>> by inject(named("taskPages"))
    /* val news: MutableLiveData<ArrayList<News>> by inject(named("news"))
     val events: MutableLiveData<ArrayList<Event>> by inject(named("events"))*/


    private val api = RetrofitClient
        .getRetrofit(Constants.BASE_URL, context, sharedManager, gson)
        .create(ApiInterface::class.java)

    private val compositeDisposable = CompositeDisposable()


    @SuppressLint("CheckResult")
    fun newThread(action: () -> Unit) {
        Observable.fromCallable { action() }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, { e ->
                e.printStackTrace()
                parseError(e)
            })
    }

    private fun parseError(e: Throwable?) {
        var message = context.resources.getString(R.string.smth_wrong)
        if (e != null && e.localizedMessage != null) {
            loge(e.localizedMessage)
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                errorBody?.let {
                    try {
                        loge(it)
                        val errors = it.split(":")
                            .filter { it.contains("[") }
                        val errorsString = if (errors.isNotEmpty()) {
                            errors.toString()
                                .replace("[", "")
                                .replace(",", "\n")
                                .replace("]", "")
                                .replace("{", "")
                                .replace("}", "")
                                .replace("\"", "")
                        } else {
                            val resp = it.split(":")
                            if (resp.size >= 2) resp[1].replace("{", "")
                                .replace("}", "")
                                .replace("\"", "")
                            else it
                        }

                        message = if (errorsString.isEmpty())
                            context.resources.getString(R.string.smth_wrong)
                        else errorsString

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else message = Errors.traceErrors(e, context)
        }
        toast(context, message)

    }

    fun fetchData() {
        logi("Current token : ")
        getTasks()
        /* getNews()
         getEvents()
         getStreams()
         getUserAchievements()
         getUserNotifications()
         getStoreItems()
         getLessonsDay()
         getLessonSeasons()
         getPlaylists()*/

    }
    fun getTasks() = compositeDisposable.add(
        api.getTasks().observeAndSubscribe()
            .subscribe({
                if (it.success) taskPages.postValue(ArrayList(it.task_pages))
            }, {
                parseError(it)
            })
    )
}

fun <T> Single<T>.observeAndSubscribe() =
    subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())


/* fun getNews(
     start_date: String? = null,
     end_date: String? = null,
     tag_ids: ArrayList<Int>? = null
 ) = compositeDisposable.add(
     api.getNews(start_date, end_date, tag_ids).observeAndSubscribe()
         .subscribe({
             if (it.success) news.postValue(it.news_items)
         }, {
             parseError(it)
         })
 )

 fun getNewsDetail(newsId: Int) = compositeDisposable.add(
     api.getNewsDetail(newsId).observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun getNewsTags() = compositeDisposable.add(
     api.getNewsTags().observeAndSubscribe()
         .subscribe({
             if (it.success) tags.postValue(ArrayList(it.news_item_tags))
         }, {
             parseError(it)
         })
 )

 fun getNewsComments(newsId: Int) = compositeDisposable.add(
     api.getNewsComments(newsId).observeAndSubscribe()
         .subscribe({
             if (it.success) commentNews.postValue(ArrayList(it.news_item_comments))
         }, {
             parseError(it)
         })
 )

 fun addCommentToNews(newsId: Int, comment: String) = compositeDisposable.add(
     api.addCommentToNews(newsId, comment).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getNewsComments(newsId)
                 getNewsDetail(newsId)
             }
         }, {
             parseError(it)
         })
 )

 fun likeNews(newsId: Int) = compositeDisposable.add(
     api.likeNews(newsId).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getNews()
                 getNewsDetail(newsId)
             }
         }, {
             parseError(it)
         })
 )

 fun shareNews(newsId: Int) = compositeDisposable.add(
     api.shareNews(newsId).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getNews()
                 getNewsDetail(newsId)
             }
         }, {
             parseError(it)
         })
 )

 fun bookmarkNews(newsId: Int) = compositeDisposable.add(
     api.bookmarkNews(newsId).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getNews()
                 getNewsDetail(newsId)
             }
         }, {
             parseError(it)
         })
 )

 fun getChats() = compositeDisposable.add(
     api.getChats().observeAndSubscribe()
         .subscribe({
             if (it.success) chats.postValue(ArrayList(it.chat_items))
         }, {
             parseError(it)
         })
 )

 fun sendMessageChats(text: String) = compositeDisposable.add(
     api.sendMessageChats(text).observeAndSubscribe()
         .subscribe({
             if (it.success) getChats()
         }, {
             parseError(it)
         })
 )

 fun readMessageChats(chatId: Int) = compositeDisposable.add(
     api.readMessageChats(chatId).observeAndSubscribe()
         .subscribe({
             if (it.success) getChats()
         }, {
             parseError(it)
         })
 )

 fun getUser() = compositeDisposable.add(
     api.getUser().observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 user.postValue(it.user)
                 sharedManager.user = it.user
             }
         }, {
             parseError(it)
         })
 )

 fun editUser(body: UserRequest) = compositeDisposable.add(
     api.editUser(body).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 user.postValue(it.user)
                 sharedManager.user = it.user
             }
         }, {
             parseError(it)
         })
 )

 fun updateUserPhoto(body: RequestBody) = compositeDisposable.add(
     api.updateUserPhoto(body)
         .observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 user.postValue(it.user)
                 sharedManager.user = it.user
             }
         }, {
             data.value = false
             parseError(it)
         })
 )

 fun getUserNotifications() = compositeDisposable.add(
     api.getUserNotifications().observeAndSubscribe()
         .subscribe({
             if (it.success) notifications.postValue(ArrayList(it.user_notifications))
         }, {
             parseError(it)
         })
 )

 fun getUserAchievements() = compositeDisposable.add(
     api.getUserAchievements().observeAndSubscribe()
         .subscribe({
             if (it.success) achievements.postValue(ArrayList(it.user_achievements))
         }, {
             parseError(it)
         })
 )

 fun getStreams() = compositeDisposable.add(
     api.getStreams().observeAndSubscribe()
         .subscribe({
             if (it.success) streams.postValue(ArrayList(it.streams))
         }, {
             parseError(it)
         })
 )

 fun getStreamMessages(streamId: Int) = compositeDisposable.add(
     api.getStreamMessages(streamId).observeAndSubscribe()
         .subscribe({
             if (it.success) streamsMessages.postValue(ArrayList(it.stream_chat_items))
         }, {
             parseError(it)
         })
 )

 fun sendStreamMessage(streamId: Int, message: String) = compositeDisposable.add(
     api.sendStreamMessages(streamId, message).observeAndSubscribe()
         .subscribe({
             if (it.success) getStreamMessages(streamId)
         }, {
             parseError(it)
         })
 )

 fun joinStream(streamId: Int) = compositeDisposable.add(
     api.joinStream(streamId).observeAndSubscribe()
         .subscribe({
         }, {
             parseError(it)
         })
 )

 fun getStreamUrl(streamId: Int, html: String, script: String) = compositeDisposable.add(
     api.getStreamUrl(streamId, html, script).observeAndSubscribe()
         .subscribe({
             data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun getStoreItems() = compositeDisposable.add(
     api.getStoreItems().observeAndSubscribe()
         .subscribe({
             if (it.success) stores.postValue(ArrayList(it.store_items))
         }, {
             parseError(it)
         })
 )

 fun getUserOrdersHistory() = compositeDisposable.add(
     api.getUserOrdersHistory().observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun getStoreCategories() = compositeDisposable.add(
     api.getStoreCategories().observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun getLessonsDay() = compositeDisposable.add(
     api.getLessonsDay().observeAndSubscribe()
         .subscribe({
             if (it.success) lessonsDay.postValue(ArrayList(it.lesson_day_items))
         }, {
             parseError(it)
         })
 )

 fun getQuiz(id: Int) = compositeDisposable.add(
     api.getQuiz(id).observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun sendQuizAnswers(id: Int, answers: ArrayList<QuizAnswerRequest>) = compositeDisposable.add(
     api.sendQuizAnswers(id, QuizAnswerRequestList(answers)).observeAndSubscribe()
         .subscribe({
             data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun getLessonSeasons() = compositeDisposable.add(
     api.getLessonSeasons().observeAndSubscribe()
         .subscribe({
             if (it.success) lessonSeasons.postValue(ArrayList(it.lesson_seasons))
         }, {
             parseError(it)
         })
 )

 fun getLessonSeason(id: Int) = compositeDisposable.add(
     api.getLessonSeason(id).observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun uploadReport(
     id: Int,
     text: String,
     photo: List<MultipartBody.Part>?,
     video: List<MultipartBody.Part>?,
     file: List<MultipartBody.Part>?
 ) = compositeDisposable.add(
     api.uploadReport(id, mapOf("text" to text), photo, video, file)
         .observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             data.value = false
             parseError(it)
         })
 )

 fun completeLesson(id: Int) = compositeDisposable.add(
     api.completeLesson(id).observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it) // todo
         }, {
             data.value = false
             parseError(it)
         })
 )

 fun getEvents(
     start_date: String? = null,
     end_date: String? = null,
     tag_ids: ArrayList<Int>? = null,
     city_ids: ArrayList<Int>? = null
 ) = compositeDisposable.add(
     api.getEvents(start_date, end_date, tag_ids, city_ids).observeAndSubscribe()
         .subscribe({
             if (it.success) events.postValue(ArrayList(it.events))
         }, {
             parseError(it)
         })
 )

 fun getEventsDetail(id: Int) = compositeDisposable.add(
     api.getEventsDetail(id).observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun getEventsComments(eventsId: Int) = compositeDisposable.add(
     api.getEventsComments(eventsId).observeAndSubscribe()
         .subscribe({
             if (it.success) commentEvents.postValue(ArrayList(it.event_comments))
         }, {
             parseError(it)
         })
 )

 fun addCommentToEvent(eventsId: Int, comment: String) = compositeDisposable.add(
     api.addCommentToEvent(eventsId, comment).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getEventsComments(eventsId)
                 getEventsDetail(eventsId)
             }
         }, {
             parseError(it)
         })
 )

 fun likeEvents(id: Int) = compositeDisposable.add(
     api.likeEvents(id).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getEvents()
                 getEventsDetail(id)
             }
         }, {
             parseError(it)
         })
 )

 fun shareEvents(id: Int) = compositeDisposable.add(
     api.shareEvents(id).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getEvents()
                 getEventsDetail(id)
             }
         }, {
             parseError(it)
         })
 )

 fun getEventsTags() = compositeDisposable.add(
     api.getEventsTags().observeAndSubscribe()
         .subscribe({
             if (it.success) tags.postValue(ArrayList(it.event_tags))
         }, {
             parseError(it)
         })
 )

 fun getPlaylists() = compositeDisposable.add(
     api.getPlaylists().observeAndSubscribe()
         .subscribe({
             if (it.success) playlists.postValue(ArrayList(it.audio_playlists))
         }, {
             parseError(it)
         })
 )

 fun getPlaylist(id: Int) = compositeDisposable.add(
     api.getPlaylist(id).observeAndSubscribe()
         .subscribe({
             if (it.success) data.postValue(it)
         }, {
             parseError(it)
         })
 )

 fun getPlaylistTags() = compositeDisposable.add(
     api.getPlaylistTags().observeAndSubscribe()
         .subscribe({
             if (it.success) tags.postValue(ArrayList(it.audio_playlist_tags))
         }, {
             parseError(it)
         })
 )

 fun bookmarkPlaylist(id: Int) = compositeDisposable.add(
     api.bookmarkPlaylist(id).observeAndSubscribe()
         .subscribe({
             if (it.success) {
                 data.postValue(it)
                 getPlaylists()
                 getPlaylist(id)
             }
         }, {
             parseError(it)
         })
 )

 override fun onCleared() {
     super.onCleared()
     compositeDisposable.clear()
 }

}
fun <T> Single<T>.observeAndSubscribe() =
 subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

*/

