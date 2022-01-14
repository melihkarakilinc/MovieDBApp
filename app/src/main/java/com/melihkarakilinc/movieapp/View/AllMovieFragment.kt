package com.melihkarakilinc.movieapp.View

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.melihkarakilinc.movieapp.Adapter.ItemAdapter
import com.melihkarakilinc.movieapp.Model.Result
import com.melihkarakilinc.movieapp.R
import com.melihkarakilinc.movieapp.Utils.ItemListener
import com.melihkarakilinc.movieapp.ViewModel.MainViewModel
import com.melihkarakilinc.movieapp.databinding.FragmentAllMovieBinding


class AllMovieFragment : Fragment(), ItemListener {

    private var _binding: FragmentAllMovieBinding? = null
    private val binding get() = _binding!!
    private var page: Int = 1
    private val viewModel: MainViewModel by viewModels()
    private val adapter = ItemAdapter()
    private var movieList = ArrayList<Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Fragment","onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        adapter.context = requireContext()
        Log.e("Fragment","onCreatedView")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Fragment","onViewCreated")


        binding.rv.adapter = adapter
        val connMgr = activity
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connMgr.activeNetworkInfo

        if (networkInfo!=null&&networkInfo.isConnected) {
            if (page == 1 && movieList.size == 0) {
                deleteRoomDatabase()
                viewModel.getData(page)
            }
            rvScroll()
        } else {
            gettRoomDatbase()
            Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                .show()
            binding.progressBar.visibility = View.GONE
            binding.progressBarnew.visibility = View.GONE
        }

        viewModel.progres.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.movieData.observe(viewLifecycleOwner, Observer { movie ->
            movieList = movie as ArrayList<Result>
            adapter.movieList(movieList, this)
            deleteRoomDatabase()
            insertRoomDatabase()
            binding.progressBarnew.visibility = View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            Log.e("ERROR!", error)
        })

    }

    fun insertRoomDatabase() {
        for (result in movieList) {
            viewModel.insertData(result)
        }
    }

    fun gettRoomDatbase() {
        viewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            if (data.isNotEmpty()) {
                movieList = data as ArrayList<Result>
                adapter.movieList(movieList, this)
                Toast.makeText(context, movieList.size.toString(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Data is Null", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun deleteRoomDatabase() {
        for (result in movieList) {
            viewModel.deleteData(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun OnItemSelect(result: Result) {
        val direction = AllMovieFragmentDirections
            .actionToDetailFragment(result)
        findNavController().navigate(direction)
    }
    private fun rvScroll(){
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                if (position + 1 == movieList.size) {
                    binding.progressBarnew.visibility = View.VISIBLE
                    deleteRoomDatabase()
                    if (page < 5) {
                        ++page
                        viewModel.getData(page)
                    } else {
                        view?.let {
                            Snackbar.make(it, getString(R.string.max_data_reached),Snackbar.LENGTH_LONG).show()
                        }
                        binding.progressBarnew.visibility = View.GONE
                    }
                }
            }
        })

    }
}