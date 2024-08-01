package id.kokowilly.filebrowser.lib.common

abstract class DaggerContainer<D, T : Any> {
    lateinit var component: T

    abstract fun initialize(dependency: D): T
}
