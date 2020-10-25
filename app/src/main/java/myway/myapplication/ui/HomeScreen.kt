package myway.myapplication.ui

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import kotlinx.android.synthetic.main.screen_home.*
import myway.myapplication.R
import myway.myapplication.base.BaseFragment
import myway.myapplication.ui.home.NewsFragment

class HomeScreen : BaseFragment(R.layout.screen_home) {

    private var data = arrayListOf<HomeData>()

    override fun initialize() {

        data = arrayListOf(
            HomeData(R.drawable.ic_videocam, "Новости", NewsFragment())

        /* HomeData(R.drawable.ic_videocam, "Контент дня", MediaContentFragment())
        HomeData(R.drawable.ic_videocam, "Новости", NewsFragment()),
            HomeData(R.drawable.ic_videocam, "Прямые эфиры", StreamsFragment()),
            HomeData(R.drawable.ic_videocam, "Подкасты", PodcastsFragment())*/
        )

        pager.adapter = HomePagerAdapter(data, childFragmentManager)

        tabLayout.setupWithViewPager(pager)

        tabLayout.getTabAt(0)!!.setIcon(data[0].icon)
        tabLayout.getTabAt(1)!!.setIcon(data[1].icon)
        tabLayout.getTabAt(2)!!.setIcon(data[2].icon)
        tabLayout.getTabAt(3)!!.setIcon(data[3].icon)
    }
}

data class HomeData(@DrawableRes val icon: Int, val title: String, val fragment: Fragment)

data class HomePagerAdapter(private val data: ArrayList<HomeData>, val fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = data[position].fragment

    override fun getCount(): Int = data.size

    override fun getPageTitle(position: Int) = data[position].title

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        try {
            super.restoreState(state, loader)
        } catch (e: Exception) {
        }
    }

    override fun saveState(): Parcelable? = null
}
