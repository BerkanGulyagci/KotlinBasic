package com.berkang.cookbook.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.berkang.cookbook.activityResultLauncher
import com.berkang.cookbook.databinding.FragmentRecipeBinding
import com.berkang.cookbook.model.Recipe
import com.berkang.cookbook.permissionLauncher
import com.berkang.cookbook.roomdb.RecipeDAO
import com.berkang.cookbook.roomdb.RecipeDatabase
import com.berkang.cookbook.secilenBitmap
import com.berkang.cookbook.secilenGorsel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.IOException
import java.io.ByteArrayOutputStream
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers





class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    public lateinit var permissionLauncher: ActivityResultLauncher<String>
    public lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    public var secilenBitmap : Bitmap? = null
    public var secilenGorsel : Uri? = null
    private lateinit var  db: RecipeDatabase
    private lateinit var recipeDao: RecipeDAO
    private  val mDisposable =CompositeDisposable()
    private  var choosedRecipe: Recipe? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLauncher()
        db = Room.databaseBuilder(requireContext(), RecipeDatabase::class.java,"Recipes")
            .build()
        recipeDao = db.recipeDao()


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Senin mantık: it ile View geç
        binding.imageView.setOnClickListener { pickImg(it) }
        binding.saveButton.setOnClickListener { save(it) }
        binding.RemoveButton.setOnClickListener { delete(it) } // XML'deki id'n "deleteButton" değilse onu yaz

        arguments?.let {
            val bilgi = RecipeFragmentArgs.fromBundle(it).bilgi
            if (bilgi == "yeni" ) {
                // Yeni kaydediliyor
                choosedRecipe=null
                binding.RemoveButton.isEnabled = true
                binding.saveButton.isEnabled = true
                binding.mealText.setText("")
                binding.materialText.setText("")
            } else {
                // Eski gösteriliyor
                binding.RemoveButton.isEnabled = true
                binding.saveButton.isEnabled = true
                val id =RecipeFragmentArgs.fromBundle(it).id
                mDisposable.add(
                    recipeDao.findById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )
            }
        }
    }

    private  fun handleResponse(recipe: Recipe){
binding.mealText.setText(recipe.name)
        binding.materialText.setText(recipe.material)
       val bitmap= BitmapFactory.decodeByteArray(recipe.pimage, 0, recipe.pimage.size)
        binding.imageView.setImageBitmap(secilenBitmap)
        choosedRecipe=recipe

    }


    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    secilenGorsel = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                secilenGorsel!!
                            )
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(secilenBitmap)
                        } else {
                            secilenBitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                secilenGorsel
                            )
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                //permission granted
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(requireContext(), "Permisson needed!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun createSmallBitmap(secilenBitmap: Bitmap, maxSize: Int): Bitmap {
        var width = secilenBitmap.width
        var height = secilenBitmap.height

        val bitmapRatio: Double = width.toDouble() / height.toDouble()

        if (bitmapRatio > 1) {
            // Image is horizontal
            width = maxSize
            val reducedHeight = width / bitmapRatio
            height = reducedHeight.toInt()
        } else {
            // Image is vertical
            height = maxSize
            val reducedWidth = height * bitmapRatio
            width = reducedWidth.toInt()
        }

        return Bitmap.createScaledBitmap(secilenBitmap, width, height, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

    // --- Senin çağrılarınla uyumlu stublar (View parametreli) ---
    private fun pickImg(view: View) {  activity?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    Snackbar.make(
                        view,
                        "Permission needed for gallery",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(
                        view,
                        "Permission needed for gallery",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        }
    }
    }
    private fun save(view: View) {
        val name =binding.mealText.text.toString()
        val material = binding.materialText.text.toString()
        if (secilenBitmap != null) {
            val littleBitmap = createSmallBitmap(secilenBitmap!!, maxSize = 300)
            val outputStream = ByteArrayOutputStream()
            littleBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteArray = outputStream.toByteArray()

            val recipe = Recipe(name, material , byteArray)
            //RxJava ile veritabanına ekleme
            mDisposable.add(
                recipeDao.insert(recipe)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handeResponseForInsert)
            )




        }
    }


    private fun delete(view: View) {
        choosedRecipe?.let { recipe ->
            mDisposable.add(
                recipeDao.delete(recipe = choosedRecipe!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handeResponseForInsert)
            )
        }
    }
    private fun handeResponseForInsert(){
        val action=RecipeFragmentDirections.actionRecipeFragmentToListFragment()
        Navigation.findNavController(requireView()).navigate(action)
        //requireView() → “Ben şu an bu otobüsteyim.”
        //
        //findNavController() → “Otobüsün şoförünü bul.”
        //
        //navigate(action) → “Şoföre söyle: beni şu durağa götür (ListFragment).”
    }
}