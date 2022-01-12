package com.melihkarakilinc.movieapp.View

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.melihkarakilinc.movieapp.Adapter.SimilarAdapter
import com.melihkarakilinc.movieapp.Utils.ItemListener
import com.melihkarakilinc.movieapp.Model.Result
import com.melihkarakilinc.movieapp.R
import com.melihkarakilinc.movieapp.Utils.Util
import com.melihkarakilinc.movieapp.ViewModel.DetailViewModel
import com.melihkarakilinc.movieapp.databinding.FragmentDetailBinding


class DetailFragment : Fragment(), ItemListener {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val adapter = SimilarAdapter()
    private lateinit var resultDetail: Result
    private val util = Util()
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

        val connMgr = activity
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo

        binding.rvSimilar.adapter = adapter
        binding.rvSimilar.setLayoutManager(
            StaggeredGridLayoutManager(
                1,
                StaggeredGridLayoutManager.HORIZONTAL
            )
        )
        arguments?.let {
            resultDetail = DetailFragmentArgs.fromBundle(it).result
            util.imageLoader(resultDetail.poster_path, binding.imageView)
            binding.txtDetailOverview.text = resultDetail.overview
            binding.txtTitle.text = resultDetail.title
            binding.txtReleaseDate.text = resultDetail.release_date
            binding.txtVote.text = resultDetail.vote_average.toString()
        }

        if (networkInfo != null && networkInfo.isConnected) {
            viewModel.getData(resultDetail.id)
        } else {
            Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                .show()
        }

        viewModel.similarData.observe(viewLifecycleOwner, Observer { similar ->
            adapter.movieList(similar, this)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun OnItemSelect(result: Result) {
        val direction = DetailFragmentDirections
            .actionDetailFragmentSelf(result)
        findNavController().navigate(direction)
    }

}