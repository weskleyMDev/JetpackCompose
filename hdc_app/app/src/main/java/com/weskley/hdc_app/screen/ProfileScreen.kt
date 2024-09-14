package com.weskley.hdc_app.screen

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.weskley.hdc_app.R
import com.weskley.hdc_app.event.UserEvent
import com.weskley.hdc_app.model.User
import com.weskley.hdc_app.state.UserState
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.viewmodel.UserViewModel
import java.io.File
import java.io.InputStream

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userState by userViewModel.userState.collectAsState()
    val userEvent = userViewModel::userEvent
    val isUpdate = remember { mutableStateOf(false) }
    val editUser = remember { mutableStateOf<User?>(null) }
    UpdateDialog(
        openDialog = userState.openDialog.value,
        userState = userState,
        onConfirm = {
            userEvent(UserEvent.SaveUser)
        },
        onDismiss = {
            userEvent(UserEvent.HideDialog)
        },
        userUpdate = userState.userUpdated,
        isUpdate = isUpdate.value
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userState.users.isEmpty()) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color.Black, CircleShape),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.add_user),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clickable { userEvent(UserEvent.ShowDialog) }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)
                    .background(Color.LightGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(userState.users) { user ->
                    editUser.value = user
                    UserItem(user)
                }
            }
        }
        ExtendedFloatingActionButton(
            modifier = Modifier.padding(bottom = 8.dp, end = 8.dp).align(Alignment.End),
            onClick = {
                isUpdate.value = true
                userEvent(UserEvent.EditUser(editUser.value!!))
            }
        ) {
            Text(text = "EDITAR")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
        }
    }
}

@Composable
fun UpdateDialog(
    openDialog: Boolean,
    userState: UserState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    userUpdate: User?,
    isUpdate: Boolean
) {
    val context = LocalContext.current.applicationContext
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri.value?.let { uri ->
                    Log.d("CameraCapture", "Image saved to: $uri")
                    userState.imageUri.value = uri.toString()
                    saveImageToGallery(context, uri)
                }
            } else {
                Log.e("CameraCapture", "Image capture failed")
            }
        }

    fun createImageUri(): Uri {
        val imageFileName = "HDC_${System.currentTimeMillis()}.jpg"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(storageDir, imageFileName)
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    fun takePicture() {
        val uri = createImageUri()
        imageUri.value = uri
        takePictureLauncher.launch(uri)
    }
    if (openDialog) {
        LaunchedEffect(userUpdate) {
            if (isUpdate && userUpdate != null) {
                userState.name.value = userUpdate.name
                userState.age.value = userUpdate.age.toString()
                userState.bloodType.value = userUpdate.bloodType
                userState.imageUri.value = userUpdate.imageUri
            } else {
                userState.name.value = ""
                userState.age.value = ""
                userState.bloodType.value = ""
                userState.imageUri.value = ""
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
                    Text(text = if (isUpdate) "ATUALIZAR" else "SALVAR")
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = userState.name.value,
                        onValueChange = { userState.name.value = it },
                        label = { Text(text = "Nome", overflow = TextOverflow.Ellipsis) },
                        maxLines = 1,
                    )
                    OutlinedTextField(
                        value = userState.age.value,
                        onValueChange = {
                            val filter = it.filter { char -> char.isDigit() }
                            if (filter.length <= 2) {
                                userState.age.value = filter
                            }
                        },
                        label = { Text(text = "Idade", overflow = TextOverflow.Ellipsis) },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
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
                    OutlinedTextField(
                        value = userState.imageUri.value,
                        onValueChange = { },
                        label = {
                            Text(
                                text = "Imagem",
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        },
                        maxLines = 1,
                        trailingIcon = {
                            IconButton(onClick = { takePicture() }) {
                                Icon(
                                    imageVector = Icons.TwoTone.AddAPhoto,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun UserItem(
    user: User,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.imageUri),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = user.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
        Text(
            text = "Idade: ${user.age} anos",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
        Text(
            text = "Tipo Sanguíneo: ${user.bloodType}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
    }
}

private fun saveImageToGallery(context: Context?, uri: Uri) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (context != null) {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex("_display_name")
                    val mimeTypeIndex = cursor.getColumnIndex("mime_type")

                    val displayName = if (displayNameIndex != -1) {
                        cursor.getString(displayNameIndex)
                    } else {
                        "image_${System.currentTimeMillis()}.jpg"
                    }

                    val mimeType = if (mimeTypeIndex != -1) {
                        cursor.getString(mimeTypeIndex)
                    } else {
                        "image/jpeg"
                    }

                    val imageStream: InputStream? = context.contentResolver.openInputStream(uri)

                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                        put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    val newUri = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    newUri?.let {
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            imageStream?.copyTo(outputStream)
                        }
                    }
                } else {
                    Log.e("CameraCapture", "Cursor did not return any data")
                }
            } ?: Log.e("CameraCapture", "Failed to query content resolver")
        }
    } else {
        val file = File(uri.path ?: return)
        val newFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            file.name
        )
        file.copyTo(newFile, overwrite = true)
    }
}