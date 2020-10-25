package myway.myapplication.adapter

import android.widget.BaseAdapter
import myway.myapplication.R
import myway.myapplication.network.model.tasks.TaskPages
import myway.myapplication.utils.commons.ViewHolder

class NewsAdapter(
    private val listener: (TaskPages) -> Unit, private val action: (Boolean, Int) -> Unit
) : BaseAdapter<TaskPages>(R.layout.item_news) {

    fun bindViewHolder(holder: ViewHolder, data: TaskPages) {
        holder.itemView.apply {
            data.apply {
                name.text = title
                image.loadImage(photo)
                author.loadImage(author_avatar)
                desc.text = short_description
                authorName.text = author_name
                createdDate.text = created_at.formatTime()

                commentsCount.text = comments_count.toString()
                sharesCount.text = shares_count.toString()
                mark.apply {

                    setOnClickListener {
                        it.blockClickable()
                        action.invoke(false, data.id)
                    }
                    setImageResource(if (is_bookmarked) R.drawable.ic_marked else R.drawable.ic_mark)
                }

                likesCount.apply {
                    text = likes_count.toString()

                    if (is_liked) {
                        setDrawableStart(R.drawable.ic_liked)
                        setTextColorRes(R.color.red)
                    } else {
                        setDrawableStart(R.drawable.ic_like)
                        setTextColorRes(R.color.hint)
                    }

                    setOnClickListener {
                        it.blockClickable()
                        action.invoke(true, data.id)
                    }
                }

                recyclerTags.adapter = TagsAdapter().apply {
                    setData(ArrayList(tags))
                }
            }

            container.setOnClickListener { listener.invoke(data) }
        }
    }
}
