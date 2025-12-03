@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mybank.selfievalidation.presentation.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.view.Surface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LinkedCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.mybank.selfievalidation.presentation.viewmodel.SelfieValidationUiState
import com.example.mybank.selfievalidation.presentation.viewmodel.SelfieValidationViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File
import androidx.lifecycle.LifecycleOwner

@Composable
fun CaptureSelfieScreen(
    viewModel: SelfieValidationViewModel = koinViewModel(),
    onValidationSuccess: (String) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasPermission by remember {
        mutableStateOf(false)
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            hasPermission = granted
        }
    val permissionController = remember {
        SelfiePermissionController(
            context = context,
            permissionLauncher = permissionLauncher
        )
    }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(previewView.display?.rotation ?: Surface.ROTATION_0)
            .build()
    }
    LaunchedEffect(Unit) {
        hasPermission = permissionController.hasCameraPermission()
        if (!hasPermission) {
            permissionController.ensureCameraPermission()
        }
    }
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            startSelfieCamera(previewView, imageCapture, lifecycleOwner)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.clearStatus()
    }

    LaunchedEffect(uiState.status) {
        uiState.status?.let {
            onValidationSuccess(it.name)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionController.ensureCameraPermission()
        }
    }

    CaptureSelfieScreenContent(
        uiState = uiState,
        hasPermission = hasPermission,
        onCapture = {
            captureSelfie(context, imageCapture, viewModel)
        },
        onBack = onBack,
        previewView = previewView
    )
}

@Composable
fun CaptureSelfieScreenContent(
    uiState: SelfieValidationUiState,
    hasPermission: Boolean,
    onCapture: () -> Unit,
    onBack: () -> Unit,
    previewView: PreviewView?,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Valide sua selfie") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tire uma selfie para validar seu acesso ao MyBank.",
                style = MaterialTheme.typography.bodyLarge,
            )

                Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasPermission && previewView != null) {
                        AndroidView(
                            modifier = Modifier.fillMaxWidth(),
                            factory = { previewView }
                        )
                    } else {
                        Text(
                            text = "Nenhuma câmera configurada",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    uiState.selfieBitmap?.let { bitmap ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Selfie capturada",
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                }
            }
            }

            Button(
                onClick = onCapture,
                enabled = !uiState.isLoading && hasPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.LinkedCamera, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.isLoading) "Validando..." else "Tirar selfie"
                )
            }

            if (!hasPermission) {
                Text(
                    text = "Precisamos da permissão de câmera para capturar sua selfie.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun captureSelfie(
    context: Context,
    imageCapture: ImageCapture,
    viewModel: SelfieValidationViewModel
) {
    val executor = ContextCompat.getMainExecutor(context)
    val selfieFile = File(context.cacheDir, "selfie.jpg").apply {
        if (exists()) delete()
        try {
            createNewFile()
        } catch (_: Exception) {
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions.Builder(selfieFile).build()
    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bitmap = BitmapFactory.decodeFile(selfieFile.absolutePath)
                bitmap?.let { viewModel.onSelfieCaptured(it) }
            }

            override fun onError(exception: ImageCaptureException) {
            }
        }
    )
}

private fun startSelfieCamera(
    previewView: PreviewView,
    imageCapture: ImageCapture,
    lifecycleOwner: LifecycleOwner
) {
    val context = previewView.context
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        try {
            val cameraProvider = cameraProviderFuture.get()
            val preview = CameraPreview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }
            val selector = CameraSelector.DEFAULT_FRONT_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, imageCapture)
        } catch (exc: Exception) {
        }
    }, ContextCompat.getMainExecutor(context))
}
@Composable
fun SelfieResultScreen(
    status: String,
    onContinue: () -> Unit,
    onRedo: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Resultado da validação") },
                navigationIcon = {
                    IconButton(onClick = onRedo) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Tentar novamente")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Status: $status",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (status == "APROVADO") {
                    "Sua selfie foi aprovada com sucesso."
                } else {
                    "Precisamos de outra tentativa."
                },
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ir para o dashboard")
            }
        }
    }
}


@Preview(showBackground = true, name = "CaptureSelfieContent")
@Composable
fun CaptureSelfieScreenContentPreview() {
    MaterialTheme {
        CaptureSelfieScreenContent(
            uiState = SelfieValidationUiState(
                status = null,
                selfieBitmap = null,
                error = null,
                isLoading = false
            ),
            hasPermission = true,
            onCapture = {},
            onBack = {},
            previewView = null
        )
    }
}

@Preview
@Composable
fun SelfieResultScreenPreview() {
    MaterialTheme {
        SelfieResultScreen(
            status = "APROVADO",
            onContinue = {},
            onRedo = {}
        )
    }
}
