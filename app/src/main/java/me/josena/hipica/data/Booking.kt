package me.josena.hipica.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(

    @PrimaryKey
    var rider: String,
    var horse: String,
    var telephone: String,
    var date: String,
    var hour: String,
    var comment: String
) {
//    @PrimaryKey(autoGenerate = true) val id: IntF
    override fun toString(): String {
        return "Se ha confirmado su reserva: \n$date a las $hour horas. \nNo olvide el caballo.\nGracias."
    }
}