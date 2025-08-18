package com.berkang.cookbook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.berkang.cookbook.adapter.RecipeAdapter
import com.berkang.cookbook.databinding.FragmentListBinding
import com.berkang.cookbook.model.Recipe
import com.berkang.cookbook.roomdb.RecipeDAO
import com.berkang.cookbook.roomdb.RecipeDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers



class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var  db: RecipeDatabase
    private lateinit var recipeDao: RecipeDAO
    private  val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(), RecipeDatabase::class.java,"Recipes")
            .build()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addButton.setOnClickListener { v -> addNew(v) }
       binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())

    }


    private fun takeDatas(){
        mDisposable.add(
            recipeDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn((AndroidSchedulers.mainThread()))
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(recipes : List<Recipe>){
val adapter=RecipeAdapter(recipes)
        binding.recyclerView.adapter=adapter
    }

    fun addNew(view: View){
        val action = ListFragmentDirections
            .actionListFragmentToRecipeFragment(bilgi = "new", id = 0)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

}