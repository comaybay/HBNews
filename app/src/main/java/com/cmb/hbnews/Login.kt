package com.cmb.hbnews

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_user.*

class Login : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorYellow)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        // Initialize Firebase Auth
        signUP_activity.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            overridePendingTransition(R.anim.anim_move_right, R.anim.anim_move_left)
        }
        forgetPassword.setOnClickListener {

        }
        button_SignIn.setOnClickListener {
            Login()
        }
        back_btn_SignIn.setOnClickListener {
                onBackPressed()
        }
        sign_in_button_google.setOnClickListener {

        }

    }
    private fun Login()
    {
        val email: String = loginEmailAddress.text.toString()
        val password: String = loginPassword.text.toString()

        if(email == "")
        {
            editTextTextEmailAddress.setError("Vui lòng điền địa chỉ Email.")
            Toast.makeText(
                baseContext, "Vui lòng điền địa chỉ Email.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if(password == "")
        {
            editTextTextEmailAddress.setError("Mật khẩu không được bỏ trống.")
            Toast.makeText(
                baseContext, "Mật khẩu không được bỏ trống.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else
        {
            progressBarLogin.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            baseContext, "Đăng nhập thành công. Chào mừng bạn đã quay lại.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        overridePendingTransition(R.anim.anim_move_right, R.anim.anim_move_left)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "Có lỗi.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }
    private fun googleLogin()
    {

    }
}