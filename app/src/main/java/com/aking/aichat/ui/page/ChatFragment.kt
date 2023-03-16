package com.aking.aichat.ui.page

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.MATCH_ALL
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognitionService
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.navArgs
import com.aking.aichat.BR
import com.aking.aichat.R
import com.aking.aichat.databinding.FragmentChatBinding
import com.aking.aichat.ui.adapter.ChatAdapter
import com.aking.aichat.ui.helper.ControlFocusInsetsAnimationCallback
import com.aking.aichat.ui.helper.TranslateViewInsetsAnimationListener
import com.aking.aichat.vm.ChatViewModel
import com.google.android.material.transition.MaterialContainerTransform
import com.txznet.common.ui.BaseVMFragment
import timber.log.Timber


/**
 * Created by Rick on 2023-02-24  16:09.
 * Description:
 */
class ChatFragment : BaseVMFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var mSpeechRecognizer: SpeechRecognizer
    private lateinit var mRecognitionIntent: Intent

    override fun getVMExtras(): Any = args.ownerWithChat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // Scope the transition to a view in the hierarchy so we know it will be added under
            // the bottom app bar but over the elevation scale of the exiting HomeFragment.
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            startContainerColor = requireContext().getColor(R.color.white)
            endContainerColor = requireContext().getColor(R.color.reply_blue_50)
        }
    }

    override fun FragmentChatBinding.initView() {
        bindVariables(BR.viewModel to vm, BR.click to ClickProxy(), BR.adapter to ChatAdapter())

        //OnApplyWindowInsetsListener会自动更新我们的根视图向上移动，根据输入法和底部虚拟导航栏的size
        root.setOnApplyWindowInsetsListener { rootView, windowInsets ->    /*BottomAppBar设置wrap_content同样效果*/
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets, rootView).also {
                val barsIme = it.getInsets(WindowInsetsCompat.Type.ime())
                bottomAppbar.updatePadding(bottom = barsIme.bottom)
            }
            windowInsets
        }

        //当点击文字输入框的时候，应用跟随着软键盘一起移动并且创造了一个更流畅的体验
        ViewCompat.setWindowInsetsAnimationCallback(
            bottomAppbar, TranslateViewInsetsAnimationListener(
                bottomAppbar, WindowInsetsCompat.Type.ime()
            )
        )

        //自动处理焦点
        ViewCompat.setWindowInsetsAnimationCallback(
            editMessage, ControlFocusInsetsAnimationCallback(editMessage)
        )

    }

    override fun FragmentChatBinding.initObservable() {
        arguments?.getCharSequence("shortcut")?.let {
            vm.postRequest(it.toString())
        }
    }

    inner class ClickProxy {
        fun clickSend(v: View) {
            binding.editMessage.text.apply {
                vm.postRequest("$this")
                clear()
            }
        }

        fun clickVoice(v: View) {
            if (requireContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                activity?.requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 0x99)
                return
            }
            if (!SpeechRecognizer.isRecognitionAvailable(requireContext()) || !isEnableVoice()) {
                return Toast.makeText(context, "语音服务不可用", Toast.LENGTH_SHORT).show()
            }

            // 开始语音识别 结果在 mSpeechRecognizer.setRecognitionListener(this)回调中
            try {
                mSpeechRecognizer.startListening(mRecognitionIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
            // 停止监听
            //mSpeechRecognizer.stopListening()
            // 取消服务
            //mSpeechRecognizer.cancel()
        }
    }

    fun isEnableVoice(): Boolean {
        if (::mSpeechRecognizer.isInitialized) {
            return true
        }

        // 查找当前系统的内置使用的语音识别服务
        // com.huawei.vassistant/com.huawei.ziri.service.FakeRecognitionService
        val serviceComponent =
            Settings.Secure.getString(requireContext().contentResolver, "voice_recognition_service")
        Timber.d("voice_recognition_service : $serviceComponent")

        if (serviceComponent.isNullOrEmpty()) return false

        val component = ComponentName.unflattenFromString(serviceComponent)
        if (component == null) {
            Timber.d("voice_recognition_service component == null")
            return false
        }

        Timber.d("serviceComponent : " + component.toShortString())

        var isRecognizerServiceValid = false
        var currentRecognitionCmp: ComponentName? = null

        // 查找得到的 "可用的" 语音识别服务
        val intent = Intent(RecognitionService.SERVICE_INTERFACE)
        val list = requireContext().packageManager.queryIntentServices(intent, MATCH_ALL) ?: emptyList()
        if (list.isNotEmpty()) {
            for (info in list) {
                Timber.v("${info.loadLabel(context?.packageManager)} ${info.serviceInfo.packageName} ${info.serviceInfo.name}")

                if (info.serviceInfo.packageName.equals(component.packageName)) {
                    isRecognizerServiceValid = true
                    break
                } else {
                    if (currentRecognitionCmp != null) continue
                    currentRecognitionCmp = ComponentName(info.serviceInfo.packageName, info.serviceInfo.name)
                }
            }
        } else {
            Timber.d("No recognition services installed")
            return false
        }

        Timber.d("isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(requireContext()))

        mSpeechRecognizer = if (isRecognizerServiceValid) {
            SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            SpeechRecognizer.createSpeechRecognizer(context, currentRecognitionCmp)
        }

        mSpeechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Timber.v("onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                Timber.v("onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {
                Timber.v("onBufferReceived")
            }

            override fun onEndOfSpeech() {
                Timber.v("onEndOfSpeech")
            }

            override fun onError(error: Int) {
                Timber.e("onError:$error")
            }

            override fun onResults(results: Bundle) {
                val partialResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (partialResults != null && partialResults.size > 0) {
                    val bestResult = partialResults[0]
                    Timber.i("SpeechRecognition", "onResults bestResult=$bestResult")
                }
            }

            override fun onPartialResults(partialResults: Bundle) {
                val results = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (results != null && results.size > 0) {
                    val bestResult = results[0]
                    Timber.i("SpeechRecognition", "onPartialResults bestResult=$bestResult")
                    binding.editMessage.text.append(bestResult)
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Timber.e("onEvent:$eventType  -- $params")
            }
        })

        if (!::mRecognitionIntent.isInitialized) {
            mRecognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            mRecognitionIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            mRecognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            mRecognitionIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }
        return true

    }
}