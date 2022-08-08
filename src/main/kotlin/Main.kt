import org.koin.core.context.startKoin

suspend fun main(args: Array<String>) {
  startKoin {
    // use Koin logger
    printLogger()
    // declare modules
    modules(module)
  }

  Main().run()
}
