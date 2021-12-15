package com.college.app.ui.todo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import com.college.app.data.daos.TodoDao

@Composable
fun TodoScreen() {

    Text("This is todo screen ")

    val viewModel = hiltViewModel<TodoViewModel>()
    val todos = viewModel.todoList.observeAsState()

    Scaffold() {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(0.3f),
            color = MaterialTheme.colors.onSurface
        ) {

            if (todos.value?.isNotEmpty() == true) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                }
            }


        }
    }

}