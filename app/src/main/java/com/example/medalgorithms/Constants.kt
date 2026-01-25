// File: app/src/main/java/com/example/medalgorithms/Constants.kt
package com.example.medalgorithms

data class TemplateCategory(
    val id: String,
    val title: String
)

const val ALGORITHMS_PDF_ASSET = "algorithms.pdf"

val TEMPLATE_CATEGORIES: List<TemplateCategory> = listOf(
    TemplateCategory(id = "therapy", title = "Терапия"),
    TemplateCategory(id = "surgery", title = "Хирургия"),
    TemplateCategory(id = "pediatrics", title = "Педиатрия"),
    TemplateCategory(id = "other", title = "Другое"),
)
