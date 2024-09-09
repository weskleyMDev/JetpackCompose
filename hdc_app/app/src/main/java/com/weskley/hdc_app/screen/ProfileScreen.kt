package com.weskley.hdc_app.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.event.UserEvent
import com.weskley.hdc_app.model.User
import com.weskley.hdc_app.state.UserState
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.ui.theme.MediumDarkBlue
import com.weskley.hdc_app.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userState by userViewModel.userState.collectAsState()
    val userEvent = userViewModel::userEvent
    UpdateDialog(
        openDialog = userState.openDialog.value,
        userState = userState,
        onConfirm = {
            userEvent(UserEvent.SaveUser)
        },
        onDismiss = {
            userEvent(UserEvent.HideDialog)
        },
        userUpdate = userState.userUpdated
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(userState.users) { user ->
                UserItem(user)
                Button(
                    onClick = {
                        userEvent(UserEvent.EditUser(user))
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediumDarkBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Editar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun UpdateDialog(
    openDialog: Boolean,
    userState: UserState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    userUpdate: User?
) {
    if (openDialog) {
        LaunchedEffect(userUpdate) {
            if (userUpdate != null) {
                userState.name.value = userUpdate.name
                userState.age.value = userUpdate.age.toString()
                userState.bloodType.value = userUpdate.bloodType
            }
        }
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text(text = "ATUALIZAR")
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = userState.name.value,
                        onValueChange = {
                            userState.name.value = it
                        },
                        label = {
                            Text(text = "Nome")
                        }
                    )
                    OutlinedTextField(
                        value = userState.age.value,
                        onValueChange = {
                            userState.age.value = it
                        },
                        label = {
                            Text(text = "Idade")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = userState.bloodType.value,
                        onValueChange = {
                            userState.bloodType.value = it
                        },
                        label = {
                            Text(text = "Tipo Sanguíneo")
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun GetUserDialog(
    onDismiss: () -> Unit,
    userState: UserState,
    userEvent: (UserEvent) -> Unit
) {
    val context = LocalContext.current.applicationContext
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    userEvent(UserEvent.SaveUser)
                    onDismiss()
                    Toast.makeText(
                        context,
                        "Usuário adicionado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Text(text = "SALVAR")
            }
        },
        title = { Text(text = "ADICIONAR USUÁRIO") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = userState.name.value,
                    onValueChange = { userState.name.value = it },
                    label = { Text(text = "Nome", overflow = TextOverflow.Ellipsis) },
                    maxLines = 1,
                )
                OutlinedTextField(
                    value = userState.age.value,
                    onValueChange = { userState.age.value = it },
                    label = { Text(text = "Idade", overflow = TextOverflow.Ellipsis) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = userState.bloodType.value,
                    onValueChange = { userState.bloodType.value = it },
                    label = {
                        Text(
                            text = "Tipo Sanguíneo",
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    placeholder = { Text(text = "EX.: A-, O+...") },
                    maxLines = 1,
                )
            }
        }
    )
}

@Composable
fun UserItem(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Box(
                modifier = Modifier
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Image(
                modifier = Modifier
                    .size(42.dp)
                    .align(Alignment.BottomCenter)
                    .offset(x = 46.dp),
                painter = painterResource(id = R.drawable.camera),
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = user.name.replaceFirstChar { it.uppercase() },
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
        Text(
            text = user.age.toString(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
        Text(
            text = user.bloodType,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
    }
}