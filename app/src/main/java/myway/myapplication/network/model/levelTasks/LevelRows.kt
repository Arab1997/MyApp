package myway.myapplication.network.model.levelTasks
import com.google.gson.annotations.SerializedName

data class LevelRows (

	@SerializedName("total") val total : Int,
	@SerializedName("rows") val rows : List<LevelTasks>
)