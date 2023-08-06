package com.igo.kenavgraph_copy2

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRootBinding.bind(requireView())
        // указать (requireView()) - универсальный вариант.
        // вообще здесь передача fragment была через конструктор, поэтому было бы достаточно указать просто (view)

        // Кнопка 1. Зеленый
        binding.openGreenBoxBtn.setOnClickListener {
            openBox(Color.rgb(200, 255, 200))
        }

//        // Кнопка 1.2. Зеленый (классический переход)
//        binding.openGreenBoxClassicBtn.setOnClickListener {
//            openBoxClassic(Color.rgb(255, 255, 200))
//        }

        // Кнопка 2. Желтый
        binding.openYellowBoxBtn.setOnClickListener {
            openBox(Color.rgb(255, 255, 200))
        }


        // Здесь отлавливаем передаваемые данные при возврате с последующего экрана (по методу #2)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("key")?.observe(viewLifecycleOwner) {
            val number = it + 1000
            Toast.makeText(requireContext(), "Random number: $number", Toast.LENGTH_SHORT).show()
        }

        // Здесь отлавливаем передаваемые данные при возврате с последующего экрана (по методу #3)
        parentFragmentManager.setFragmentResultListener(BoxFragment.REQUEST_CODE, viewLifecycleOwner) { _, data ->
            val number = data.getInt(BoxFragment.EXTRA_RANDOM_NUMBER)
            Toast.makeText(requireContext(), "Random number: $number", Toast.LENGTH_SHORT).show()
        }

        // Кнопка "Назад" физическая
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        callback.isEnabled = true

    }

    // метод для перехода тут общий, т.к. различие по кнопкам только в цвете
    private fun openBox (color: Int) {

        // A) Вызываем КОНТРОЛЛЕР
        // B) Делаем переход. Три способа:
        //      1) переход по action (рекомендован),
        //      2) переход по destination (не пробовал),
        //      3) переход обратно по стеку .popBackStack() - (показан в следующем фрагменте)
        // С) Передаем данные. Три способа:
        //      #1) bundleOf (ниже в примере) - можно только вверх по стеку, НО! нельзя обратно (ТРЭШ!!)
        //      #2) .previousBackStackEntry? - встроенный в NavGraph метод (показан в следующем фрагменте), минус - переПРИНИМАЕТ данные при повороте экрана
        //      #3) parentFragmentManager+popBackStack() - не NavGraph, но хороший способ
        //      #4) sharedVM - не рассматривается
        // D) Добавляем анимацию перехода
        //      - TODO

        // ---1--- Это ПЕРЕХОД ПО ACTIONS (Используем везде где возможно)
        // !!! ВЫБОР: для перехода и для передачи вверх
        findNavController().navigate(
            R.id.action_rootFragment_to_boxFragment,            // переход по action
            bundleOf(BoxFragment.ARG_COLOR to color       // передача данных вверх по стеку
                                                                // можно через "запятую" еще и анимацию перехода добавить
            )
        )
    }


//    // Classic way for compare:
//    private fun openBoxClassic (color: Int) {
//            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, BoxFragment())?.addToBackStack(null)?.commit()
//    }




}