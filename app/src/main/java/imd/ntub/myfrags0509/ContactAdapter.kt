package imd.ntub.myfrags0509

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import imd.ntub.myfrags0509.databinding.ItemContactBinding

class ContactAdapter(
    private val contactList: MutableList<Contact>,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.binding.nameTextView.text = contact.name
        holder.binding.phoneTextView.text = contact.phone

        // 设置头像图片，如果为空则使用默认头像
        val bitmap = if (contact.image.isNotEmpty()) {
            BitmapFactory.decodeByteArray(contact.image, 0, contact.image.size)
        } else {
            BitmapFactory.decodeResource(holder.itemView.context.resources, R.drawable.default_avatar)
        }
        holder.binding.imageView.setImageBitmap(bitmap)

        // 删除按钮点击事件
        holder.binding.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("删除联系人")
            builder.setMessage("确定要删除此联系人吗？")
            builder.setPositiveButton("是") { dialog, _ ->
                val dbHelper = DBHelper(holder.itemView.context)
                dbHelper.deleteContact(contact.id)
                contactList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, contactList.size)
                dialog.dismiss()
            }
            builder.setNegativeButton("否") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        // 编辑按钮点击事件
        holder.binding.editButton.setOnClickListener {
            (activity as MainActivity).startEditDialog(contact)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}