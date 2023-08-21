package co.fanavari.androidfanavari40205.ui.score

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        //session A19-06-04
        binding.textView4.setOnClickListener {
            binding.group.visibility = View.VISIBLE
        }
    }
}