package com.igo.kenavgraph_copy2

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.igo.kenavgraph_copy2.databinding.FragmentSecretBinding

class SecretFragment : Fragment(R.layout.fragment_secret) {

    private lateinit var binding: FragmentSecretBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecretBinding.bind(requireView())

        // Кнопка (3_0) Переход назад
        binding.goPopBackStackNoData.setOnClickListener {
            findNavController().popBackStack()
        }

        // Кнопка (31_0) "Назад" физическая
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        // Кнопка (4_0) Переход popBackStack, но в конкретный destination
        binding.goDestinationNoData.setOnClickListener {
            findNavController().popBackStack(R.id.rootFragment, false)
        }

    }
}