package com.cmb.hbnews

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_user.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var signIn_btn : TextView
    private lateinit var signUp_btn : TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private var userID: String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
        firebaseAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        val firebaseFirestore = Firebase.firestore
        if (firebaseAuth.currentUser != null) {
            userID = firebaseAuth.currentUser!!.uid
            //Get name on Userprofile
            val documentReference: DocumentReference =
                firebaseFirestore.collection("users").document(userID)
            documentReference.addSnapshotListener(
            ) { documentSnapshot, e -> userName.setText(documentSnapshot!!.getString("fName")) }

        }



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signIn_btn = view.findViewById(R.id.signIn_btn)
        signUp_btn = view.findViewById(R.id.signUp_btn)

        signIn_btn.setOnClickListener{
            val intent = Intent(getActivity(),Login::class.java)
                startActivity(intent)
        }
        signUp_btn.setOnClickListener{
            val intent = Intent(getActivity(),SignUp::class.java)
            startActivity(intent)
        }
        if(firebaseAuth.currentUser == null)
        {
            signIn_btn.isVisible = true
            signUp_btn.isVisible = true
            textViewor.isVisible = true
        }
        SignOut.setOnClickListener {
            if (firebaseAuth.currentUser != null) {
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(requireContext())
                // set message of alert dialog
                dialogBuilder.setMessage("Thao tác này sẽ đăng xuất tài khoản khỏi ứng dụng.")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Đồng Ý", DialogInterface.OnClickListener { dialog, id ->
                        Firebase.auth.signOut()
                        Toast.makeText(
                            activity, "Cảm ơn.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(getActivity(), Login::class.java))


                    })
                    // negative button text and action
                    .setNegativeButton("Trở Về", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Đăng Xuất")
                // show alert dialog
                alert.show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}