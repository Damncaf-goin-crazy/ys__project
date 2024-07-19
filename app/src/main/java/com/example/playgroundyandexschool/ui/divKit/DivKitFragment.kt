package com.example.playgroundyandexschool.ui.divKit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playgroundyandexschool.utils.theme.AppTodoTheme
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div2.DivData
import org.json.JSONObject

class DivKitFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                AppTodoTheme {
                    Create()
                }
            }
        }
    }

    @Composable
    fun Create() {
        val context = LocalContext.current

        val json = try {
            context.assets.open("divcard.json").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.e("DivKitFragment", "Error reading divCard.json", e)
            return
        }
        val data = try {
            JSONObject(json).asDiv2DataWithTemplates()
        } catch (e: Exception) {
            Log.e("DivKitFragment", "Error parsing JSON to DivData", e)
            return
        }

        DivView(data = data, tag = DivDataTag("MyLog"))
    }

    @Composable
    fun DivView(
        data: DivData,
        tag: DivDataTag,
        modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current
        val divContext = Div2Context(
            baseContext = ContextThemeWrapper(context, context.theme),
            configuration = createDivConfiguration(context),
            lifecycleOwner = LocalLifecycleOwner.current
        )

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = {
                Div2View(divContext)
            },
            update = { view ->
                view.setData(data, tag)
            },
            onRelease = { view ->
                view.cleanup()
            },
        )
    }

    private fun createDivConfiguration(context: Context): DivConfiguration {
        return DivConfiguration
            .Builder(PicassoDivImageLoader(context))
            .visualErrorsEnabled(true)
            .build()
    }

    private fun JSONObject.asDiv2DataWithTemplates(): DivData {
        val environment = DivParsingEnvironment(ParsingErrorLogger.ASSERT)

        try {
            val templates = getJSONObject("templates")
            environment.parseTemplates(templates)
        } catch (e: Exception) {
            Log.e("DivKitFragment", "Error parsing templates", e)
        }

        val card = getJSONObject("card")
        return DivData(environment, card)
    }
}
