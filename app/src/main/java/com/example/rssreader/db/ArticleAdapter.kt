package com.example.tests.db

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rssreader.R
import com.squareup.picasso.Picasso


class ArticleAdapter () : RecyclerView.Adapter<ArticleAdapter.NoteViewHolder>() {

    private var open_Item: onSelectedItem? = null
    lateinit var context: Context
    class NoteViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {

        val artticleTitle = view.findViewById<TextView>(R.id.titleA)
        val articleDescr = view.findViewById<TextView>(R.id.shortContentA)
        val dateArticle = view.findViewById<TextView>(R.id.date)
        val enclosure = view.findViewById<ImageView>(R.id.enclosure)

    }

    val articles = ArrayList<Article>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        context = parent.context
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.artticleTitle.text = articles[position].articleTitle
        holder.articleDescr.text = articles[position].articleDescription
        holder.dateArticle.text = articles[position].articlePubDate
        if(!articles[position].articleEnclosure.isNullOrEmpty())
        {
            Picasso.with(context).load(articles[position].articleEnclosure).into(holder.enclosure)
        }
        else
        {
            holder.enclosure.visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener{ open_Item!!.onClicked(articles[position]) }
    }


    fun updateList(newList: ArrayList<Article>)
    {
            articles.clear()
            articles.addAll(newList)
            notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return articles.size
    }


    fun setOpenItemInt(open_item : onSelectedItem)
    {
        open_Item = open_item
    }

    interface onSelectedItem{
        fun onClicked(article: Article)
    }


}
