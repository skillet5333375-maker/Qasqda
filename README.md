# MedAlgorithms (Android 5.0+)

Проект для Android Studio: 
- minSdk = 21 (Android 5.0)
- Главный экран: две кнопки — «Алгоритмы» и «Шаблоны»
- «Алгоритмы»: встроенный PDF из assets + поиск по тексту + переход на страницу
- «Шаблоны»: категории → шаблоны (создание/редактирование) + «Копировать» в буфер

## Важно
В этом ZIP не включён Gradle Wrapper (gradlew/gradle-wrapper.jar). Android Studio обычно предложит:
- **Add Gradle Wrapper** / **Use gradle wrapper**, либо
- использовать встроенный Gradle.

Если появится диалог, выберите **Add Gradle Wrapper**.

PDF-файл уже добавлен в `app/src/main/assets/algorithms_mossmp_2024.pdf`.
