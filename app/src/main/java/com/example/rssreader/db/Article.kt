package com.example.tests.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles_table")
class Article (
    @ColumnInfo(name = "title") var articleTitle: String,
    @ColumnInfo(name = "text" ) var articleDescription: String,
    @ColumnInfo(name = "pubDate") var articlePubDate:String,
    @ColumnInfo(name = "link") var articleLink:String,
    @ColumnInfo(name = "enclosure") var articleEnclosure:String)

{

    @PrimaryKey(autoGenerate = true) var id = 0
}

