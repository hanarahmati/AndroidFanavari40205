package co.fanavari.androidfanavari40205

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.fanavari.androidfanavari40205.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)

        viewModel.score.observe(this, Observer { score ->
            binding.scoreText.text = score.toString()
        })

        binding.scoreText.setOnClickListener {
            viewModel.incrementScore()
        }
    }
}