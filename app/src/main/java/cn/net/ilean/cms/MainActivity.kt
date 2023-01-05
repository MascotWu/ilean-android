package cn.net.ilean.cms

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.net.ilean.cms.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                App()
            }
        }
    }
}

@Composable
fun Greeting() {
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize()
    ) {
        Text(text = "primary", color = MaterialTheme.colors.primary)
        Text(text = "primaryVariant", color = MaterialTheme.colors.primaryVariant)
        Text(text = "secondary", color = MaterialTheme.colors.secondary)
        Text(text = "secondaryVariant", color = MaterialTheme.colors.secondaryVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Log.e("DefaultPreview", "DefaultPreview: ")
    MyApplicationTheme() {
        Greeting()
    }
}

@Composable
fun Profile(onClick: () -> Unit) {
    Column {
        Text(text = "Profile")
        Button(onClick = onClick) {
            Text("Nav")
        }
    }
}

@Composable
fun Friends(id: Int?, name: String?) {
    Text(text = "Friend: $name($id)")
}