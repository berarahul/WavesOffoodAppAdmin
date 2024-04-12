package com.example.adminwavesoffood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.example.adminwavesoffood.Model.UserModel
import com.example.adminwavesoffood.databinding.ActivityLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var username:String?=null
    private var nameofresturant:String?=null
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
private lateinit var googlesinginclient: GoogleSignInClient
    private  var callbackManager = CallbackManager.Factory.create()
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //for Google
        val googlesigninoption: GoogleSignInOptions =GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        //initialize firebase auth
        auth=Firebase.auth
        //initialize firebase database
        database=Firebase.database.reference
        //initialize google sign in
        googlesinginclient= GoogleSignIn.getClient(this,googlesigninoption)
        initializeFacebookLogin()

        //loginbutton
        binding.loginbutton.setOnClickListener {
            //get text from edittext
            email=binding.NameOfOwner.text.toString().trim()
            password=binding.passWord.text.toString().trim()
            if (email.isBlank()||password.isBlank()){
                Toast.makeText(this,"Please Fill All The Details",Toast.LENGTH_SHORT).show()
            }else{
                createUserAccount(email,password)
            }
        }
        //for facebook button



        //for google button
        binding.googlebutton.setOnClickListener {
            val signIntent=googlesinginclient.signInIntent
            launcher.launch(signIntent)
        }
        binding.donthaveaccount.setOnClickListener{
            val intent=Intent(this,SingupActivity::class.java)
            startActivity(intent)

        }
    }

    private fun initializeFacebookLogin() {
        binding.facebookbutton.setOnClickListener {
            handleFacebookLogin()
        }
    }

    private fun handleFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("public_profile")
        )
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Facebook login canceled", Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onError(exception: FacebookException) {
                Toast.makeText(
                    this@LoginActivity,
                    "Facebook login error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun createUserAccount(email: String, password: String) {
   auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task ->

       if (task.isSuccessful){

           val user:FirebaseUser?=auth.currentUser
           Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()
           updateUi(user)

       }else
       {
           auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
               if (task.isSuccessful){
                   val user:FirebaseUser?=auth.currentUser
                   Toast.makeText(this,"Create User And Login SuccessFull",Toast.LENGTH_SHORT).show()
                   saveUserData()
                   updateUi(user)
               }else{
                   Toast.makeText(this,"Authentication Failed",Toast.LENGTH_SHORT).show()
                   Log.d("Account","CreateUserAccount: Authentication Failed",task.exception)
               }

           }

       }
   }
    }

    private fun saveUserData() {

        email=binding.NameOfOwner.text.toString().trim()
        password=binding.passWord.text.toString().trim()
        val user=UserModel(username,nameofresturant,email,password)
        val userid:String? =FirebaseAuth.getInstance().currentUser?.uid
        userid?.let{
                database.child("user").child(it).setValue(user)
        }
    }

 //Launcher for google sign-in
    private val launcher=registerForActivityResult( ActivityResultContracts.StartActivityForResult()){
        result->
        if (result.resultCode==Activity.RESULT_OK){

            val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){

                val account:GoogleSignInAccount=task.result
                val credential:AuthCredential=GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask->
                    if (authTask.isSuccessful){
                        // Successfully sign in with google
                        Toast.makeText(this,"Successfully sign-in with google",Toast.LENGTH_SHORT).show()

                       updateUi(authTask.result?.user)
                        finish()
                        Log.d("Account", "CreateUserAccount: Authentication Failed", task.exception)
                    }else
                    {
                        Toast.makeText(this,"google sign-in Failed ",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this,"google sign-in Failed ",Toast.LENGTH_SHORT).show()
            }
        }
    }
    //check if user log in

    override fun onStart() {
        super.onStart()
        val currentuser:FirebaseUser?=auth.currentUser
        if (currentuser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
    }

    }
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Toast.makeText(this, "Login with Facebook Successful", Toast.LENGTH_SHORT)
                        .show()
                    updateUi(user)
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    Log.d("Account", "Facebook authentication failed", authTask.exception)
                }
            }
    }
}