package com.example.redlocal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redlocal.ui.theme.RedLocalTheme

// Modelos de datos mejorados
data class OfertaTrabajo(
    val titulo: String, 
    val descripcion: String, 
    val presupuesto: String, 
    val categoria: String,
    val icono: ImageVector
)

data class Tecnico(
    val nombre: String, 
    val especialidad: String, 
    val calificacion: Double,
    val trabajosRealizados: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RedLocalTheme {
                RedLocalApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedLocalApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.OFERTAS) }
    var esModoTecnico by rememberSaveable { mutableStateOf(false) }

    // Lista de ofertas inicial con categorías e iconos descriptivos
    val listaOfertas = remember {
        mutableStateListOf(
            OfertaTrabajo("Gasfitería Urgente", "Se filtró el lavaplatos en la cocina.", "$15.000", "Gasfitería", Icons.Default.Build),
            OfertaTrabajo("Limpieza de Estufa", "Mantención preventiva antes de invierno.", "$25.000", "Limpieza", Icons.Default.Home),
            OfertaTrabajo("Lavado de Auto", "SUV blanco, lavado full a domicilio.", "$12.000", "Lavado", Icons.Default.DirectionsCar),
            OfertaTrabajo("Electricista", "Instalación de focos LED en el patio.", "$20.000", "Técnico", Icons.Default.FlashOn)
        )
    }

    var mostrarSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = { Icon(painterResource(it.icon), contentDescription = it.label) },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = Color(0xFFF8F9FA), // Fondo Gris Claro Moderno
            floatingActionButton = {
                if (currentDestination == AppDestinations.OFERTAS && !esModoTecnico) {
                    ExtendedFloatingActionButton(
                        onClick = { mostrarSheet = true },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        text = { Text("Publicar Pedido") },
                        containerColor = Color(0xFF2E7D32), // Verde para el botón de acción
                        contentColor = Color.White
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                when (currentDestination) {
                    AppDestinations.OFERTAS -> PantallaOfertas(listaOfertas, esModoTecnico)
                    AppDestinations.TECNICOS -> PantallaTecnicos(esModoTecnico)
                    AppDestinations.PERFIL -> PantallaPerfil(esModoTecnico) { esModoTecnico = it }
                }
            }

            if (mostrarSheet) {
                ModalBottomSheet(onDismissRequest = { mostrarSheet = false }, sheetState = sheetState) {
                    FormularioPublicacion(onPublicar = { nueva ->
                        listaOfertas.add(0, nueva)
                        mostrarSheet = false
                    })
                }
            }
        }
    }
}

enum class AppDestinations(val label: String, val icon: Int) {
    OFERTAS("Ofertas", R.drawable.ic_home),
    TECNICOS("Técnicos", R.drawable.ic_account_box),
    PERFIL("Mi Perfil", R.drawable.ic_favorite),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaOfertas(ofertas: List<OfertaTrabajo>, esModoTecnico: Boolean) {
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Gasfitería", "Limpieza", "Lavado", "Técnico")
    
    val ofertasFiltradas = if (categoriaSeleccionada == "Todos") {
        ofertas
    } else {
        ofertas.filter { it.categoria == categoriaSeleccionada }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (esModoTecnico) "Trabajos Disponibles" else "Encuentra Ayuda Local",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1565C0) // Azul Profesional
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        // Fila de Filtros (Horizontal)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categorias) { cat ->
                FilterChip(
                    selected = categoriaSeleccionada == cat,
                    onClick = { categoriaSeleccionada = cat },
                    label = { Text(cat) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE3F2FD),
                        selectedLabelColor = Color(0xFF1565C0)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de tarjetas rediseñadas
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(ofertasFiltradas) { oferta ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF5F5F5)
                            ) {
                                Icon(
                                    imageVector = oferta.icono,
                                    contentDescription = null,
                                    modifier = Modifier.padding(10.dp),
                                    tint = Color(0xFF1565C0)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(oferta.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text(oferta.categoria, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(oferta.descripcion, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = oferta.presupuesto,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF2E7D32) // Verde para resaltar el presupuesto
                            )
                            if (esModoTecnico) {
                                val context = LocalContext.current
                                Button(
                                    onClick = { Toast.makeText(context, "¡Postulación enviada!", Toast.LENGTH_SHORT).show() },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                                ) {
                                    Text("Postular")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaTecnicos(esModoTecnico: Boolean) {
    if (esModoTecnico) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Panel de Control: Revisa tus postulaciones aceptadas.", color = Color.Gray)
        }
    } else {
        val tecnicos = listOf(
            Tecnico("Juan Maestro", "Gasfitería Certificada", 4.8, 156),
            Tecnico("Elena Solar", "Limpieza Industrial", 4.9, 89),
            Tecnico("Pedro Ruedas", "Lavado a Domicilio", 4.7, 210)
        )
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Profesionales Destacados", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(tecnicos) { pro ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pro.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(pro.especialidad, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                    Text(" ${pro.calificacion} • ${pro.trabajosRealizados} realizados", fontSize = 12.sp)
                                }
                            }
                            val context = LocalContext.current
                            Button(onClick = { Toast.makeText(context, "Solicitud enviada a ${pro.nombre}", Toast.LENGTH_SHORT).show() }, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) {
                                Text("Agendar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaPerfil(esModoTecnico: Boolean, onCambioModo: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFFE3F2FD)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color(0xFF1565C0))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Usuario de RedLocal", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Modo Técnico", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Actívalo para ver ofertas de trabajo", style = MaterialTheme.typography.bodySmall)
                }
                Switch(
                    checked = esModoTecnico, 
                    onCheckedChange = onCambioModo,
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2E7D32))
                )
            }
        }
    }
}

@Composable
fun FormularioPublicacion(onPublicar: (OfertaTrabajo) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("¿Qué necesitas hoy?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("¿Qué buscas? (ej: Lavado de auto)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción del trabajo") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
        OutlinedTextField(value = presupuesto, onValueChange = { presupuesto = it }, label = { Text("Tu Presupuesto sugerido") }, modifier = Modifier.fillMaxWidth())
        
        Button(
            onClick = { 
                if(titulo.isNotBlank()) onPublicar(OfertaTrabajo(titulo, desc, presupuesto, "Otros", Icons.Default.Work))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text("Publicar Ahora")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
