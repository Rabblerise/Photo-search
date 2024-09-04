package com.example.laboratory3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.laboratory3.ui.theme.Laboratory3Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.SignInButton
import com.squareup.picasso.Picasso
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInButton: SignInButton
    private lateinit var likeButton: ImageButton
    private lateinit var dLikeButton: ImageButton
    private lateinit var searchButton: ImageButton
    private lateinit var googleImage: ImageView
    private lateinit var searchText: EditText
    private lateinit var toListButton: ImageButton
    private lateinit var toMainButton: ImageButton

    private var currentImageIndex: Int = 0
    private var links: List<String> = emptyList()
    private var likeLinks: MutableList<String> = mutableListOf()
    private var firstLayout: Boolean = true

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }
    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        searchText = findViewById(R.id.SearchText)

        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }

        searchButton = findViewById(R.id.Search)
        searchButton.setOnClickListener {
            currentImageIndex = 0
            loadImages()
        }

        likeButton = findViewById(R.id.Like)
        likeButton.setOnClickListener {
            if (currentImageIndex < links.size) {
                currentImageIndex++
                if (currentImageIndex < links.size) {
                    likeLinks.add(links[currentImageIndex])
                    loadImage(links[currentImageIndex])
                }
            }
        }

        dLikeButton = findViewById(R.id.dislike)
        dLikeButton.setOnClickListener {
            if (currentImageIndex < links.size) {
                currentImageIndex++
                if (currentImageIndex < links.size) {
                    loadImage(links[currentImageIndex])
                }
            }
        }

        googleImage = findViewById(R.id.GoogleImage)
        googleImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(links[currentImageIndex]))
            startActivity(intent)
        }

        toListButton = findViewById(R.id.toList)
        toListButton.setOnClickListener{
            setContentView(R.layout.second_layout)
            toMainButton = findViewById(R.id.toMain)
            toMainButton.setOnClickListener {
                setContentView(R.layout.main_layout)
            }
        }
    }
    private fun handleSignInResult(completeTask: Task<GoogleSignInAccount>){
        try{
            val account = completeTask.getResult(ApiException::class.java)

            val displayName = account?.displayName
            val email = account?.email

            Log.d("GoogleSignIn", "Display Name: $displayName, Email: $email")
        }catch (e: ApiException){
            Log.e("GoogleSignIn", "Sign-in failed with error code: ${e.statusCode}")
        }
    }

    private fun loadImages() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GoogleCustomSearchApi::class.java)

        lifecycleScope.launch(Dispatchers.IO){
            try {
                val response = api.searchImages(
                    apiKey = "AIzaSyBe42I9ZbnIyS-nrkYCdug2OLH7HPkeDLA",
                    cseId =  "f1acd59557afe4ab6",
                    query = "${searchText.text}",
                    searchType = "image"
                ).execute()

                if (response.isSuccessful){
                    val searchResponse = response.body()
                    searchResponse?.items?.let { items ->
                        links = items.map { item -> item.link }
                        withContext(Dispatchers.Main) {
                            loadImage(links[currentImageIndex]) // Используйте imagesSources
                        }
                    }
                }
                else{
                    val statusCode = response.code().toString()
                    val errorBody = response.errorBody()?.string()
                    Log.e("HTTP Status Code", statusCode)
                    Log.e("HTTP Status Code", errorBody.toString())
                }
            }
            catch (e: Exception){
                Log.e("ERROR", "Network request failed", e)
            }
        }
    }
    private fun loadImage(imageUrl: String) {
        Picasso.get().load(imageUrl).into(googleImage)
        Log.i("loadImage", imageUrl)
    }
}