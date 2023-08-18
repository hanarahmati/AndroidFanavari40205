package co.fanavari.androidfanavari40205

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import co.fanavari.androidfanavari40205.databinding.ActivityDashboardBinding
import com.google.android.material.button.MaterialButton
import java.time.Duration

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_dashboard)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        print("on create")
        binding.editProfileButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val x: Int = 0
        val buttonTodo: MaterialButton = findViewById(R.id.todoButton)

        val openIntentForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                if (it.resultCode == Activity.RESULT_OK)
                    Toast.makeText(this,
                        it.data?.getStringExtra("message").toString(),
                    Toast.LENGTH_LONG).show()

                else
                    showToast("no data")
            }
        buttonTodo.setOnClickListener {
            createAlarm("test", 12, 15)
        }

       /* binding.profileButton.setOnClickListener {
            openWebPage("https://www.fanavari.co")
        }*/

        binding.imageButtonLogout.setOnClickListener {
            composeEmail(
                arrayOf("hana.rahmati@gmail.com", "fanavari@gmail.com"),
                "contact us"
            )
        }

        binding.layoutCards.setOnClickListener {
            var intent = Intent(
                this,
                IntentExampleActivity::class.java
            )
            val mentorName = binding.textViewMentorName.text
            intent.putExtra("mentorName", mentorName)
            intent.putExtra(Constants.CLASS_NO, 21)
            startActivity(intent)

        }


        binding.lifeCycleCard.setOnClickListener {
            openIntentForResult.launch(
                Intent(this, IntentExampleActivity::class.java).apply {
                    putExtra("id", 1)
                }
            )
        }

        binding.themeCard.setOnClickListener {
            val intent = Intent(this, ComponentActivity::class.java)
            startActivity(intent)
        }

        binding.profileButton.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }

        binding.databaseCard.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            intent.putExtra("PAGE_DB", true)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        print("on start")
    }

    override fun onResume() {
        super.onResume()
        print("on resume")
    }

    override fun onStop() {
        super.onStop()
        print("on stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        print("on destroy")
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        print("on back pressed!")
    }*/

    private fun print(msg: String){
        Log.i("activity state", "activity state : $msg")
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