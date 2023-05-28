package me.josena.hipica.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id") val id: Int = 0,
    @PrimaryKey
    @ColumnInfo(name = "rider") var rider: String,
    @ColumnInfo(name = "horse") var horse: String,
    @ColumnInfo(name = "tlf") var telephone: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "hour") var hour: String,
    @ColumnInfo(name = "comment") var comment: String
) {
    override fun toString(): String {
        return "Se ha confirmado su reserva: \n$date a las $hour horas. \nNo olvide el caballo.\nGracias."
    }
}

//    constructor(
//        rider: String,
//        horse: String,
//        telephone: String,
//        date: String,
//        hour: String,
//        comment: String
//    ) :
//            this(0, rider, horse, telephone, date, hour, comment)
//}