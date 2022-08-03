import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ImageComparatorTest {
  @Test
  fun `exactly same image`() {
    assertTrue(ImageComparator.similar("fbi.png", "fbi.png"))
  }

  @Test
  fun `slightly different image`() {
    assertTrue(ImageComparator.similar("fbi.png", "fbi-camera-roll.png"))
  }

  @Test
  fun `cropped image`() {
    assertTrue(ImageComparator.similar("tattoo.jpg", "tattoo-cropped.jpg"))
  }

  @Test
  fun `heavily edited image`() {
    assertTrue(ImageComparator.similar("fbi.png", "fbi-heavily-edited.jpg"))
  }

  @Test
  fun `different image`() {
    assertFalse(ImageComparator.similar("fbi.png", "wings.jpg"))
  }
}
