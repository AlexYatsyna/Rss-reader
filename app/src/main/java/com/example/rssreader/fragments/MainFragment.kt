package com.example.rssreader.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rssreader.HTTPDataHandler
import com.example.rssreader.databinding.FragmentMainBinding
import com.example.rssreader.network.NetworkUtil
import com.example.tests.db.Article
import com.example.tests.db.ArticleAdapter
import com.example.tests.db.ArticleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var rssLink = ""
    var rssItems = ArrayList<Article>()
    var lastItems = ArrayList<Article>()
    private lateinit var viewModel: ArticleViewModel
    private lateinit var adapterM : ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
            ArticleViewModel::class.java)

        adapterM = ArticleAdapter()
        adapterM.setOpenItemInt(onSelectedItem)

        viewModel.lastArticles.observe(
            viewLifecycleOwner,
            Observer { list ->
                list?.let {
                    if (adapterM.articles.isEmpty())
                        adapterM.updateList(ArrayList(it))
                    lastItems = ArrayList(it)
                }
            }
        )

        rssLink = getUrl()
        binding.editUrl.setText(rssLink)

        var orient = 1
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orient = 2
        }

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(orient, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = adapterM
        }

        binding.refresh.setOnClickListener {
            updateArticles()
        }

        binding.editUrl.setOnFocusChangeListener(
            View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    binding.addUrl.visibility = View.VISIBLE
                } else {
                    binding.addUrl.visibility = View.INVISIBLE
                }
            })

        binding.addUrl.setOnClickListener{changeRsslink(binding.editUrl.text.toString())}

        updateArticles()
    }

    private var onSelectedItem = object : ArticleAdapter.onSelectedItem {
        override fun onClicked(article: Article) {
            if((lastItems.filter { it.articleLink.equals(article.articleLink) }).isNullOrEmpty())
            {
                viewModel.addArticle(article)
            }
            val intent = Intent(context, ArticleFragment::class.java)
            intent.putExtra("url",article.articleLink)
            startActivity(intent)
        }
    }

     private fun changeRsslink(url : String)
     {
         if(url != rssLink)
         {
         rssLink = url
         writeUrl(url)
         binding.editUrl.setText(rssLink)
         updateArticles()
         }
         requireActivity().currentFocus?.let { view ->
             val imm =requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
             imm?.hideSoftInputFromWindow(view.windowToken, 0)
         }
         binding.editUrl.clearFocus()
     }

    private suspend fun loadRss() {

        withContext(Dispatchers.IO)
        {
            var inputStream: InputStream? = null
            try {
                val url = URL(rssLink)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.inputStream
                    val parser = HTTPDataHandler()
                    rssItems = ArrayList(parser.getHTTPData(inputStream!!))
                }
                connection.disconnect()
            } catch (e: Exception) {
                Log.e(tag, e.toString())
            }
        }
        adapterM.updateList(rssItems)
    }

     fun updateArticles()
     {
         var status = NetworkUtil.getConnectivityStatus(context)
         if (status == NetworkUtil.TYPE_NOT_CONNECTED) {
             loadLast(lastItems)
         } else {
             lifecycleScope.launch {
                 loadRss()
             }
         }
     }

    fun loadLast(last : List<Article>)
    {
        adapterM.updateList(ArrayList<Article>(last))
    }

    fun writeUrl(url:String)
    {
        val file = File(context?.filesDir,"url.txt")
        if(!file.exists())
            file.createNewFile()
        file.writeText(url)
    }

    fun getUrl():String
    {
        val file = File(context?.filesDir,"url.txt")
        if(file.exists())
            return file.readText()
        else
            return ""
    }

}

