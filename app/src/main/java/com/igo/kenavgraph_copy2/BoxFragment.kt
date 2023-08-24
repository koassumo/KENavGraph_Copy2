package com.igo.kenavgraph_copy2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.igo.kenavgraph_copy2.databinding.FragmentBoxBinding
import kotlin.random.Random


class BoxFragment : Fragment(R.layout.fragment_box) {

    private lateinit var binding: FragmentBoxBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoxBinding.bind(requireView())


//================ ВХОД ИЗ ДРУГОГО ФРАГМЕНТА ==================================================================================
        // Считается хорошим тоном сначала прописать аргументы в nav_graph.xml
        // особенно для второго и последующих аргументов
        //         <argument
        //            android:name="color"
        //            app:argType="integer" />
        //        <argument
        //            android:name="colorName"
        //            app:argType="string" />
        // Вручную или также можно сделать автоматически через вкладку design
        // Но! у меня все работает и без этого

        // Ловим данные, полученные из предыдущего фрагмента (из bundle)
        val color = requireArguments().getInt(ARG_COLOR)
        val colorName = requireArguments().getString(ARG_COLOR_NAME)  // не путать .getInt, .getString
        binding.root.setBackgroundColor(color)
        Toast.makeText(requireContext(), "First arg: $color, second arg: $colorName", Toast.LENGTH_SHORT).show()


//=============   ВЫХОД В ДРУГОЙ ФРАГМЕНТ ==================================================================================


        // ниже текст продублирован для удобства пользования:
        // Options for fragment transition:
        //      (1_ переход по action (рекомендован),
        //      (2_ переход по destination (не пробовал),
        //      (3_ переход обратно по стеку .popBackStack() на 1 уровень - (показан в следующем фрагменте)
        //      (4_ переход обратно по стеку .popBackStack(), но в конкретный destination - (показан в следующем фрагменте)
        //      (5_ переход обратно по .navigateUp() Отличие от popBackStack: 1.Не закрывает app из root. 2. При вызове из др.проги на непервый фрагмент, не уходит в нее.

        // Options for data transfer:
        //      _0) ничего не передается
        //      _1) nav bundleOf - можно только вверх по стеку, НО! нельзя обратно (ТРЭШ!!)
        //      _2)  .previousBackStackEntry? - встроенный в NavGraph метод, минус - переПРИНИМАЕТ данные при повороте экрана
        //      _21) .previousBackStackEntry? - встроенный в NavGraph метод, обработан с помощью if чтобы не отображался повторно при повороте экрана
        //      _3) parentFragmentManager+popBackStack() - не NavGraph, но хороший способ
        //      _) sharedViewModel - не рассматривается, данные переходят вперед и назад
        //      _) плагин saveArguments - не рассматривается, данные переходят только вперед  https://www.youtube.com/watch?v=q76ooLPJPsM
        // Добавляем анимацию перехода
        //      - TODO

        // Кнопка (1_0). ПЕРЕХОД На секрет-фрагмент
        binding.goForwardSecretNoData.setOnClickListener {
            openSecret()
        }

        // Кнопка (1_0). Назад как вперед (ну, конечно, стек будет переполняться)
        binding.goBackLikeForwardNoData.setOnClickListener {
            findNavController().navigate(
                R.id.action_boxFragment_to_rootFragment3
            )
        }


        // * Кнопка (3_0). Просто ПЕРЕХОД НАЗАД ПО POPBACKSTACK (без передачи данных)
        binding.goPopBackStackNoData.setOnClickListener {
            findNavController().popBackStack()
        }


        // ДАЛЕЕ встроенный в Nav вариант передачи данных обратно по стеку (В ДВУХ ВАРИАНТАХ !!!)
        // Минусы:
        // - не особо читаемый код (?)
        // - при повороте экрана данные приходят второй раз
        // Отличие этих вариантах не в этом фрагменте, а в предыдущем фрагменте,
        // где обрабатывается эта строка, которая отловливает данные:
        // findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(PREVIOUS_KEY)?.observe(viewLifecycleOwner) {}

        // Кнопка (3_2) ПЕРЕХОД НАЗАД ПО POPBACKSTACK (c передачей данных по методу _2), без обработки данных при приеме
        binding.goPopBackStackAndNavData.setOnClickListener {
            val number = Random.nextInt(100)
            findNavController().previousBackStackEntry?.savedStateHandle?.set(PREVIOUS_KEY, number)
            findNavController().popBackStack()
        }

        // * Кнопка (3_21)
        // ПЕРЕХОД НАЗАД ПО POPBACKSTACK (c передачей данных по методу _21), но с обработкой данных при приеме
        binding.goPopBackStackAndNavDataProcessed.setOnClickListener {
            val number = Random.nextInt(100)
            findNavController().previousBackStackEntry?.savedStateHandle?.set(PREVIOUS_KEY_PROCESSED, number)
            findNavController().popBackStack()

//          For transfer several arguments use bundle:
//            val args = Bundle().apply {
//                putInt(ARG_NUMBER1, 100)
//                putString(ARG_TEXT, "Some text")
//                putBoolean(ARG_FLAG, true)
//                putFloat(ARG_FLOAT, 3.14f)
//            }
//
//            findNavController().previousBackStackEntry?.savedStateHandle?.set(PREVIOUS_ARGS_KEY, args)
//            findNavController().popBackStack()


        }



        // Кнопка (3_3). Назад с передачей данных по методу по методу _3
        binding.goPopBackStackAndAsideData.setOnClickListener {
            val number = Random.nextInt(100)
            // передача данных "назад" здесь идет более удобным, чем встроенный способ:
            parentFragmentManager.setFragmentResult(REQUEST_CODE, bundleOf(REQUEST_CODE_EXTRA to number))
            findNavController().popBackStack()
        }



        // Кнопка (31_0) "Назад" физическая
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }


        // Кнопка (1_0). На секрет-фрагмент
        binding.openSecretBtn.setOnClickListener {
            openSecret()
        }
    }

    private fun openSecret() {
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

}