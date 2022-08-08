import com.github.romankh3.image.comparison.ImageComparison
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ImageComparatorTest {
  private val imageComparator = ImageComparator { img0, img1 -> ImageComparison(img0, img1) }

  @Test
  fun `exactly same image`() {
    assertTrue(imageComparator.similar("fbi.png", "fbi.png"))
  }

  @Test
  fun `slightly different image`() {
    assertTrue(imageComparator.similar("fbi.png", "fbi-camera-roll.png"))
  }

  @Test
  fun `cropped image`() {
    assertTrue(imageComparator.similar("tattoo.jpg", "tattoo-cropped.jpg"))
  }

  @Test
  fun `heavily edited image`() {
    assertTrue(imageComparator.similar("fbi.png", "fbi-heavily-edited.jpg"))
  }

  @Test
  fun `different image`() {
    assertFalse(imageComparator.similar("fbi.png", "wings.jpg"))
  }
}
