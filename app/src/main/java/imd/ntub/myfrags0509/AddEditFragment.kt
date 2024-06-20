package imd.ntub.myfrags0509

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import imd.ntub.myfrags0509.databinding.FragmentAddEditBinding
import java.io.ByteArrayOutputStream

class AddEditFragment : Fragment() {

    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        binding.buttonSave.setOnClickListener {
            val newName = binding.editTextName.text.toString()
            val newPhone = binding.editTextPhone.text.toString()

            if (newName.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val imageBytes: ByteArray = try {
                    if (imageUri != null) {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
                        val resizedBitmap = resizeBitmap(bitmap, 200, 200) // 调整图像尺寸
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        byteArrayOutputStream.toByteArray()
                    } else {
                        getDefaultImageBytes()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    getDefaultImageBytes()
                }

                val dbHelper = DBHelper(requireContext())
                val result = dbHelper.addContact(newName, newPhone, imageBytes)

                if (result != -1L) {
                    Toast.makeText(activity, "Contact saved", Toast.LENGTH_SHORT).show()

                    // 清空字段
                    binding.editTextName.text.clear()
                    binding.editTextPhone.text.clear()
                    binding.imageView.setImageBitmap(null)
                    imageUri = null

                    // 返回到第一個頁面
                    (activity as MainActivity).supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(activity, "Failed to save contact", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun getDefaultImageBytes(): ByteArray {
        val vectorDrawable = VectorDrawableCompat.create(resources, R.drawable.default_avatar, null)
        if (vectorDrawable == null) {
            throw IllegalArgumentException("Resource R.drawable.default_avatar could not be decoded")
        }

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(binding.imageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}