package com.example.homework3

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework3.databinding.ActivityMainBinding
import com.example.homework3.databinding.CustomDialogWindowBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var mainMenu: Menu
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var listContact: MutableList<ContactModel>
    private var sizeListContact = 0
    private lateinit var listEnabledContactItem: MutableList<ContactModel>
    private val contactService = ContactService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        contactService.getContactList()
        initRecyclerAndAdapter()
        addContact()
        editContact()
        multipleChoiceOfContacts()
    }

    private fun initRecyclerAndAdapter() {
        listContact = contacts()
        contactAdapter = ContactAdapter()
        contactService.liveData.observe(this) {
            contactAdapter.submitList(it)
            sizeListContact = it.size
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerView.adapter = contactAdapter
        setupSwipeListener(binding.recyclerView)
    }

    private fun addContact() {
        binding.btAdd.setOnClickListener {
            val dialogBinding = CustomDialogWindowBinding.inflate(layoutInflater)
            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding.root)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            dialogBinding.btAdd.setOnClickListener {
                contactService.addContactItem(
                    ContactModel(
                        firstName = dialogBinding.edtFirstName.text.toString(),
                        lastName = dialogBinding.edtLastName.text.toString(),
                        phoneNumber = dialogBinding.edtPhoneNumber.text.toString(),
                        isEnabled = false
                    )
                )
                myDialog.dismiss()
            }
            dialogBinding.btCancellation.setOnClickListener {
                myDialog.dismiss()
            }
        }
    }

    private fun editContact() {
        contactAdapter.onItemClickListener = { contactItem ->
            val dialogBinding = CustomDialogWindowBinding.inflate(layoutInflater)
            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding.root)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            dialogBinding.edtFirstName.setText(contactItem.firstName)
            dialogBinding.edtLastName.setText(contactItem.lastName)
            dialogBinding.edtPhoneNumber.setText(contactItem.phoneNumber)

            dialogBinding.btAdd.setOnClickListener {
                contactService.editContactItem(
                    contactItem.copy(
                        firstName = dialogBinding.edtFirstName.text.toString(),
                        lastName = dialogBinding.edtLastName.text.toString(),
                        phoneNumber = dialogBinding.edtPhoneNumber.text.toString()
                    )
                )
                myDialog.dismiss()
            }

            dialogBinding.btCancellation.setOnClickListener {
                myDialog.dismiss()
            }
        }
    }

    private fun multipleChoiceOfContacts() {
        listEnabledContactItem = mutableListOf()
        contactAdapter.onItemClickListenerSelected = {

            if (it.isEnabled) {
                listEnabledContactItem.remove(it.copy(isEnabled = false))
            } else {
                listEnabledContactItem.add(it)
            }

            supportActionBar?.title = "Selected ${listEnabledContactItem.size} of $sizeListContact"

            if (listEnabledContactItem.size > 0) {
                mainMenu.findItem(R.id.menu_delete).isVisible = true
                mainMenu.findItem(R.id.menu_delete_dis).isVisible = false
            } else {
                mainMenu.findItem(R.id.menu_delete_dis).isVisible = true
                mainMenu.findItem(R.id.menu_delete).isVisible = false
            }

            contactService.editEnabledContactItem(it)
        }
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition

                contactService.liveData.observe(this@MainActivity) {
                    Collections.swap(it, fromPos, toPos)
                    contactAdapter.notifyItemMoved(fromPos, toPos)
                }
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = contactAdapter.currentList[viewHolder.adapterPosition]
                contactService.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.action_bar_menu, mainMenu)
        mainMenu.findItem(R.id.menu_delete).isVisible = false
        mainMenu.findItem(R.id.menu_choose).isVisible = true
        mainMenu.findItem(R.id.menu_delete_dis).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_choose -> {
                mainMenu.findItem(R.id.menu_choose).isVisible = false
                mainMenu.findItem(R.id.menu_delete_dis).isVisible = true
                contactAdapter.mod = ContactAdapter.DELETE_MOD
                supportActionBar?.title =
                    "Selected ${listEnabledContactItem.size} of $sizeListContact"
            }
            R.id.menu_delete -> {
                contactService.deleteSelectedItems(listEnabledContactItem)
                initMenuVisible()
            }
            R.id.menu_delete_dis -> {
                initMenuVisible()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMenuVisible() {
        mainMenu.findItem(R.id.menu_delete).isVisible = false
        mainMenu.findItem(R.id.menu_choose).isVisible = true
        mainMenu.findItem(R.id.menu_delete_dis).isVisible = false
        contactAdapter.mod = ContactAdapter.STANDARD_MOD
        listEnabledContactItem.clear()
        supportActionBar?.title = this.getString(R.string.app_name)
    }
}