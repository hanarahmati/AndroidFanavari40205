package co.fanavari.androidfanavari40205

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import co.fanavari.androidfanavari40205.databinding.ActivityDashboardBinding
import com.google.android.material.button.MaterialButton

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_dashboard)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editProfileButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val x: Int = 0
        val buttonTodo: MaterialButton = findViewById(R.id.todoButton)

        buttonTodo.setOnClickListener {
            createAlarm("test", 12, 15)
        }

        binding.profileButton.setOnClickListener {
            openWebPage("https://www.fanavari.co")
        }

        binding.imageButtonLogout.setOnClickListener {
            composeEmail(
                arrayOf("hana.rahmati@gmail.com","fanavari@gmail.com"),
                "contact us")
        }


    }
    private fun createAlarm(message: String, hour: Int, minutes: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        }

        //if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        //}
    }

    private fun composeEmail(addresses: Array<String>, subject: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
//            putExtra(Intent.EXTRA_STREAM, attachment)
        }
        //if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        //}
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        //if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        //}
    }

}