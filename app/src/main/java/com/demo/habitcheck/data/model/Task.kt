package com.demo.habitcheck.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Task(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo val title: String? = null,
    @ColumnInfo val description: String? = null,
    @ColumnInfo val progress: Int? = 0,
    @ColumnInfo val remindTime: String? = null,
    @ColumnInfo val remindDate: String? = null
) : Parcelable