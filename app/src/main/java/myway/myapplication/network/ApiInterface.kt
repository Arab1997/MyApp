package myway.myapplication.network

import android.media.session.MediaSession
import io.reactivex.Single
import myway.myapplication.network.model.tasks.TaskPages
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiInterface {

    @GET("lesson_seasons")
    fun getTasks(): Single<TaskPages>

   /* @POST("auth/login")
    fun login(@Body body: LoginRequest): Single<MediaSession.Token>

    @POST("auth/register")
    fun register(@Body body: RegisterRequest): Single<RegisterResponse>*/
}
data class ErrorResp(val message: String, val errors: Any? = null)

 /*   @POST("auth/register")
    fun setPassword(@Body body: PasswordRequest): Single<SuccessResp>

    @POST("auth/logout")
    fun logout(): Single<MessageResp>

    @POST("auth/forgot_password")
    fun forgotPassword(@Body body: ForgotRequest): Single<ForgotResponse>

    @GET("user/chat/messages")
    fun getChats(): Single<ChatsResp>

    @POST("user/chat/send_message")
    @FormUrlEncoded
    fun sendMessageChats(@Field("text") text: String): Single<SendMessageChatResp>

    @POST("user/chat/read")
    @FormUrlEncoded
    fun readMessageChats(@Field("chat_items_ids") chat_items_ids: Int): Single<ReadMessageChatResp>

    @GET("user/info")
    fun getUser(): Single<UserResp>

    @POST("user/edit")
    fun editUser(@Body body: UserRequest): Single<UserResp>

    @POST("user/edit")
    fun updateUserPhoto(@Body requestBody: RequestBody): Single<UserResp>

    @GET("user/notifications")
    fun getUserNotifications(): Single<NotificationsResp>

    @GET("user/achievements")
    fun getUserAchievements(): Single<AchievementsResp>

    @GET("streams")
    fun getStreams(): Single<StreamsResp>

    @GET("streams/{id}/messages")
    fun getStreamMessages(@Path("id") id: Int): Single<StreamMessagesResp>

    @POST("streams/{id}/send_message")
    @FormUrlEncoded
    fun sendStreamMessages(
        @Path("id") id: Int, @Field("text") text: String
    ): Single<StreamMessageResp>

    @POST("streams/{id}/join")
    fun joinStream(@Path("id") id: Int): Single<SuccessResp>

    @FormUrlEncoded
    @POST("streams/{id}/direct_url")
    fun getStreamUrl(
        @Path("id") id: Int,
        @Field("html") html: String,
        @Field("script") script: String
    ): Single<StreamUrlResp>

    @GET("store_items/categories")
    fun getStoreCategories(): Single<StoreCategories>

    @GET("store_items")
    fun getStoreItems(): Single<StoreResp>

    @GET("store_items/orders_history")
    fun getUserOrdersHistory(): Single<OrderHistoryResp>

    @GET("user/lesson_day_items")
    fun getLessonsDay(): Single<LessonsDayResp>

    @GET("user/quiz/{id}")
    fun getQuiz(@Path("id") id: Int): Single<QuizResp>

    @POST("user/quiz/{id}/answers")
    fun sendQuizAnswers(
        @Path("id") id: Int, @Body body: QuizAnswerRequestList
    ): Single<SuccessResp>

    @GET("lesson_seasons")
    fun getLessonSeasons(): Single<SeasonsResp>

    @GET("user/lesson_seasons/{id}")
    fun getLessonSeason(@Path("id") id: Int): Single<SeasonResp>

    @Multipart
    @POST("user/lesson_items/{id}/upload_report")
    fun uploadReport(
        @Path("id") id: Int,
        @PartMap partMap: Map<String, String>,
        @Part photo: List<MultipartBody.Part>?,
        @Part video: List<MultipartBody.Part>?,
        @Part file: List<MultipartBody.Part>?
    ): Single<SuccessResp>

    @POST("user/lesson_items/{id}/lesson_complete")
    fun completeLesson(@Path("id") id: Int): Single<SuccessResp>

    // News
    @GET("news_items")
    fun getNews(
        @Query("start_date") start_date: String? = null,
        @Query("end_date") end_date: String? = null,
        @Query("tag_ids[]") tag_ids: ArrayList<Int>? = null
    ): Single<NewsResp>

    @GET("news_items/{id}")
    fun getNewsDetail(@Path("id") id: Int): Single<NewsDetailResp>

    @GET("news_items/{id}/comments")
    fun getNewsComments(@Path("id") id: Int): Single<NewsCommentsResp>

    @POST("news_items/{id}/comment")
    @FormUrlEncoded
    fun addCommentToNews(
        @Path("id") id: Int,
        @Field("text") comment: String
    ): Single<AddCommentNewsResp>

    @POST("news_items/{id}/like")
    fun likeNews(@Path("id") id: Int): Single<Like>

    @POST("news_items/{id}/share")
    fun shareNews(@Path("id") id: Int): Single<Share>

    @POST("news_items/{id}/bookmark")
    fun bookmarkNews(@Path("id") id: Int): Single<Bookmark>

    @GET("news_items/tags")
    fun getNewsTags(): Single<NewsTagResp>

    // Event
    @GET("events")
    fun getEvents(
        @Query("start_date") start_date: String? = null,
        @Query("end_date") end_date: String? = null,
        @Query("tag_ids[]") tag_ids: ArrayList<Int>? = null,
        @Query("city_ids[]") city_ids: ArrayList<Int>? = null
    ): Single<EventsResp>

    @GET("events/{id}")
    fun getEventsDetail(@Path("id") id: Int): Single<EventResp>

    @GET("events/{id}/comments")
    fun getEventsComments(@Path("id") id: Int): Single<EventsCommentsResp>

    @POST("events/{id}/comment")
    @FormUrlEncoded
    fun addCommentToEvent(
        @Path("id") id: Int,
        @Field("text") comment: String
    ): Single<AddCommentEventsResp>

    @POST("events/{id}/like")
    fun likeEvents(@Path("id") id: Int): Single<Like>

    @POST("events/{id}/share")
    fun shareEvents(@Path("id") id: Int): Single<Share>

    @GET("events/tags")
    fun getEventsTags(): Single<EventsTagResp>

    @GET("audio_playlists")
    fun getPlaylists(): Single<AudioPlaylistsResp>

    @GET("audio_playlists/{id}")
    fun getPlaylist(@Path("id") id: Int): Single<AudioPlaylistResp>

    @GET("audio_playlists/tags")
    fun getPlaylistTags(): Single<AudioTagResp>

    @POST("audio_playlists/{id}/bookmark")
    fun bookmarkPlaylist(@Path("id") id: Int): Single<Bookmark>

}

data class ErrorResp(val message: String, val errors: Any? = null)

data class MessageResp(val message: String)

data class Token(
    val access_token: String,
    val token_type: String,
    val unique_id: String
)

data class RegisterRequest(
    val phone: String,
    val code: String? = null
)

data class RegisterResponse(
    val success: Boolean,
    val needs_verification: Boolean?,
    val access_token: String?,
    val token_type: String?,
    val unique_id: String?,
    val message: String
)

data class ForgotRequest(
    val phone: String,
    val code: String? = null,
    val password: String? = null,
    val token: String? = null
)

data class ForgotResponse(
    val success: Boolean,
    val token: String?,
    val message: String
)

data class PasswordRequest(val password: String)

data class LoginRequest(
    val phone: String,
    val password: String
)

data class ReportRequest(
    val text: String,
    val photo: List<RequestBody>? = null,
    val video: List<RequestBody>? = null,
    val file: List<RequestBody>? = null
)*/