package org.ageage.composedraganddroplistitem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.ageage.composedraganddroplistitem.ui.theme.ComposeDragAndDropListItemTheme

data class User(val age: Int, val name: String) {
    companion object {
        fun dummyList(): List<User> {
            return listOf(
                User(10, "ユーザー1"),
                User(20, "ユーザー2"),
                User(30, "ユーザー3"),
                User(40, "ユーザー4"),
                User(50, "ユーザー5"),
                User(60, "ユーザー6"),
                User(70, "ユーザー7"),
                User(80, "ユーザー8"),
                User(90, "ユーザー9"),
                User(100, "ユーザー10"),
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDragAndDropListItemTheme {
                val lazyListState = rememberLazyListState()
                val offset = remember { mutableStateOf(0f) }
                val initialDraggedElement = remember { mutableStateOf<LazyListItemInfo?>(null) }
                val currentIndexOfDraggedItem = remember { mutableStateOf<Int?>(null) }
                val users = remember { mutableStateOf(User.dummyList()) }
                LazyColumn(
                    modifier = Modifier.pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                lazyListState.layoutInfo.visibleItemsInfo
                                    .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
                                    ?.also {
                                        currentIndexOfDraggedItem.value = it.index
                                        initialDraggedElement.value = it
                                    }
                            },
                            onDragEnd = {},
                            onDragCancel = {},
                            onDrag = { change, dragAmount ->
                                change.consumeAllChanges()
                            }
                        )
                    },
                    state = lazyListState
                ) {
                    items(users.value) { user ->
                        Row(
                            modifier = Modifier
                                .height(100.dp)
                                .graphicsLayer(translationY = offset.value),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = user.age.toString()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = user.name
                            )
                        }
                        Divider(color = Color.LightGray)
                    }
                }
            }
        }
    }
}
