package com.weskley.lazycolumn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weskley.lazycolumn.model.Person
import com.weskley.lazycolumn.repository.PersonRepository

@Composable
fun CustomItem(person: Person) {
    Row(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = "${person.age}",
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = person.firstName,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = person.lastName,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewItem() {
    val personRepo = PersonRepository()
    val getData = personRepo.getAllData()
    LazyColumn {
        items(items = getData) {
            CustomItem(person = it)
        }
    }
}