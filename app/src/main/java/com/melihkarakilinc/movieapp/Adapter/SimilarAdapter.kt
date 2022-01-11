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
import com.melihkarakilinc.movieapp.databinding.SimilarLayoutBinding

class SimilarAdapter() : RecyclerView.Adapter<SimilarViewHolder>(), ImageLoader {

    private var similar = mutableListOf<Result>()
    lateinit var context: Context
    lateinit var itemListener: ItemListener


    @SuppressLint("NotifyDataSetChanged")
    fun movieList(movie: List<Result>, itemListener: ItemListener) {
        this.similar = movie.toMutableList()
        this.itemListener = itemListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SimilarLayoutBinding.inflate(inflater, parent, false)
        return SimilarViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        val movie = similar[position]
        holder.binding.txtTitleSimilar.text = movie.title
        holder.binding.txtVoteSimilar.text = movie.vote_average.toString() + " / 10"
        if (movie.vote_average < 7) {
            holder.binding.imageStarSimilar.setImageResource(R.drawable.star_red)
            holder.binding.txtVoteSimilar.setTextColor(context.resources.getColor(R.color.red))
        } else if (movie.vote_average in 7.0..9.0) {
            holder.binding.imageStarSimilar.setImageResource(R.drawable.star_orange)
            holder.binding.txtVoteSimilar.setTextColor(context.resources.getColor(R.color.orange))
        } else if (movie.vote_average > 9) {
            holder.binding.imageStarSimilar.setImageResource(R.drawable.star_green)
            holder.binding.txtVoteSimilar.setTextColor(context.resources.getColor(R.color.green))
        }
        Glide
            .with(context)
            .load(ApiUrl.POSTER_PATH + movie.poster_path)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_add_24)
            .into(holder.binding.imgPosterSimilar);

        holder.binding.relative2.setOnClickListener {
            itemListener.OnItemSelect(movie)
        }

    }

    override fun getItemCount(): Int {
        return similar.size
    }
}

class SimilarViewHolder(val binding: SimilarLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

}