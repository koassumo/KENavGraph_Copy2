package com.igo.kenavgraph_copy2

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.igo.kenavgraph_copy2.databinding.FragmentBoxBinding
import kotlin.random.Random


class BoxFragment : Fragment(R.layout.fragment_box) {

    companion object {
        // для получения данных из предыдущего фрагмента
        const val ARG_COLOR = "color"
        // для возвращения данных в предыдущий фрагмент
        const val REQUEST_CODE = "RANDOM_NUMBER_REQUEST_CODE"
        const val EXTRA_RANDOM_NUMBER = "EXTRA_RANDOM_NUMBER"
    }

    private lateinit var binding: FragmentBoxBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoxBinding.bind(requireView())


        // записываем данные, полученные из предыдущего фрагмента (из bundle)
        val color = requireArguments().getInt(ARG_COLOR)
        binding.root.setBackgroundColor(color)

        // ---2--- Это ПЕРЕХОД НАЗАД ПО POPBACKSTACK (без передачи данных)
        // Кнопка ". Просто назад
        binding.goBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // Кнопка 2. На секрет-фрагмент
        binding.openSecretBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_boxFragment_to_secretFragment // только action, без данных, без анимации

//                navOptions {
//                    anim {
//                        enter = R.anim.enter
//                        exit = R.anim.exit
//                        popEnter = R.anim.pop_enter
//                        popExit = R.anim.pop_exit
//                    }
//                }
            )
        }


        // ---2--- Это ПЕРЕХОД НАЗАД ПО POPBACKSTACK (c передачей данных)
        // Кнопка 3. Назад с передачей данных по методу #2.
        binding.generateNumberBtn0.setOnClickListener {
            val number = Random.nextInt(100)

            // Ниже встроенный в Nav вариант передачи данных обратно по стеку.
            // Минусы:
            // - не особо читаемый код
            // - при повороте экрана данные приходят второй раз
            findNavController().previousBackStackEntry?.savedStateHandle?.set("key", number)
            findNavController().popBackStack()
            // а данную строку нужно вставить в предыдущем фрагменте, чтобы отловить данные:
            // findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("key")?.observe(viewLifecycleOwner) {}
        }

        // Кнопка 4. Назад с передачей данных по методу по методу #3
        binding.generateNumberBtn.setOnClickListener {
            val number = Random.nextInt(100)
            // передача данных "назад" здесь идет более удобным, чем встроенный способ:
            parentFragmentManager.setFragmentResult(REQUEST_CODE, bundleOf(EXTRA_RANDOM_NUMBER to number))
            findNavController().popBackStack()

        }

        // Кнопка 5. Назад как вперед (ну, конечно, стек будет переполняться)
        binding.goBackLikeForwardNewNavActionBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_boxFragment_to_rootFragment3
            )
        }

        // Кнопка "Назад" физическая
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

}