package com.example.location_feature.view.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.location_feature.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecoferia.network.FirestoreService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private lateinit var appBarLayout: AppBarLayout
//    private lateinit var bottomNavigationView: BottomNavigationView
//    private lateinit var toolbar: Toolbar
//    private lateinit var firestoreService: FirestoreService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment  // Asegúrate de usar el ID correcto
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nvMenu)
        bottomNavigationView.setupWithNavController(navController)
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nvMenu)
//        bottomNavigationView.setupWithNavController(navController)
//    }
}




//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}



//        // Inicializar las vistas usando findViewById
//        appBarLayout = findViewById(R.id.app_bar)
//        bottomNavigationView = findViewById(R.id.nvMenu)
//        toolbar = findViewById(R.id.Toolbarmain)
//
//        // Obtener el NavHostFragment y NavController
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        // Configurar AppBarConfiguration para los fragmentos que usan BottomNavigationView
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.home_mapa, R.id.comentarios_cuenta, R.id.calendario     // Reemplazar con los IDs de tus fragmentos
//        ))
//
//        // Enlazar la Toolbar y BottomNavigationView con el NavController
//        toolbar.setupWithNavController(navController, appBarConfiguration)
//        bottomNavigationView.setupWithNavController(navController)
//
//        // Agregar un listener al NavController para manejar los cambios de destino
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                // Ocultar AppBarLayout y BottomNavigationView para los fragmentos de login y signup
//                R.id.iniciar_sesion, R.id.crearcuenta -> hideAppBarAndBottomNav()
//                // Mostrar AppBarLayout y BottomNavigationView para otros fragmentos
//                else -> showAppBarAndBottomNav()
//            }
//        }
//    }
//
//    // Método para ocultar AppBarLayout y BottomNavigationView
//    private fun hideAppBarAndBottomNav() {
//        appBarLayout.visibility = AppBarLayout.GONE
//        bottomNavigationView.visibility = BottomNavigationView.GONE
//    }
//
//    // Método para mostrar AppBarLayout y BottomNavigationView
//    private fun showAppBarAndBottomNav() {
//        appBarLayout.visibility = AppBarLayout.VISIBLE
//        bottomNavigationView.visibility = BottomNavigationView.VISIBLE
//    }
