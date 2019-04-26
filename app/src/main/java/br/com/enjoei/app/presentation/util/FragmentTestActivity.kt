package br.com.enjoei.app.presentation.util

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.enjoei.app.R
import br.com.enjoei.app.presentation.extensions.runTransaction

@VisibleForTesting
class FragmentTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            id = R.id.frameLayoutTest
        })
    }

    fun setFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        manager.runTransaction {
            replace(R.id.frameLayoutTest, fragment)
        }
    }
}