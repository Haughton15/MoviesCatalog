package com.example.testandroid.ui.top_rated

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
import com.example.testandroid.databinding.FragmentTopRatedBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TopRatedFragment: Fragment(), TopRatedMovieItemAdapter.OnMovieClickListener {
    private var _binding: FragmentTopRatedBinding? = null

    private val binding get() = _binding!!
    private var loading = true

    private val viewModel: TopRatedViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var topRatedMovieItemAdapter: TopRatedMovieItemAdapter
    private lateinit var adapter: PagingDataAdapter<MovieEntity, TopRatedMovieItemAdapter.TopRatedViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopRatedBinding.inflate(inflater, container, false)
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
                    Log.e("fetchTopRatedMovies", "El usuario llego al final de la lista y scroll")
                    viewModel.loadNextPage()
                }
            }
        })
        viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchTopRatedMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    /*if (::popularMovieItemAdapter.isInitialized) {
                        popularMovieItemAdapter.updateData(it.data!!)
                    } else {
                        popularMovieItemAdapter = PopularMovieItemAdapter(it.data!! as MutableList<MovieEntity>, this@PopularFragment)
                    }*/
                    Log.e("fetchTopRatedMovies", "Success")
                    topRatedMovieItemAdapter = TopRatedMovieItemAdapter(it.data!!as MutableList<MovieEntity>, this@TopRatedFragment)
                    binding.rvMovies.adapter = topRatedMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchTopRatedMovies", "Failure: ${it.message} ")
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
       val action = TopRatedFragmentDirections.actionTopRatedFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}