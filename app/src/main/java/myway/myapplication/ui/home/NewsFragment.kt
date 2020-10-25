package myway.myapplication.ui.home

import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.content_filter.*
import kotlinx.android.synthetic.main.fragment_news.*
import myway.myapplication.R
import myway.myapplication.adapter.NewsAdapter
import myway.myapplication.base.BaseFragment
import myway.myapplication.network.model.tasks.TaskPages
import myway.myapplication.ui.filter.FilterBottomSheet
import myway.myapplication.ui.filter.FilterType
import myway.myapplication.utils.extensions.blockClickable

class NewsFragment : BaseFragment(R.layout.fragment_news) {

    private lateinit var newsAdapter: NewsAdapter
  //  private lateinit var newsRoundedAdapter: NewsRoundedAdapter
    private var selectedTags = arrayListOf<TaskPages>()
    private var startDate: String = ""
    private var endDate: String = ""
    override fun initialize() {

        filter.setOnClickListener {
            it.blockClickable()
            val bottomSheet = FilterBottomSheet.newInstance(
                true,
                if (selectedTags.isNotEmpty()) selectedTags.size.toString() else "",
                if (startDate.isNotEmpty() && endDate.isNotEmpty()) "${startDate.formatTime4()} - ${endDate.formatTime4()}" else "",
                ""
            ).apply {
                setListener {
                    if (it == FilterType.TAGS) addFragment(
                        FilterTagsScreen.newInstance(false).apply {
                            setSelectedTags(selectedTags)
                            setListener {
                                selectedTags = it
                                getNews()
                            }
                        })
                    if (it == FilterType.DATES) addFragment(FilterDatesScreen().apply {
                        setListener {
                            startDate = it.key
                            endDate = it.value
                            getNews()
                        }
                    })
                }
            }
            bottomSheet.show(childFragmentManager, "")
        }
        favorites.setOnClickListener {
            addFragment(FavoriteNewsScreen())
        }

       /* val data = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, "")
        newsRoundedAdapter = NewsRoundedAdapter {
            addFragment(StoriesFragment())
        }.apply { setData(data) }*/

        newsAdapter = NewsAdapter({
           // addFragment(NewsDetailScreen.newInstance(it.id), tag = Constants.NEWS_DETAILED_FRAGMENT)
        }, { like, id ->
            if (like) viewModel.likeNews(id)
            else viewModel.bookmarkNews(id)
        })

       // recycler.adapter = newsRoundedAdapter
        recyclerNews.adapter = newsAdapter

        swipeLayout.setOnRefreshListener {
            viewModel.getTasks()
        }
    }

    private fun getNews() {
        val ids = ArrayList(selectedTags.map { it.id })
        viewModel.getTasks(
            startDate.getOrNull(),
            endDate.getOrNull(),
            if (ids.isNotEmpty()) ids else null
        )
    }

    private fun String.getOrNull() = if (this.isNotEmpty()) this else null
    override fun observe() {
        viewModel.taskPages.observe(viewLifecycleOwner, Observer {
            swipeLayout?.isRefreshing = false
            val count = ArrayList(it.filter { it.is_bookmarked }).size
            favCount.text = if (count != 0) count.toString() else ""
            newsAdapter.setData(it)
        })
    }

}

