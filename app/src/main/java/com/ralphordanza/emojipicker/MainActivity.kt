package com.ralphordanza.emojipicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ralphordanza.emojipicker.ui.theme.EmojiSampleTheme
import com.ralphordanza.emojipickercompose.EmojiPickerView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EmojiSampleTheme {
                var selectedEmoji by remember {
                    mutableStateOf("")
                }
                val sheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
                        skipHalfExpanded = true)
                val scope = rememberCoroutineScope()
                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetContent = {
                        Box(modifier = Modifier.defaultMinSize(minHeight = 1.dp)) {
                            EmojiPickerView(
                                onEmojiSelect = { unicode ->
                                    selectedEmoji = unicode
                                    scope.launch {
                                        sheetState.animateTo(ModalBottomSheetValue.Hidden)
                                    }
                                },
                            )
                        }
                    },
                    content = {
                        Column {
                            Button(
                                onClick = {
                                    scope.launch {
                                        sheetState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                },
                            ) {
                                Text("Open Emoji Picker")
                            }
                            Text(selectedEmoji)
                        }
                    }
                )
            }
        }
    }
}