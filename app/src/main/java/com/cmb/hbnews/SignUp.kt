package com.cmb.hbnews

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private lateinit var firebaseStore: Firebase
    private var userID: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorYellow)
        }
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        button_SignUp.setOnClickListener {
            register()

        }
        back_btn_SignUp.setOnClickListener{
            onBackPressed()
        }
        termApp.setOnClickListener {
            term()
        }





    }

    private fun register()
    {

        val email :String  = editTextTextEmailAddress.text.toString()
        val password = editTextTextPassword.text.toString()
        val passwordAgain = editTextTextPassword2.text.toString()
        val userName = editTextTextPersonName.text.toString()

        if(email == "")
        {
            editTextTextEmailAddress.setError("Vui lòng điền địa chỉ Email.")
            Toast.makeText(
                baseContext, "Vui lòng điền địa chỉ Email.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if(userName == "")
        {
            editTextTextPersonName.setError("Tên không được bỏ trống.")
            Toast.makeText(
                baseContext, "Tên không được bỏ trống.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if(password == "")
        {
            editTextTextPassword.setError("Bạn chưa nhập mật khẩu kìa.")
            Toast.makeText(
                baseContext, "Bạn chưa tạo mật khẩu.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if(passwordAgain == "")
        {
            editTextTextPassword2.setError("Nhập lại mật khẩu")
            Toast.makeText(
                baseContext, "Nhập lại mật khẩu.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if(password.length < 6)
        {
            editTextTextPassword.setError("Mật khẩu phải trên 6 kí tự.")
            Toast.makeText(
                baseContext, "Mật khẩu phải trên 6 kí tự.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (password == passwordAgain && checkBox.isChecked()) {
            progressBarSignUp.visibility = View.VISIBLE

            firebaseAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        userID = firebaseAuth.currentUser!!.uid
                        val documentReference: DocumentReference = Firebase.firestore.collection("users").document(userID)
                        val user: MutableMap<String, Any> = HashMap()
                        user["fName"] = userName
                        user["fEmail"] = email
                        user["prefNewsSources"] = "All"
                        documentReference.set(user).addOnSuccessListener {
                            Log.d(
                                "TAG",
                                "Tạo tài khoản thành công: $userID"
                            )
                        }.addOnFailureListener { e -> Log.d("TAG", "Lỗi: $e") }
                        startActivity(Intent(this, Login::class.java))
                        overridePendingTransition(R.anim.anim_move_right, R.anim.anim_move_left)
                        finish()
                    } else {
                        progressBarSignUp.visibility = View.INVISIBLE
                        Toast.makeText(
                            baseContext, "Tài khoản đã tồn tại.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            progressBarSignUp.visibility = View.INVISIBLE
            Toast.makeText(
                baseContext, "Mật khẩu bạn nhập không chính xác.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun term()
    {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        // set message of alert dialog
        dialogBuilder.setMessage("Thỏa thuận Google Apps (Miễn phí)\n" +
                "Đi tới Điều khoản dịch vụ của Google Apps for Business\n" +
                "Đi tới Điều khoản dịch vụ của Google Apps for Education\n" +
                "\n" +
                "Thỏa thuận Google Apps (miễn phí) này (\"Thỏa thuận\") này được lập bởi và giữa Google Inc., một công ty Delaware, có văn phòng tại 1600 Amphitheatre Parkway, Mountain View, California 94043 (\"Google\") và tổ chức đồng ý với những điều khoản này (\"Khách hàng\"). Thỏa thuận này điều chỉnh quyền truy cập và sử dụng Dịch vụ của Khách hàng và sẽ có hiệu lực kể từ Ngày bắt đầu có hiệu lực. Thỏa thuận này có hiệu lực kể từ ngày Khách hàng nhấp vào nút \"Tôi chấp nhận\" dưới đây (\"Ngày bắt đầu có hiệu lực\"). Nếu bạn chấp nhận thay mặt cho Khách hàng, bạn đại diện và đảm bảo rằng: (i) bạn có đầy đủ quyền hạn pháp lý để ràng buộc chủ lao động của mình hoặc tổ chức hiện hành với những điều khoản và điều kiện này (ii) bạn đã đọc và hiểu rõ Thỏa thuận này; và (iii) bạn đồng ý, thay mặt cho bên mà bạn đại diện, với Thỏa thuận này. Nếu bạn không có quyền hạn pháp lý để ràng buộc Khách hàng, xin vui lòng không nhấp vào nút \"Tôi chấp nhận\" bên dưới. Thỏa thuận này điều chỉnh quyền truy cập và sử dụng Dịch vụ của Khách hàng.")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Đồng Ý", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                Toast.makeText(
                    baseContext, "Cảm ơn bạn đã đọc.",
                    Toast.LENGTH_SHORT
                ).show()
            })
            // negative button text and action
            .setNegativeButton("Trở Về", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Điều Khoản")
        // show alert dialog
        alert.show()
    }


}
