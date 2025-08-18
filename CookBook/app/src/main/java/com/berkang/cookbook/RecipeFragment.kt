package com.berkang.cookbook

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.berkang.cookbook.databinding.FragmentRecipeBinding
import com.google.android.material.snackbar.Snackbar
import android.Manifest
import android.os.Build
import android.widget.Toast
import android.graphics.ImageDecoder
import java.io.IOException
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

public lateinit var permissionLauncher: ActivityResultLauncher<String>
public lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

public var secilenBitmap : Bitmap? = null
public var secilenGorsel : Uri? = null


