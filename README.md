# compose-emoji-picker

## Basic Usage
```
var selectedEmoji by remember {
    mutableStateOf("")
}
EmojiPickerView(
    onEmojiSelect = { unicode ->
        selectedEmoji = unicode
    },
)
```

## Using a BottomSheet
```
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
