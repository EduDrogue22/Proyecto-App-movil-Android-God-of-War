package com.example.godofwarapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.godofwarapp.databinding.FragmentInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoFragment : Fragment(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var adapter: PersonajeAdapter
    private var _binding: FragmentInfoBinding? = null
    private var lista = mutableListOf<InfoPersonajeItem>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_info, container, false)

        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchNombre.setOnQueryTextListener(this)
        initRecycle()
        //buscarPersonaje()
    }

    //INICIAMOS RECYCLEVIEW
    private fun initRecycle(){
        adapter = PersonajeAdapter(lista)
        binding.rvInfo.layoutManager = LinearLayoutManager(context)
        binding.rvInfo.adapter = adapter
    }

    //Llamamos a la API CON RETROFIT
    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://god-of-war-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buscarPersonaje(character:String){
        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit().create(APIService::class.java).getInfo("characters")
            val juegos = call.body()!!
            activity?.runOnUiThread(){
                if(call.isSuccessful){
                    //MOSTRAMOS EN EL RECYCLERVIEW
                    lista.clear()
                    for (aux in juegos){
                        if(aux.character.equals(character))
                        lista.add(aux)
                        adapter.notifyDataSetChanged()
                    }

                } else {
                    //MOSTRAMOS UN ERROR
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard(){
        val inm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            buscarPersonaje(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean{
        return true
    }

}
