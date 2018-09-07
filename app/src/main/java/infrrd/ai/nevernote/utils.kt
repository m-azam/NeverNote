package infrrd.ai.nevernote

import java.util.regex.Pattern

fun validateEmail(email: String): Boolean {
    return Pattern.matches(".+@[a-zA-Z]+\\.com", email)
}

fun checkNull(note: String?):Boolean {
    return note.isNullOrBlank()
}