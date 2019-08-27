package au.com.dius.pactconsumer.model

import com.google.gson.annotations.SerializedName
import java.util.Calendar

data class AnimalCollection(
    @SerializedName("valid_date")
    val validDate: Calendar,
    val animals: List<Animal>
)