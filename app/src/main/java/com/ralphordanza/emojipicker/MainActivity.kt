package com.ralphordanza.emojipicker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.ralphordanza.emojipicker.ui.theme.EmojiSampleTheme
import com.ralphordanza.emojipickercompose.EmojiPickerView

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EmojiSampleTheme {
                var selectedEmoji by remember {
                    mutableStateOf("")
                }
                EmojiPickerView(
                    onEmojiSelect = { unicode ->
                        selectedEmoji = unicode
                    },
                )
            }
        }
    }
}