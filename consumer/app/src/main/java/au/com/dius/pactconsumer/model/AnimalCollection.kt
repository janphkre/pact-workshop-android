package au.com.dius.pactconsumer.model

import com.google.gson.annotations.SerializedName
import java.util.Calendar

data class AnimalCollection(
    val valid_date: Calendar,
    val animals: List<Animal>
)