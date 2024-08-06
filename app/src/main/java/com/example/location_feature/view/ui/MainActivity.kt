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

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar el NavHostFragment y NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        // Inicializar el BottomNavigationView
        bottomNavigationView = findViewById(R.id.nvMenu)
        bottomNavigationView.setupWithNavController(navController)

        // Configurar AppBarConfiguration para los fragmentos que usan BottomNavigationView
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.home_mapa, R.id.iniciar_sesion, R.id.crear_cuenta // Reemplazar con los IDs de tus fragmentos
        ))

        // Enlazar la Toolbar con el NavController
        val toolbar = findViewById<Toolbar>(R.id.Toolbarmain)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        // Agregar un listener al NavController para manejar los cambios de destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Ocultar BottomNavigationView para los fragmentos de login y signup
                R.id.iniciar_sesion, R.id.crear_cuenta -> hideBottomNav()
                // Mostrar BottomNavigationView para otros fragmentos
                else -> showBottomNav()
            }
        }
    }

    // Método para ocultar BottomNavigationView
    private fun hideBottomNav() {
        bottomNavigationView.visibility = BottomNavigationView.GONE
    }

    // Método para mostrar BottomNavigationView
    private fun showBottomNav() {
        bottomNavigationView.visibility = BottomNavigationView.VISIBLE
    }
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
//
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment  // Asegúrate de usar el ID correcto
//        val navController = navHostFragment.navController
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nvMenu)
//        bottomNavigationView.setupWithNavController(navController)
//    }


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
