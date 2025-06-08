package com.example.mobile_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application.R
import com.example.mobile_application.ui.adapter.MovieAdapter
import com.example.mobile_application.viewmodel.MovieViewModel

class MovieListFragment : Fragment() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        adapter = MovieAdapter { movie ->
            // Nawigacja do szczegółów filmu
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
        }

        viewModel.fetchMovies(null) // Można podać tytuł do wyszukania

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString().trim()
                if (query.isBlank()) {
                    viewModel.fetchMovies(null)
                } else {
                    viewModel.fetchMovies(query)
                }
                true
            } else {
                false
            }
        }

    }
}
