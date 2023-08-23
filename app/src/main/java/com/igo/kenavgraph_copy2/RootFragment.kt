package com.igo.kenavgraph_copy2

import android.graphics.Color
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.igo.kenavgraph_copy2.databinding.FragmentRootBinding


// 1.Вместо использования onCreateView для связывания с макетным файлом xml используется
// конструктор, куда передаем xml, т.е. не пустые скобки - (R.layout.fragment_root)
// Преимущества:
//  - Код компактным и лаконичным, поскольку вы переносите раздувание макета в конструктор.
//  - Вам не нужно переопределять onCreateView() и возвращать представление вручную.
// Недостатки:
//  - меньше контроля над созданием и настройкой представления, поскольку раздувание макета происходит внутри конструктора фрагмента.
//  - не можете выполнять доп настройки или инициализацию элементов пользовательского интерфейса перед возвращением представления.

// 2. Также желательно вернуться в макетный файл и там тоже проставить обратную ссылку на фрагмент
//    (но если создавался через контексное меню студии new -> Fragment, то ссылка уже есть)
class RootFragment : Fragment(R.layout.fragment_root) {

    private lateinit var binding: FragmentRootBinding
    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRootBinding.bind(requireView())
        // указать (requireView()) - универсальный вариант.
        // вообще здесь передача fragment была через конструктор, поэтому было бы достаточно указать просто (view)


        // Options for fragment transition:
        //      (1_ переход по action (рекомендован),
        //      (2_ переход по destination (не пробовал),
        //      (3_ переход обратно по стеку .popBackStack() на 1 уровень - (показан в следующем фрагменте)
        //      (4_ переход обратно по стеку .popBackStack(), но в конкретный destination - (показан в следующем фрагменте)
        //      (5_ переход обратно по .navigateUp() Отличие от popBackStack: 1.Не закрывает app из root. 2. При вызове из др.проги на непервый фрагмент, не уходит в нее.

        // Options for data transfer:
        //      _0) ничего не передается
        //      _1) bundleOf (ниже в примере) - можно только вверх по стеку, НО! нельзя обратно (ТРЭШ!!)
        //      _2) .previousBackStackEntry? - встроенный в NavGraph метод (показан в следующем фрагменте), минус - переПРИНИМАЕТ данные при повороте экрана
        //      _3) parentFragmentManager+popBackStack() - не NavGraph, но хороший способ
        //      _4) sharedVM - не рассматривается
        // Добавляем анимацию перехода
        //      - TODO


//=============   ВЫХОД В ДРУГОЙ ФРАГМЕНТ ==================================================================================

        //   Стандартный переход nav_graph и стандартная nav_graph передача данных

        // Line (1_1). Yellow
        binding.goForwardBoxYellow.setOnClickListener {
            openBox(Color.rgb(255, 255, 200), "yellow")
        }

        // Line (1_1). Green
        binding.goForwardBoxGreen.setOnClickListener {
            openBox(Color.rgb(200, 255, 200), "green")
        }

//   // Классический переход (не Nav_Graph)
//        // Кнопка . Зеленый
//        binding.goForwardBoxClassic.setOnClickListener {
//            val color: Int = Color.rgb(255, 255, 200)
//            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BoxFragment())?.addToBackStack(null)?.commit()
//        }


        // Кнопка "Назад" физическая
        callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navController = findNavController()
            if (navController.currentDestination?.id != R.id.rootFragment) {
                navController.popBackStack()
            } else {
                // Если находимся в корневом фрагменте, то здесь вы можете выполнить
                // определенные действия или ничего не делать, чтобы предотвратить
                // закрытие приложения
            }
        }
        callback.isEnabled = true


//================ ВХОД ИЗ ДРУГОГО ФРАГМЕНТА ==================================================================================

        // (_2) Здесь отлавливаем передаваемые данные при возврате с последующего экрана (по методу _2)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(PREVIOUS_KEY)
            ?.observe(viewLifecycleOwner) {
                val number = it
                Toast.makeText(requireContext(), "Random number: $number", Toast.LENGTH_SHORT)
                    .show()
            }

        // (_3) Здесь отлавливаем передаваемые данные при возврате с последующего экрана (по методу _3)
        parentFragmentManager.setFragmentResultListener(
            REQUEST_CODE,
            viewLifecycleOwner
        ) { _, data ->
            val number = data.getInt(REQUEST_CODE_EXTRA)
            Toast.makeText(requireContext(), "Random number: $number", Toast.LENGTH_SHORT).show()
        }

    }

    // метод для перехода тут общий, т.к. различие по кнопкам только в цвете
    private fun openBox(color: Int, colorName: String) {
        // Toast.makeText(requireContext(), "1 arg: $color, 2 arg: $colorName", Toast.LENGTH_SHORT).show()

        // ---1--- Это ПЕРЕХОД ПО ACTIONS (Используем везде где возможно)
        // !!! ВЫБОР: для перехода и для передачи вверх
        // A) Вызываем КОНТРОЛЛЕР
        // B) Делаем переход ПО ACTIONS.
        findNavController().navigate(
            R.id.action_rootFragment_to_boxFragment,             // переход по action
            bundleOf(
                ARG_COLOR to color,                   // передача данных вверх по стеку 1го аргумента
                ARG_COLOR_NAME to colorName           // передача данных вверх по стеку 2го аргумента - его обязательно нужно прописать в nav_graph
            )
//            ,                                         // можно через "запятую" еще и анимацию перехода добавить
//            navOptions {
//                anim {
//                    enter = androidx.transition.R.anim.fragment_close_enter
//                    exit = com.google.android.material.R.anim.abc_fade_in
//                }
//            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.isEnabled = false      //для избежания проблем с утечкой памяти
    }

}