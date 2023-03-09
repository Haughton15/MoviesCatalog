package com.example.testandroid.ui.upcoming

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentUpcomingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFragment: Fragment(), UpcomingMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentUpcomingBinding? = null

    private val binding get() = _binding!!
    private var loading = true

    private val viewModel: UpcomingViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var upcomingMovieItemAdapter: UpcomingMovieItemAdapter
    private lateinit var adapter: PagingDataAdapter<MovieEntity, UpcomingMovieItemAdapter.UpcomingViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview = binding.rvMovies
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItem >= totalItemCount && firstVisibleItem >= 0 && loading) {
                    loading = false
                    Log.e("fetchUpcomingMovies", "El usuario llego al final de la lista y scroll")
                    viewModel.loadNextPage()
                }
            }
        })
        viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchUpcomingMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    /*if (::popularMovieItemAdapter.isInitialized) {
                        popularMovieItemAdapter.updateData(it.data!!)
                    } else {
                        popularMovieItemAdapter = PopularMovieItemAdapter(it.data!! as MutableList<MovieEntity>, this@PopularFragment)
                    }*/
                    Log.e("fetchUpcomingMovies", "Success")
                    upcomingMovieItemAdapter = UpcomingMovieItemAdapter(it.data!!as MutableList<MovieEntity>, this@UpcomingFragment)
                    binding.rvMovies.adapter = upcomingMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchUpcomingMovies", "Failure: ${it.message} ")
                    Toast.makeText(requireContext(), "Failure: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = UpcomingFragmentDirections.actionUpcomingFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}