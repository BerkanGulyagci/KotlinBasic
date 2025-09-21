package com.berkang
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.berkang.instagramclone.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    var selectedPicture : Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
      registerLauncher()
        auth= Firebase.auth
        firestore= Firebase.firestore
        storage=Firebase.storage


    }


    fun uploadClicked(view: View) {
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        if (selectedPicture != null) {
            imageReference.putFile(selectedPicture!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Başarılı yükleme → burada download URL alıp Firestore’a kaydedebilirsin
                    val uploadedPictureReference = storage.reference.child("images").child(imageName)
                    uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        println(downloadUrl)
                        val postMap = hashMapOf<String,Any>()
                        postMap.put("downloadUrl",downloadUrl)
                        postMap.put("userEmail",auth.currentUser!!.email.toString())
                        postMap.put("comment",binding.uploadCommentText.text.toString())
                        postMap.put("date", Timestamp.now())

                        firestore.collection("Posts").add(postMap).addOnCompleteListener{
                            task-> if(task.isComplete && task.isSuccessful){
                                //Feed’e dön
                                finish()
                            }
                        }

                }
                .addOnFailureListener {
                    Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }
}


    fun imageViewClicked (view : View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            ) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give Permission") {
//request permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                    }.show()
            } else {
//request permission
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }

        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //startActivityForResult
            activityResultLauncher.launch(intent)
        }
    }

private fun registerLauncher() {
    activityResultLauncher  = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val intentFromResult = result.data
            if (intentFromResult != null) {
                selectedPicture= intentFromResult.data
              selectedPicture?.let {
                  binding.uploadImageView.setImageURI(it)
              }
            }
        }


    }
    permissionLauncher=registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            //permission granted
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        } else {
            //permission denied
            Toast.makeText(this, "Permission needed!", Toast.LENGTH_LONG).show()
        }
    }

}






}