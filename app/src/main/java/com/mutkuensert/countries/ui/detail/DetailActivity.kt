package com.mutkuensert.countries.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.mutkuensert.countries.databinding.ActivityDetailBinding
import com.mutkuensert.countries.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var countryCode: String
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemBars()

        getIntentExtraOrFinish()
        setObserver()
        setClickListener()
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun getIntentExtraOrFinish(){
        if (intent.extras?.getString("countryCode")!= null){
            countryCode = intent.extras?.getString("countryCode")!!
            viewModel.requestCountryDetail(countryCode)
        }else{ finish() }
    }

    private fun setClickListener(){
        binding.actionBar.setNavigationOnClickListener { finish() }
    }

    private fun setObserver(){
        viewModel.data.observe(this){ resource->
            when(resource.status){
                Status.STANDBY -> {}
                Status.ERROR -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    resource.data?.let { response ->
                        with(binding){
                            capitalTextView.text = response.capital
                            countryCodeTextView.text = response.code
                            actionBar.title = response.name

                            forMoreInformationButton.setOnClickListener {
                                response.wikiDataId?.let { id->
                                    val url = "https://www.wikidata.org/wiki/" + id
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)),null)
                                }
                            }
                        }

                        val imageLoader = ImageLoader.Builder(this)
                            .components {
                                add(SvgDecoder.Factory())
                            }
                            .build()
                        binding.imageView.load(response.flagImageUri, imageLoader)
                    }
                }
            }
        }
    }
}