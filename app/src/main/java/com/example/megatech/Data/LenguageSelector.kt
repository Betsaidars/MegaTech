package com.example.megatech.Data

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.example.megatech.R

data class Language(val code: String, val initials: String, val flagResId: Int)

@Composable
fun LanguageSelector() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var currentLanguageCode by rememberSaveable { mutableStateOf("es") }


    val supportedLanguages = remember {
        listOf(
            Language("es", "ES", R.drawable.flag_spain),
            Language("en", "EN", R.drawable.flag_english),
            Language("fr", "FR", R.drawable.flag_french),
            Language("de", "DE", R.drawable.flag_germany),
            Language("it", "IT", R.drawable.flag_italy),
            Language("pt", "PT", R.drawable.flag_portuguise),
        )
    }

    val currentLanguage = supportedLanguages.find { it.code == currentLanguageCode } ?: supportedLanguages.first()

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = currentLanguage.flagResId),
                contentDescription = currentLanguage.initials,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(currentLanguage.initials, style = MaterialTheme.typography.subtitle2)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            supportedLanguages.forEach { language ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    currentLanguageCode = language.code
                    val localeList = LocaleListCompat.forLanguageTags(language.code)
                    Log.d("LanguageSelector", "Cambiando a idioma: ${language.code}, localeList: $localeList")
                    AppCompatDelegate.setApplicationLocales(localeList)


                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = language.flagResId),
                            contentDescription = language.initials,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(language.initials)
                    }
                }
            }
        }
    }
}