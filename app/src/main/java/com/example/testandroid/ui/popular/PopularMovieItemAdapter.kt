package com.example.testandroid.ui.popular


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class PopularMovieItemAdapter(
    //private val moviesList: List<MovieEntity>,
    private val itemClickListener: OnMovieClickListener
) : PagingDataAdapter<MovieEntity, PopularMovieItemAdapter.PopularViewHolder>(MyDiffCallback)  {

    interface OnMovieClickListener {
        fun onMovieClick(movieEntity: MovieEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding  = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    //override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        with(holder){
            with(getItem(position)) {
                binding.titleMovieText.text = this?.title ?: ""
                binding.overviewMovieText.text = this?.overview ?: ""
                binding.percentageMovieText.text = this?.voteAverage?.toString() ?: ""
                binding.releaseMovieText.text = this?.releaseDate ?: ""
                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + (this?.posterPath ?: ""))
                    .into(binding.posterMovieImage)

                holder.itemView.setOnClickListener {
                    this?.let { it1 -> itemClickListener.onMovieClick(it1) }
                }
            }
        }
    }

    object MyDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }
    inner class PopularViewHolder(val binding: ItemMovieBinding)
        :RecyclerView.ViewHolder(binding.root)

}