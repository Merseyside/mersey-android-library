package com.merseyside.merseyLib.utils.speech

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

class TextToSpeechHelper private constructor(private var textToSpeech: TextToSpeech) {

    class Builder(private val context: Context) : CoroutineScope {

        private var textToSpeech: TextToSpeech? = null

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Default

        private var pitch: Float = 1f
        private var language: Locale = Locale.US
        private var speechRate: Float = 1f
        private var gender: Gender = Gender.FEMALE

        fun setLanguage(locale: Locale): Builder {
            this.language = locale
            return this
        }

        fun setLanguage(language: String): Builder {
            return setLanguage(Locale(language))
        }

        fun setPitch(pitch: Float): Builder {
            this.pitch = pitch

            return this
        }

        fun setSpeechRate(speechRate: Float): Builder {
            this.speechRate = speechRate
            return this
        }

        fun setGender(gender: Gender): Builder {
            this.gender = gender
            return this
        }

        private fun getMaleVoice(): Voice? {
            val set: MutableSet<String> = HashSet()
            set.add("male")

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Voice(
                    "en-us-x-sfg#male_2-local",
                    Locale("en", "US"),
                    400,
                    200,
                    true,
                    set
                )
            } else {
                null
            }
        }

        @Throws(IllegalStateException::class)
        suspend fun build(): TextToSpeechHelper? {
            val textToSpeech = textToSpeech ?: initTextSpeech()

            return textToSpeech?.let {
                TextToSpeechHelper(it)
            }
        }

        private suspend fun initTextSpeech(): TextToSpeech? {

             return suspendCancellableCoroutine { continuation ->

                 if (continuation.isActive) {
                     var textToSpeech: TextToSpeech? = null
                     textToSpeech = TextToSpeech(
                         context,
                         TextToSpeech.OnInitListener { status ->
                             if (status == TextToSpeech.SUCCESS && textToSpeech != null) {

                                 textToSpeech!!.language =
                                     if (textToSpeech?.isLanguageAvailable(language)
                                         == TextToSpeech.LANG_AVAILABLE
                                     ) {
                                         language
                                     } else {
                                         Locale.US
                                     }

                                 textToSpeech!!.setPitch(pitch)
                                 textToSpeech!!.setSpeechRate(speechRate)

                                 if (gender != Gender.FEMALE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                     textToSpeech!!.voice = getMaleVoice()
                                 }

                                 continuation.resume(textToSpeech!!)


                             } else {
                                 continuation.resume(null)
                             }
                         }
                     )
                 }

             }
        }
    }

    fun speak(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text)
        } else {
            ttsUnder20(text)
        }
    }

    private fun ttsUnder20(text: String) {
        val map: HashMap<String, String> = HashMap()
        map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "MessageId"
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun ttsGreater21(text: String) {
        val utteranceId = this.hashCode().toString() + ""
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun setProgressListener(listener: UtteranceProgressListener) {
        textToSpeech.setOnUtteranceProgressListener(listener)
    }

    fun stop() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

}