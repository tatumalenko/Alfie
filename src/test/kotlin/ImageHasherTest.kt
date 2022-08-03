import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ImageHasherTest {
  @Test
  fun `all different images`() {
    val dir = File("src/test/resources/different")
    val files = dir.listFiles()!!
    val pairs = files.flatMap { f0 -> files.map { f1 -> if (f0 !== f1) Pair(f0, f1) else null } }.filterNotNull()
    pairs.forEach { (f0, f1) ->
      assertFalse(ImageHasher.similar(f0, f1), "$f0, $f1")
    }
  }

  @Test
  fun `exactly same image`() {
    assertTrue(ImageHasher.similar("fbi.png", "fbi.png"))
  }

  @Test
  fun `slightly different image`() {
    assertTrue(ImageHasher.similar("fbi.png", "fbi-camera-roll.png"))
  }

  @Test
  fun `cropped image`() {
    assertTrue(ImageHasher.similar("tattoo.jpg", "tattoo-cropped.jpg"))
  }

  @Test
  fun `heavily edited image`() {
    assertTrue(ImageHasher.similar("fbi.png", "fbi-heavily-edited.jpg"))
  }

  @Test
  fun `different image`() {
    assertFalse(ImageHasher.similar("fbi.png", "wings.jpg"))
  }
}
