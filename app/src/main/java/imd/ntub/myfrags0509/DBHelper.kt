package imd.ntub.myfrags0509

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_IMAGE + " BLOB" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addContact(name: String, phone: String, image: ByteArray): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_IMAGE, image)

        val result = db.insert(TABLE_CONTACTS, null, values)
        db.close()
        return result
    }

    fun updateContact(id: Int, name: String, phone: String, image: ByteArray): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_IMAGE, image)

        val result = db.update(TABLE_CONTACTS, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun deleteContact(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CONTACTS, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun getAllContacts(): List<Contact> {
        val contactList: MutableList<Contact> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val contact = Contact(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                )
                contactList.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return contactList
    }

    fun getContactById(id: Int): Contact? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CONTACTS,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_PHONE, COLUMN_IMAGE),
            "$COLUMN_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var contact: Contact? = null
        if (cursor.moveToFirst()) {
            contact = Contact(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            )
        }
        cursor.close()
        db.close()
        return contact
    }
}