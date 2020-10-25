package myway.myapplication.network.model.stateTasks
import com.google.gson.annotations.SerializedName

data class StateRows (

	@SerializedName("total") val total : Int,
	@SerializedName("rows") val rows : List<StateTasks>
)