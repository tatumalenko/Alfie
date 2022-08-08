import com.github.romankh3.image.comparison.ImageComparison
import com.github.romankh3.image.comparison.ImageComparisonUtil
import java.awt.image.BufferedImage

class ImageComparator(
  private val imageComparisonProvider: (BufferedImage, BufferedImage) -> ImageComparison
) {
  fun similar(imagePath: String, otherPath: String): Boolean {
    val image = ImageComparisonUtil.readImageFromResources(imagePath)
    val other = ImageComparisonUtil.readImageFromResources(otherPath)
    val comparison = imageComparisonProvider(image, other).compareImages()
    return comparison.differencePercent < 25
  }
}
