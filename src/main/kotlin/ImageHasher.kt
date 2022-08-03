import com.github.romankh3.image.comparison.ImageComparisonUtil
import dev.brachtendorf.jimagehash.hash.Hash
import dev.brachtendorf.jimagehash.hashAlgorithms.AverageHash
import dev.brachtendorf.jimagehash.hashAlgorithms.MedianHash
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash
import dev.brachtendorf.jimagehash.hashAlgorithms.WaveletHash
import dev.brachtendorf.jimagehash.matcher.exotic.SingleImageMatcher
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object ImageHasher {
  fun hash(image: BufferedImage): Hash {
    val hasher = MedianHash(6)
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

  fun similar(image: BufferedImage, other: BufferedImage): Boolean {
    val hasher = MedianHash(6)
    val imageHash = hasher.hash(image)
    val otherHash = hasher.hash(other)

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

  fun similar3(imagePath: String, otherPath: String): Boolean {
    val hasher = WaveletHash(4, 3)
    val imageHash = hasher.hash(ImageComparisonUtil.readImageFromResources(imagePath))
    val otherHash = hasher.hash(ImageComparisonUtil.readImageFromResources(otherPath))

    return imageHash == otherHash
  }
}
