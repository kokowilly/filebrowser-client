package id.kokowilly.filebrowser.foundation.logics

class Toggle(
  val isEnable: () -> Boolean,
  val enable: () -> Unit,
  val disable: () -> Unit,
) {
  fun toggle() {
    if (isEnable()) disable() else enable()
  }
}
