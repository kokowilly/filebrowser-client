package id.kokowilly.filebrowser.foundation.logics

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import id.kokowilly.filebrowser.foundation.R

typealias Feedback = () -> Unit

object TextWatchers {
  private val noFeedback: Feedback = {}

  internal const val REGEX_URL =
    "^(https?://)?(([a-zA-Z\\d\\-]+\\.)+[a-zA-Z]{2,}|\\d{1,3}(\\.\\d{1,3}){3})(:\\d+)?(/\\S*)?$"

  internal const val REGEX_FILENAME =
    "^(?!\\s)(?!.*\\s\\.)[\\w\\s()]+(\\.[a-zA-Z0-9]+)+$"

  fun validateUrl(view: EditText, onEdited: Feedback = noFeedback) {
    view.addTextChangedListener(
      RegexValidationWatcher(
        regex = Regex(REGEX_URL),
        onEdited = onEdited
      ) { isValid ->
        view.error = if (isValid) null
        else
          view.context.getString(R.string.error_invalid_url)
      }
    )
  }

  fun validateUrlOptional(view: EditText, onEdited: Feedback = noFeedback) {
    view.addTextChangedListener(
      OptionalRegexValidationWatcher(
        regex = Regex(REGEX_URL),
        onEdited = onEdited
      ) { isValid ->
        view.error = if (isValid) null
        else
          view.context.getString(R.string.error_invalid_url)
      }
    )
  }

  fun validateLength(view: EditText, minLength: Int, onEdited: Feedback = noFeedback) {
    view.addTextChangedListener(
      LengthValidationWatcher(
        minLength = minLength,
        onEdited = onEdited
      ) { isValid ->
        view.error = if (isValid) null
        else
          view.context.getString(R.string.error_min_length, minLength)
      }
    )
  }

  fun validateFilename(view: EditText, onEdited: Feedback = noFeedback) {
    view.addTextChangedListener(
      RegexValidationWatcher(
        regex = Regex(REGEX_FILENAME),
        onEdited = onEdited
      ) { isValid ->
        view.error = if (isValid) null
        else
          view.context.getString(R.string.error_invalid_filename)
      }
    )
  }
}

private class LengthValidationWatcher(
  private val minLength: Int,
  private val onEdited: Feedback,
  private val onValidated: (Boolean) -> Unit,
) : TextWatcher {
  override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

  override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

  override fun afterTextChanged(editable: Editable?) {
    onValidated.invoke(editable.toString().trim().length >= minLength)
    onEdited.invoke()
  }

}

private class RegexValidationWatcher(
  private val regex: Regex,
  private val onEdited: Feedback,
  private val onValidated: (Boolean) -> Unit,
) : TextWatcher {
  override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

  override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

  override fun afterTextChanged(editable: Editable?) {
    onValidated.invoke(regex.matches(editable.toString()))
    onEdited.invoke()
  }

}

private class OptionalRegexValidationWatcher(
  private val regex: Regex,
  private val onEdited: Feedback,
  private val onValidated: (Boolean) -> Unit,
) : TextWatcher {
  override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

  override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

  override fun afterTextChanged(editable: Editable?) {
    val value = editable.toString()
    if (value.isEmpty()) {
      onValidated.invoke(true)
    } else {
      onValidated.invoke(regex.matches(value))
    }
    onEdited.invoke()
  }

}
