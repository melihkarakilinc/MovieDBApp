package com.melihkarakilinc.movieapp.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.melihkarakilinc.movieapp.Adapter.SimilarAdapter
import com.melihkarakilinc.movieapp.ApiUrl
import com.melihkarakilinc.movieapp.ItemListener
import com.melihkarakilinc.movieapp.Model.Result
import com.melihkarakilinc.movieapp.R
import com.melihkarakilinc.movieapp.ViewModel.DetailViewModel
import com.melihkarakilinc.movieapp.databinding.FragmentDetailBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager




class DetailFragment : Fragment(), ItemListener {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val adapter = SimilarAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        adapter.context = requireContext()
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSimilar.adapter = adapter
        binding.rvSimilar.setLayoutManager(
            StaggeredGridLayoutManager(
                1,
                StaggeredGridLayoutManager.HORIZONTAL
            )
        )
        arguments?.let {
            imageLoad(DetailFragmentArgs.fromBundle(it).result.poster_path, binding.imageView)
            val overView = DetailFragmentArgs.fromBundle(it).result.overview
            binding.txtDetailOverview.text = overView
            binding.txtTitle.text = DetailFragmentArgs.fromBundle(it).result.title
            binding.txtReleaseDate.text = DetailFragmentArgs.fromBundle(it).result.release_date
            binding.txtVote.text = DetailFragmentArgs.fromBundle(it).result.vote_average.toString()
            viewModel.getData(DetailFragmentArgs.fromBundle(it).result.id)

        }



        viewModel.similarData.observe(viewLifecycleOwner, Observer { similar ->
            adapter.movieList(similar, this)
            Log.e("Similar",similar.toString())
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun imageLoad(path: String, imageView: ImageView) {
        Glide
            .with(imageView.context)
            .load(ApiUrl.POSTER_PATH + path)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_add_24)
            .into(imageView);
    }

    override fun OnItemSelect(result: Result) {
        TODO("Not yet implemented")
    }
}