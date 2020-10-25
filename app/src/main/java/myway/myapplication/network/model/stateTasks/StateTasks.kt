package myway.myapplication.network.model.stateTasks
import com.google.gson.annotations.SerializedName

data class StateTasks (

	@SerializedName("id") val id : Int,
	@SerializedName("name_ru") val name_ru : String,
	@SerializedName("name_uk") val name_uk : String,
	@SerializedName("name_uz") val name_uz : String,
	@SerializedName("name") val name : String,
	@SerializedName("description") val description : String,
	@SerializedName("css_icon") val css_icon : String,
	@SerializedName("color") val color : String
)