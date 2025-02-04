package com.example.phonelist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.phonelist.model.ContactModel
import com.example.phonelist.model.UserModel

class DBHelper(context: Context): SQLiteOpenHelper(context, "database.db", null, 1){

    val sql = arrayOf(
        "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)",
        "INSERT INTO users (username, password) VALUES ('admin', 'password')",
        "CREATE TABLE contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,address TEXT, email TEXT, phone INT, imageId INT)",
        "INSERT INTO contacts (name, address, email, phone, imageId) VALUES ('Maria', 'Rua das Flores', 'maria@email', 123456789, -1)",
        "INSERT INTO contacts (name, address, email, phone, imageId) VALUES ('João', 'Rua do Sol', 'joao@email', 987654321, -1)"
    )

    /*
    Cria a tabela de usuários no banco de dados
     */
    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach{
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    /* -----------------------------------------------------------------------------------------------------------------------------------------------------
                                                    CRUD USERS
    ----------------------------------------------------------------------------------------------------------------------------------------------------- */

    /*
    Insere um novo usuário no banco de dados
     */
    fun insertUser(username: String, password: String): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val res = db.insert("users", null, contentValues)
        db.close()
        return res
    }

    /*
    Atualiza os dados de um usuário existente no banco de dados
     */
    fun updateUser(id: Int, username: String, password: String): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val res = db.update("users", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    /*
    Deleta um usuário do banco de dados
     */
    fun deteteUser(id: Int): Int{
        val db = this.writableDatabase
        val res = db.delete("users", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    /*
    Retorna um usuario do banco de dados a partir do nome de usuario e da senha
    Utilizado para indentificar no banco de dados o usuario e a partir dele buscar os seus dados
     */
    fun getUser(username: String, password: String): UserModel {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password))
        var userModel = UserModel()
        if(c.count == 1){
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val usernameIndex = c.getColumnIndex("username")
            val passwordIndex = c.getColumnIndex("password")
            userModel = UserModel(id=c.getInt(idIndex), username=c.getString(usernameIndex), password=c.getString(passwordIndex))
        }
        db.close()
        return userModel
    }

    /*
    Verifica se o usuário existe no banco de dados
     */
    fun login(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password))
        var userModel = UserModel()
        return if(c.count == 1){
            true
        }else{
            db.close()
            false
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------------------------------------------
                                                    CRUD CONTACTS
    ----------------------------------------------------------------------------------------------------------------------------------------------------- */

    /*
    Insere um novo contato no banco de dados
     */
    fun insertContact(name : String, address: String, email: String, phone: Int, imageId: Int): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("address", address)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageId", imageId)
        val res = db.insert("contacts", null, contentValues)
        db.close()
        return res
    }

    /*
    Atualiza os dados de um contato existente no banco de dados
     */
    fun updateContact(id: Int, name : String, address: String, email: String, phone: Int, imageId: Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("address", address)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageId", imageId)
        val res = db.update("contacts", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    /*
    Deleta um contato do banco de dados
     */
    fun deleteContact(id: Int): Int{
        val db = this.writableDatabase
        val res = db.delete("contacts", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    /*
    Retorna um contato do banco de dados a partir do id
    Utilizado para buscar as informações dos contatos no banco de dados
     */
    fun getContact(id: Int): ContactModel {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM contacts WHERE id=?",
            arrayOf(id.toString())
        )
        var contactModel = ContactModel()
        if(c.count == 1){
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val idName = c.getColumnIndex("name")
            val idAddress = c.getColumnIndex("address")
            val idEmail = c.getColumnIndex("email")
            val idPhone = c.getColumnIndex("phone")
            val idImageId = c.getColumnIndex("imageId")
            contactModel = ContactModel(id=c.getInt(idIndex), name=c.getString(idName), address=c.getString(idAddress), email=c.getString(idEmail), phone=c.getInt(idPhone), imageId=c.getInt(idImageId))
        }
        db.close()
        return contactModel
    }

    /*
    Retorna todos os contatos do banco de dados a partir de uma lista
     */
    fun getAllContact(): List<ContactModel> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM contacts", null)
        var listContactModel = ArrayList<ContactModel>()

        if(c.moveToFirst()){
            val idIndex = c.getColumnIndex("id")
            val idName = c.getColumnIndex("name")
            val idAddress = c.getColumnIndex("address")
            val idEmail = c.getColumnIndex("email")
            val idPhone = c.getColumnIndex("phone")
            val idImageId = c.getColumnIndex("imageId")
            do{
                val contactModel = ContactModel(
                    id=c.getInt(idIndex),
                    name=c.getString(idName),
                    address=c.getString(idAddress),
                    email=c.getString(idEmail),
                    phone=c.getInt(idPhone),
                    imageId=c.getInt(idImageId)
                )
                listContactModel.add(contactModel)
            }while(c.moveToNext())
        }
        c.close()
        db.close()
        return listContactModel
    }
}