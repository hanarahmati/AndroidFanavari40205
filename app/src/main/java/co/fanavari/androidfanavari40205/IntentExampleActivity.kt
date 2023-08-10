package co.fanavari.androidfanavari40205

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.fanavari.androidfanavari40205.databinding.ActivityIntentExampleBinding

class IntentExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntentExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntentExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras

        bundle?.let {
            val mentorName = "mentor name is " +
                    "${bundle.getString("mentorName")} " +
                    "class number is" +
                    "${bundle.getInt(Constants.CLASS_NO)}"
            binding.textViewMentor.text = mentorName
        }

        binding.textViewMentor.setOnClickListener {
            val intent = Intent().apply {
                putExtra("message", binding.editTextName.text.toString())
                //putExtra(message, binding.textViewMentor.text)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}