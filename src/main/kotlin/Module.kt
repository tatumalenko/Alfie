import aws.sdk.kotlin.services.appconfigdata.AppConfigDataClient
import aws.sdk.kotlin.services.appconfigdata.model.GetLatestConfigurationRequest
import aws.sdk.kotlin.services.appconfigdata.model.StartConfigurationSessionRequest
import com.github.romankh3.image.comparison.ImageComparison
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm
import dev.brachtendorf.jimagehash.hashAlgorithms.MedianHash
import dev.kord.core.Kord
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import java.awt.image.BufferedImage
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import lib.aws.AppConfig
import lib.dedupe.ImageComparator
import lib.dedupe.ImageHasher
import lib.discord.DefineCommand
import lib.discord.InspireCommand
import lib.discord.PlayPhraseCommand
import lib.discord.TextToImageCommand
import lib.inspire.Inspire
import lib.openai.OpenAI
import lib.playphrase.PlayPhrase
import lib.urbandictionary.UrbanDictionary
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@Suppress("USELESS_CAST")
val module = module {
  single { runBlocking { Kord(get<AppConfig>().discord.botToken) } }
  single {
    HttpClient(CIO) {
      install(ContentNegotiation) {
        json(Json {
          prettyPrint = true
          isLenient = true
          ignoreUnknownKeys = true
        })
      }
      install(HttpTimeout)
    }
  }
  single {
    runBlocking {
      val appConfigDataClient = AppConfigDataClient { region = "us-east-1" }
      val startConfigSessionResponse = appConfigDataClient.startConfigurationSession(StartConfigurationSessionRequest {
        applicationIdentifier = "olwvmhe"
        environmentIdentifier = "cohtsbb"
        configurationProfileIdentifier = "2zz1ros"
      })
      val latestConfigResponse = appConfigDataClient.getLatestConfiguration(
        GetLatestConfigurationRequest {
          configurationToken = startConfigSessionResponse.initialConfigurationToken
        }
      )
      val config =
        latestConfigResponse.configuration?.toString(StandardCharsets.UTF_8)
          ?.let { Json.decodeFromString<AppConfig>(it) }
      config
    }
  }
  singleOf(::UrbanDictionary)
  singleOf(::DefineCommand)
  singleOf(::PlayPhrase)
  singleOf(::PlayPhraseCommand)
  singleOf(::Inspire)
  singleOf(::InspireCommand)
  single { OpenAI(get(), get<AppConfig>().openAI) }
  singleOf(::TextToImageCommand)
  single { MedianHash(6) as HashingAlgorithm }
  singleOf(::ImageHasher)
  factory { (img0: BufferedImage, img1: BufferedImage) -> ImageComparison(img0, img1) }
  singleOf(::ImageComparator)
  singleOf(::App)
}
