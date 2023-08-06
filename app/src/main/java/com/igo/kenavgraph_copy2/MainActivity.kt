package com.igo.kenavgraph_copy2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


// https://www.youtube.com/watch?v=tyxt87DX7mw
// 1. Библиотеку в gradle
// 2. Желательно прописать во фрагментах обратные ссылки tools:context=".SecretFragment"
// 3. Res - New - "Android Resource File" - navigation - создаем nav_graph.xml


// -------------- Destinations & Actions -------------------
// 4. Добавляем в nav_graph макетные файлы - это destinations (первый - стартовый отмечен домиком)
// 5. Перетягиваем стрелки связи - actions


// -------------- Host -------------------
// 6. Теперь нужно создать хост, в данном примере хостом служит activity_main.xml, дополняем:
//        <androidx.fragment.app.FragmentContainerView
//        ...
//        android:name="androidx.navigation.fragment.NavHostFragment"
//        app:defaultNavHost="true"
//        app:navGraph="@navigation/nav_graph"
//   true - значит по умолчанию именно этот контейнер, он будет получать высокоуровневые
//   запросы (goBack и т.д.) Если контейнеров несколько, по дефолту - только один
//
//  7. Убрать из MainActivity кусок типа, тк. теперь об этом заботится nav_graph - устанавливает default:
//            if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, MainFragment.newInstance())
//                .commitNow()
//        }
//
// .

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}