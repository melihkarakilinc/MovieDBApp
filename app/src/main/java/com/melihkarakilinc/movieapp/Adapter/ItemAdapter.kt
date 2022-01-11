package com.melihkarakilinc.movieapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melihkarakilinc.movieapp.ApiUrl
import com.melihkarakilinc.movieapp.ImageLoader
import com.melihkarakilinc.movieapp.ItemListener
import com.melihkarakilinc.movieapp.Model.Result
import com.melihkarakilinc.movieapp.R
import com.melihkarakilinc.movieapp.databinding.ItemLayoutBinding

class ItemAdapter() : RecyclerView.Adapter<MainViewHolder>(),ImageLoader {

    private var movies = mutableListOf<Result>()
    lateinit var context: Context
    lateinit var itemListener: ItemListener


    @SuppressLint("NotifyDataSetChanged")
    fun movieList(movie: List<Result>, itemListener: ItemListener) {
        this.movies = movie.toMutableList()
        this.itemListener = itemListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.txtTitle2.text = movie.title
        holder.binding.txtReleaseDate2.text = movie.release_date
        holder.binding.txtVote2.text = movie.vote_average.toString() + " / 10"
        if (movie.vote_average < 7) {
            holder.binding.imageStar22.setImageResource(R.drawable.star_red)
            holder.binding.txtVote2.setTextColor(context.resources.getColor(R.color.red))
        } else if (movie.vote_average in 7.0..9.0) {
            holder.binding.imageStar22.setImageResource(R.drawable.star_orange)
            holder.binding.txtVote2.setTextColor(context.resources.getColor(R.color.orange))
        } else if (movie.vote_average > 9) {
            holder.binding.imageStar22.setImageResource(R.drawable.star_green)
            holder.binding.txtVote2.setTextColor(context.resources.getColor(R.color.green))
        }
        Glide
            .with(context)
            .load(ApiUrl.POSTER_PATH + movie.poster_path)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_add_24)
            .into(holder.binding.imgPoster);

        holder.binding.relative.setOnClickListener {
            itemListener.OnItemSelect(movie)
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}

class MainViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

}