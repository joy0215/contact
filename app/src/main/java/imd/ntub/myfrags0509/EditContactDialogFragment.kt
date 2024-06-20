package imd.ntub.myfrags0509

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import imd.ntub.myfrags0509.databinding.FragmentAddEditBinding
import java.io.ByteArrayOutputStream

class EditContactDialogFragment(private val contact: Contact) : DialogFragment() {

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

        // 预填充联系人信息
        binding.editTextName.setText(contact.name)
        binding.editTextPhone.setText(contact.phone)
        val bitmap = BitmapFactory.decodeByteArray(contact.image, 0, contact.image.size)
        Glide.with(this)
            .load(bitmap)
            .placeholder(R.drawable.default_avatar)
            .error(R.drawable.default_avatar)
            .into(binding.imageView)

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
                val imageBytes: ByteArray = if (imageUri != null) {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    byteArrayOutputStream.toByteArray()
                } else {
                    contact.image // 如果没有选择新图片，则使用原来的图片
                }

                val dbHelper = DBHelper(requireContext())
                val result = dbHelper.updateContact(contact.id, newName, newPhone, imageBytes)

                if (result > 0) {
                    Toast.makeText(activity, "Contact updated", Toast.LENGTH_SHORT).show()
                    dismiss() // 关闭对话框
                } else {
                    Toast.makeText(activity, "Failed to update contact", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getDefaultImageBytes(): ByteArray {
        val defaultBitmap = BitmapFactory.decodeResource(resources, R.drawable.default_avatar)
        val byteArrayOutputStream = ByteArrayOutputStream()
        defaultBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
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