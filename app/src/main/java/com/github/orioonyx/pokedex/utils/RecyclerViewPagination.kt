package com.github.orioonyx.pokedex.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class RecyclerViewPagination(
    recyclerView: RecyclerView,
    private val isLoading: () -> Boolean,
    private val loadMore: (Int) -> Unit,
    private val onLast: () -> Boolean = { true }
) : RecyclerView.OnScrollListener() {

    var threshold: Int = 10
    private var currentPage: Int = 0
    private var previousTotalItemCount = 0

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        recyclerView.layoutManager?.let { layoutManager ->
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItemPosition = when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is StaggeredGridLayoutManager -> findLastVisibleItemPosition(layoutManager.findLastVisibleItemPositions(null))
                else -> return
            }

            val shouldLoadMore = (visibleItemCount + lastVisibleItemPosition + threshold) >= totalItemCount

            if (isLoading() || onLast() || !shouldLoadMore) return

            if (totalItemCount != previousTotalItemCount) {
                currentPage++
                previousTotalItemCount = totalItemCount
                loadMore(currentPage)
            }
        }
    }

    private fun findLastVisibleItemPosition(lastVisibleItems: IntArray): Int {
        return lastVisibleItems.maxOrNull() ?: 0
    }

    fun resetPagination() {
        this.currentPage = 0
        this.previousTotalItemCount = 0
    }
}
