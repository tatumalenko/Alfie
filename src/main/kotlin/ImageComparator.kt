import com.github.romankh3.image.comparison.ImageComparison
import com.github.romankh3.image.comparison.ImageComparisonUtil
import com.github.romankh3.image.comparison.model.ImageComparisonState

object ImageComparator {
  fun compare() {
    val expectedImage = ImageComparisonUtil.readImageFromResources("IMG_0572.png")
    val actualImage = ImageComparisonUtil.readImageFromResources("edited.jpeg")
    val imageComparisonResult = ImageComparison(expectedImage, actualImage).compareImages()
    println("State: ${imageComparisonResult.imageComparisonState}")
    assert(imageComparisonResult.imageComparisonState == ImageComparisonState.MATCH)
  }
}
