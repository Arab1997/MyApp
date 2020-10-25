package myway.myapplication.ui.filter

import android.os.Bundle

import kotlinx.android.synthetic.main.bottomsheet_filter.*
import myway.myapplication.R
import myway.myapplication.utils.bottomsheet.BottomSheetRoundedFragment
import myway.myapplication.utils.extensions.gone
import myway.myapplication.utils.extensions.showGone

enum class FilterType { TAGS, DATES, CITIES, FILTER }

class FilterBottomSheet : BottomSheetRoundedFragment(R.layout.bottomsheet_filter) {

    companion object {
        private var tagCount = ""
        private var date = ""
        private var city = ""
        fun newInstance(
            newsFilter: Boolean,
            tagCount: String,
            date: String,
            city: String
        ): FilterBottomSheet {
            this.tagCount = tagCount
            this.date = date
            this.city = city
            return FilterBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean("newsFilter", newsFilter)
                }
            }
        }
    }

    private lateinit var listener: (FilterType) -> Unit
    fun setListener(listener: (FilterType) -> Unit) {
        this.listener = listener
    }

    override fun initialize() {

        if (tagCount.isNotEmpty()) selectedTag.text = tagCount else selectedTag.gone()
        if (date.isNotEmpty()) selectedDate.text = date else selectedDate.gone()
        if (city.isNotEmpty()) selectedCity.text = city else selectedCity.gone()

        arguments?.let {
            val newsFilter = it.getBoolean("newsFilter")
            cities.showGone(!newsFilter)
            filter.setBackgroundResource(
                if (newsFilter) R.drawable.rounded_black_card
                else R.drawable.rounded_blue_card
            )
        }

        tags.setOnClickListener { invoke(FilterType.TAGS) }
        dates.setOnClickListener { invoke(FilterType.DATES) }
        cities.setOnClickListener { invoke(FilterType.CITIES) }
        filter.setOnClickListener { invoke(FilterType.FILTER) }
    }

    private fun invoke(type: FilterType) {
        listener.invoke(type)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        date = ""
        city = ""
        tagCount = ""
    }
}
