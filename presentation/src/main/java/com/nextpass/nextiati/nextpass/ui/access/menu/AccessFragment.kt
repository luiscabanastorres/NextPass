package com.nextpass.nextiati.nextpass.ui.access.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.nextpass.nextiati.nextpass.R
import com.nextpass.nextiati.nextpass.base.BaseFragment
import com.nextpass.nextiati.nextpass.databinding.FragmentAccessBinding
import com.nextpass.nextiati.nextpass.ui.access.menu.adapters.MenuAccessAdapter
import com.nextpass.nextiati.nextpass.ui.access.menu.adapters.MenuAccessAdapter.Listener
import com.nextpass.nextiati.nextpass.ui.access.menu.adapters.MenuAccessAdapter.MenuItemQR
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AccessFragment : BaseFragment<FragmentAccessBinding>(R.layout.fragment_access) {
    var newQRMenuItem = "Nuevo Acceso"
    var dynamicQRMenuItem = "Código dinámico"
    var historyQRMenuItem = "Historial de accesos"
    var adapter: MenuAccessAdapter = MenuAccessAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        with(binding) {
            adapter.setData(creatingMenu()!!.toMutableList())
            adapter.setListener(object : Listener {
                override fun onClick(title: String) {
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
                    when(title){
                        newQRMenuItem->{

                        }
                        dynamicQRMenuItem->{
                            val action = AccessFragmentDirections.actionAccessFragmentToDynamicCodeFragment()
                            findNavController().navigate(action)
                        }
                        historyQRMenuItem->{

                        }
                    }
                }
            })
            recycler.adapter = adapter
        }
    }

    private fun observeViewModel() {

    }

    fun creatingMenu(): ArrayList<MenuItemQR>? {
        val list: ArrayList<MenuItemQR> = ArrayList<MenuItemQR>()
        val newQr = MenuItemQR(
            newQRMenuItem,
            "Crea accesos seguros a tu oficina",
            "#F1BA47",
            R.drawable.ic_qr_menu
        )
        val newCode = MenuItemQR(
            dynamicQRMenuItem,
            "¿Olvidaste tu credencial? Utiliza este código para un acceso seguro a tus oficinas",
            "#172B4D",
            R.drawable.ic_qr_menu
        )
        val History = MenuItemQR(
            historyQRMenuItem,
            "Consulta el historial de accesos que has generado",
            "#F1BA47",
            R.drawable.ic_history_menu
        )
        list.add(newQr)
        list.add(newCode)
        list.add(History)
        return list
    }
}