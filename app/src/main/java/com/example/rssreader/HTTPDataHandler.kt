package com.example.rssreader

import com.example.tests.db.Article
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


class HTTPDataHandler {
    var rssItems = ArrayList<Article>()
    var item : Article ?= null
    var text : String ?=null

    fun getHTTPData(inputStream: InputStream): List<Article> {

       try {
           val factory = XmlPullParserFactory.newInstance()
           factory.isNamespaceAware = true
           val parser = factory.newPullParser()
           parser.setInput(inputStream,null)
           var eventType = parser.eventType
           var foundItem = false

           while (eventType != XmlPullParser.END_DOCUMENT)
           {
               val tagName = parser.name
               when ( eventType)
               {
                   XmlPullParser.START_TAG -> if(tagName.equals("item",ignoreCase = true)){
                       foundItem = true
                       item = Article("","","","","")
                   }
                   XmlPullParser.TEXT -> text =parser.text
                   XmlPullParser.END_TAG -> if(tagName.equals("item",ignoreCase = true)) {
                       item?.let{rssItems.add(it)}
                       foundItem = false
                   }
                   else if (foundItem && tagName.equals("title",ignoreCase = true)){
                       item!!.articleTitle = text.toString()
                   }
                   else if (foundItem && tagName.equals("pubDate",ignoreCase = true)){
                       item!!.articlePubDate = text.toString()
                   }
                   else if (foundItem && tagName.equals("link",ignoreCase = true)){
                       item!!.articleLink = text.toString()
                   }
                   else if (foundItem && tagName.equals("description",ignoreCase = true)){
                       item!!.articleDescription = text.toString()
                   }
                   else if (foundItem && tagName.equals("enclosure",ignoreCase = true)){
                       item!!.articleEnclosure =parser.getAttributeValue(null, "url")
                   }
                   }
               eventType = parser.next()
               }
           }
       catch (e: XmlPullParserException)
       {
           e.printStackTrace()
       }
        catch (e: IOException)
        {
            e.printStackTrace()
        }
        return rssItems
       }
    }
