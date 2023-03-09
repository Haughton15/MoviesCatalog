package com.example.testandroid.ui.popular

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentPopularBinding
import com.example.testandroid.utils.PageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : Fragment(), PopularMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentPopularBinding? = null

    private val binding get() = _binding!!
    private var loading = true

    private val viewModel: PopularViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var popularMovieItemAdapter: PopularMovieItemAdapter
    private lateinit var adapter: PagingDataAdapter<MovieEntity,PopularMovieItemAdapter.PopularViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview = binding.rvMovies
        recyclerview.layoutManager = LinearLayoutManager(context)
        //recyclerview.adapter = popularMovieItemAdapter
        popularMovieItemAdapter = PopularMovieItemAdapter( this@PopularFragment)
        binding.rvMovies.adapter = popularMovieItemAdapter
        viewModel.popularMovies.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            //popularMovieItemAdapter = PopularMovieItemAdapter(, this@PopularFragment)
        }
        /*recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener()  {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible + visibleItemCount >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached && loading) {
                    loading=false
                    //viewModel.fetchPopularMovies(
                    Log.e("fetchPopularMovies", "Loading more")
                    Toast.makeText(requireContext(), "Loading more", Toast.LENGTH_SHORT).show()
                    pageUtils = PageUtils
                    PageUtils.popularPage += 1
                    //viewModel.notifylastVisible(layoutManager.findLastVisibleItemPosition())
                    println("PRINT-----------------------" + pageUtils.popularPage)

                    //popularMovieItemAdapter.notifyDataSetChanged()
                    loading=true
                }


            }
        })*/
        /*viewModel.fetchPopularMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchPopularMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchPopularMovies", "Success")
                    popularMovieItemAdapter = PopularMovieItemAdapter(it.data!!, this@PopularFragment)
                    binding.rvMovies.adapter = popularMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchPopularMovies", "Failure: ${it.message} ")
                    Toast.makeText(requireContext(), "Failure: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = PopularFragmentDirections.actionHomeFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}