import org.koin.core.context.startKoin

suspend fun main(args: Array<String>) {
  startKoin {
    printLogger()
    modules(module)
  }

  Main().run()
}
