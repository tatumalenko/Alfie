package lib.dedupe

import com.github.romankh3.image.comparison.ImageComparisonUtil
import dev.brachtendorf.jimagehash.hash.Hash
import dev.brachtendorf.jimagehash.hashAlgorithms.AverageHash
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm
import dev.brachtendorf.jimagehash.hashAlgorithms.MedianHash
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash
import dev.brachtendorf.jimagehash.hashAlgorithms.WaveletHash
import dev.brachtendorf.jimagehash.matcher.exotic.SingleImageMatcher
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.toByteArray
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URI
import javax.imageio.ImageIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageHasher(
  private val httpClient: HttpClient,
  private val hasher: HashingAlgorithm = MedianHash(8)
) {
  fun hash(image: BufferedImage): Hash {
    return hasher.hash(image)
  }

  fun similar(imagePath: String, otherPath: String): Boolean {
    val image = ImageComparisonUtil.readImageFromResources(imagePath)
    val other = ImageComparisonUtil.readImageFromResources(otherPath)
    return similar(image, other)
  }

  fun similar(image: File, other: File): Boolean {
    return similar(ImageIO.read(image), ImageIO.read(other))
  }

  suspend fun similar(image: URI, other: URI): Boolean {
    val imagePayload = httpClient.get(image.toASCIIString())
    val otherPayload = httpClient.get(other.toASCIIString())
    val imageData = withContext(Dispatchers.IO) {
      ImageIO.read(ByteArrayInputStream(imagePayload.bodyAsChannel().toByteArray()))
    }
    val otherData = withContext(Dispatchers.IO) {
      ImageIO.read(ByteArrayInputStream(otherPayload.bodyAsChannel().toByteArray()))
    }
    return similar(imageData, otherData)
  }

  fun similar(image: BufferedImage, other: BufferedImage): Boolean {
    val imageHash = hasher.hash(image)
    val otherHash = hasher.hash(other)
    return imageHash == otherHash
  }

  fun similar(
    imagePath: String,
    otherPath: String,
    hasher: HashingAlgorithm = WaveletHash(4, 3)
  ): Boolean {
    val imageHash = hasher.hash(ImageComparisonUtil.readImageFromResources(imagePath))
    val otherHash = hasher.hash(ImageComparisonUtil.readImageFromResources(otherPath))
    return imageHash == otherHash
  }

  fun similar2(imagePath: String, otherPath: String): Boolean {
    val image = ImageComparisonUtil.readImageFromResources(imagePath)
    val other = ImageComparisonUtil.readImageFromResources(otherPath)

    val matcher = SingleImageMatcher()
    matcher.addHashingAlgorithm(AverageHash(64), .3)
    matcher.addHashingAlgorithm(PerceptiveHash(32), .2)

    return matcher.checkSimilarity(image, other)
  }
}
