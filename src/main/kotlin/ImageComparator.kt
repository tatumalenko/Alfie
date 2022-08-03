import com.github.romankh3.image.comparison.ImageComparison
import com.github.romankh3.image.comparison.ImageComparisonUtil

object ImageComparator {
  fun similar(imagePath: String, otherPath: String): Boolean {
    val image = ImageComparisonUtil.readImageFromResources(imagePath)
    val other = ImageComparisonUtil.readImageFromResources(otherPath)
    val comparison = ImageComparison(image, other).compareImages()
    return comparison.differencePercent < 25
  }
}
